package hotel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Hotel implements Comparable<Hotel> {

    private String name;
    private String location;
    private int size;
    private boolean smoking;
    private int rate;
    private LocalDate date;
    private String owner;

    public Hotel(byte[] data, Map<String, Short> columns) {
        int lastIndex = 0;

        for (Map.Entry<String, Short> stringShortEntry : columns.entrySet()) {
            switch (stringShortEntry.getKey()) {
                case "name" -> {
                    this.name = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim();
                }
                case "location" -> {
                    this.location = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim();
                }
                case "size" -> {
                    this.size = Integer.parseInt(new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim());
                }
                case "smoking" -> {
                    this.smoking = !new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim().equals("N");
                }
                case "rate" -> {
                    String rateStr = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim();
                    rateStr = rateStr.replace("$", "").replace(".", "");

                    this.rate = Integer.parseInt(rateStr);
                }
                case "date" -> {
                    String dateString = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue()));
                    this.date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                }
                case "owner" -> {
                    this.owner = new String(Arrays.copyOfRange(data, lastIndex, lastIndex + stringShortEntry.getValue())).trim();
                }
            }
            lastIndex += stringShortEntry.getValue();
        }
    }
    public Hotel(String name, String location, int size, boolean smoking, int rate, LocalDate date, String owner) {
        this.name = name;
        this.location = location;
        this.size = size;
        this.smoking = smoking;
        this.rate = rate;
        this.date = date;
        this.owner = owner;
    }

    public static int getStartingOffset(String s) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "rw")) {
            randomAccessFile.seek(4);
            return randomAccessFile.readInt();
        }
    }

    public static Map<String, Short> readColumns(String s) throws IOException {
        Map<String, Short> map = new LinkedHashMap<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "rw")) {
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
        Set<Hotel> set = new TreeSet<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "rw")) {
            int startOff = getStartingOffset(s);
            int hotelBytes = getHotelBytes(readColumns(s));

            randomAccessFile.seek(startOff);

            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                int checkByte = randomAccessFile.readShort();

                if (checkByte == 0) {
                    byte[] ar = new byte[hotelBytes];
                    randomAccessFile.read(ar);

                    set.add(new Hotel(ar, readColumns(s)));
                } else if (checkByte == -32768) {
                    randomAccessFile.seek(randomAccessFile.getFilePointer() + hotelBytes);
                } else {
                    throw new IllegalArgumentException(s);
                }
            }
        }

        return set;
    }

    public static byte[] readData(String fileName) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw")) {
            randomAccessFile.seek(getStartingOffset(fileName));

            byte[] arr = new byte[(int) (randomAccessFile.length() - randomAccessFile.getFilePointer())];
            randomAccessFile.read(arr);

            return arr;
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
        return Comparator.comparing(Hotel::getLocation).thenComparing(Hotel::getName).compare(this, o);
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