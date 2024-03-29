package hotel;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel implements Comparable<Hotel> {

    private String name;
    private String location;
    private int size;
    private boolean smoking;
    private int rate;
    private LocalDate date;
    private String owner;

    private boolean isValid;

    public Hotel(byte[] data, Map<String, Short> columns) {
        this(data, columns, true);
    }

    public Hotel(byte[] data, Map<String, Short> columns, boolean isValid) {
        int lastIndex = 0;

        for (Map.Entry<String, Short> stringShortEntry : columns.entrySet()) {
            String stringAttr = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim();

            switch (stringShortEntry.getKey()) {
                case "name" -> {
                    this.name = stringAttr;
                }
                case "location" -> {
                    this.location = stringAttr;
                }
                case "size" -> {
                    this.size = Integer.parseInt(stringAttr);
                }
                case "smoking" -> {
                    this.smoking = !stringAttr.equals("N");
                }
                case "rate" -> {
                    this.rate = Integer.parseInt(stringAttr.replace("$", "").replace(".", ""));
                }
                case "date" -> {
                    String dateString = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue()));
                    this.date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                }
                case "owner" -> {
                    this.owner = stringAttr;
                }
            }
            lastIndex += stringShortEntry.getValue();
        }

        this.isValid = isValid;
    }

    public Hotel(String name, String location, int size, boolean smoking, int rate, LocalDate date, String owner) {
        this(name, location, size, smoking, rate, date, owner, true);
    }

    public Hotel(String name, String location, int size, boolean smoking, int rate, LocalDate date, String owner, boolean isValid) {
        this.name = name;
        this.location = location;
        this.size = size;
        this.smoking = smoking;
        this.rate = rate;
        this.date = date;
        this.owner = owner;
        this.isValid = isValid;
    }

    public static int getStartingOffset(String s) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r")) {
            randomAccessFile.seek(4);
            return randomAccessFile.readInt();
        }
    }

    public static int getId(String s) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r")) {
            return randomAccessFile.readInt();
        }
    }

    public static Map<String, Short> readColumns(String s) throws IOException {
        Map<String, Short> map = new LinkedHashMap<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r")) {
            randomAccessFile.seek(8);

            int anzCols = randomAccessFile.readShort();

            for (int i = 0; i < anzCols; i++) {
                int colNameLength = randomAccessFile.readShort();

                byte[] arr = new byte[colNameLength];
                randomAccessFile.read(arr);
                String name = new String(arr);

                short attributeLength = randomAccessFile.readShort();

                map.put(name, attributeLength);
            }
        }

        return map;
    }

    public static Set<Hotel> readHotels(String s) throws IOException {
        Set<Hotel> hotels = readAllHotels(s);

        return hotels.stream().filter(h -> h.isValid).collect(Collectors.toCollection(TreeSet::new));
    }

    public static Set<Hotel> readAllHotels(String s) throws IOException {
        Set<Hotel> set = new LinkedHashSet<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r")) {
            int startOff = getStartingOffset(s);
            int hotelBytes = getHotelBytes(readColumns(s));

            randomAccessFile.seek(startOff);

            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                int checkByte = randomAccessFile.readShort();

                byte[] ar = new byte[hotelBytes];
                randomAccessFile.read(ar);

                if (checkByte == 0) {
                    set.add(new Hotel(ar, readColumns(s), true));
                } else if (checkByte == -32768) {
                    set.add(new Hotel(ar, readColumns(s), false));
                } else {
                    throw new IllegalArgumentException(s);
                }

            }
        }

        return set;
    }

    public static byte[] readData(String fileName) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r")) {
            randomAccessFile.seek(getStartingOffset(fileName));

            byte[] arr = new byte[(int) (randomAccessFile.length() - randomAccessFile.getFilePointer())];
            randomAccessFile.read(arr);

            return arr;
        }
    }

    public byte[] hotelToByteArray(String fileName) throws IOException {
        Map<String, Short> map = readColumns(fileName);

        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        ) {
            if (isValid) dataOutputStream.writeShort(0);
            else dataOutputStream.writeShort(-32768);

            for (Map.Entry<String, Short> stringShortEntry : map.entrySet()) {
                switch (stringShortEntry.getKey()) {
                    case "name" -> dataOutputStream.write(Arrays.copyOf(name.getBytes(), stringShortEntry.getValue()));
                    case "location" ->
                            dataOutputStream.write(Arrays.copyOf(location.getBytes(), stringShortEntry.getValue()));
                    case "size" ->
                            dataOutputStream.write(Arrays.copyOf(String.valueOf(size).getBytes(), stringShortEntry.getValue()));
                    case "smoking" -> {
                        dataOutputStream.write(Arrays.copyOf((smoking ? "Y" : "N").getBytes(), stringShortEntry.getValue()));
                    }
                    case "rate" -> {
                        StringBuilder sb = new StringBuilder(String.valueOf(rate));
                        sb.insert(0, "$");
                        sb.insert(sb.length() - 2, ".");
                        dataOutputStream.write(Arrays.copyOf(sb.toString().getBytes(), stringShortEntry.getValue()));
                    }
                    case "date" ->
                            dataOutputStream.write(Arrays.copyOf(DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date).getBytes(), stringShortEntry.getValue()));
                    case "owner" ->
                            dataOutputStream.write(Arrays.copyOf(owner.getBytes(), stringShortEntry.getValue()));
                }
            }

            return byteArrayOutputStream.toByteArray();
        }
    }

    public static int getHotelBytes(Map<String, Short> map) {
        int val = 0;
        for (Short value : map.values()) {
            val += value;
        }

        return val;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public int getRate() {
        return rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", size=" + size +
                ", smoking=" + smoking +
                ", rate=" + rate +
                ", date=" + date +
                ", owner='" + owner + '\'' +
                '}';
    }

    @Override
    public int compareTo(Hotel o) {
        return Comparator
                .comparing(Hotel::getLocation)
                .thenComparing(Hotel::getName)
                .compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hotel hotel = (Hotel) o;

        if (!Objects.equals(name, hotel.name)) return false;
        return Objects.equals(location, hotel.location);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}