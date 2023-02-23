package hotel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
public class Hotelverwaltung {

    private String path;
    private List<Hotel> hotels;

    public Hotelverwaltung(String path) {
        this.path = path;
        this.hotels = new ArrayList<>();
    }

    public void newDataFile(List<Hotel> hotels) throws IOException {
        this.hotels = hotels;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            randomAccessFile.seek(Hotel.getStartingOffset(path));

            for (Hotel hotel : hotels) {
                randomAccessFile.write(hotel.hotelToByteArray(path));
            }
        }
     }

     public void appendHotel(Hotel hotel) throws IOException {
         hotels.add(hotel);

         try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(hotel.hotelToByteArray(path));
         }
     }

     public void insertHotel(Hotel hotel, int index) throws IOException {
        hotels.add(index, hotel);
        newDataFile(hotels);
     }

    public boolean setHotelValid(boolean gueltig, int index) {
        h
    }

     public Hotel changeHotelData(Hotel hotel, int index) throws IOException {
        Hotel oldHotel = hotels.get(index);
        hotels.set(index, hotel);
        newDataFile(hotels);
        return oldHotel;
     }
}
