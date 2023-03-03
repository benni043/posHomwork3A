package hotel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Hotelverwaltung {

    private final String path;
    private List<Hotel> hotels;
    private int versionsNummer = 0;

    public Hotelverwaltung(String path) throws IOException {
        this.hotels = new ArrayList<>();
        this.path = path;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            if (randomAccessFile.length() > 0) {
                randomAccessFile.seek(Hotel.getStartingOffset(path));

                hotels.addAll(Hotel.readAllHotels(path));
                versionsNummer = Hotel.getId(path);
            } else {
                writeHeader();
            }
        }
    }

    private static Map<String, Short> getColumnsMap() {
        Map<String, Short> columnLengths = new LinkedHashMap<>();
        columnLengths.put("name", (short) 64);
        columnLengths.put("location", (short) 64);
        columnLengths.put("size", (short) 4);
        columnLengths.put("smoking", (short) 1);
        columnLengths.put("rate", (short) 8);
        columnLengths.put("date", (short) 10);
        columnLengths.put("owner", (short) 8);
        return columnLengths;
    }

    public void writeHeader() throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            randomAccessFile.writeInt(versionsNummer);
            randomAccessFile.writeInt(74);
            randomAccessFile.writeShort(getColumnsMap().size());

            for (Map.Entry<String, Short> stringShortEntry : getColumnsMap().entrySet()) {
                randomAccessFile.writeShort(stringShortEntry.getKey().length());
                randomAccessFile.writeBytes(stringShortEntry.getKey());
                randomAccessFile.writeShort(stringShortEntry.getValue());
            }
        }
    }

    public void newDataFile(List<Hotel> hotels) throws IOException {
        this.hotels = hotels;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            randomAccessFile.setLength(Hotel.getStartingOffset(path));
            randomAccessFile.seek(0);

            versionsNummer++;
            randomAccessFile.writeInt(versionsNummer);

            randomAccessFile.seek(Hotel.getStartingOffset(path));

            for (Hotel hotel : hotels) {
                randomAccessFile.write(hotel.hotelToByteArray(path));
            }
        }
    }

    public void appendHotel(Hotel hotel) throws IOException {
        hotels.add(hotel);
        newDataFile(hotels);
    }

    public void insertHotel(Hotel hotel, int index) throws IOException {
        hotels.add(index, hotel);
        newDataFile(hotels);
    }

    public boolean setHotelValid(boolean gueltig, int index) throws IOException {
        boolean changed = hotels.get(index).isValid() != gueltig;

        hotels.get(index).setValid(gueltig);
        newDataFile(hotels);

        return changed;
    }

    public Hotel changeHotelData(Hotel hotel, int index) throws IOException {
        Hotel oldHotel = hotels.get(index);
        hotels.set(index, hotel);
        newDataFile(hotels);
        return oldHotel;
    }
}
