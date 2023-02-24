package hotel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HotelverwaltungTest {

    private final static String TESTFILE = "src/main/resources/hotel/hotelsTest.db";

    @Test
    public void newDataFileTest() throws IOException {
        new Hotelverwaltung(TESTFILE).newDataFile(new ArrayList<>());
        assertEquals(0, Hotel.readHotels(TESTFILE).size());

        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel("Huff", "Neulengbach", 16, true, 123, LocalDate.now(), "Benni", true));
        hotels.add(new Hotel("Frischmann", "Gablitz", 12, false, 23123, LocalDate.now(), "Tobi", false));
        hotels.add(new Hotel("Pfeiffer", "Tulln", 50, true, 6, LocalDate.now(), "Jonas", false));


        new Hotelverwaltung(TESTFILE).newDataFile(hotels);

        assertIterableEquals(hotels, Hotel.readAllHotels(TESTFILE));
        assertEquals(2, Hotel.getId(TESTFILE));
    }

    @Test
    public void addHotelTest() throws IOException {
        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel("Huff", "Neulengbach", 16, true, 123, LocalDate.now(), "Benni", true));
        hotels.add(new Hotel("Frischmann", "Gablitz", 12, false, 23123, LocalDate.now(), "Tobi", false));
        hotels.add(new Hotel("Pfeiffer", "Tulln", 50, true, 6, LocalDate.now(), "Jonas", false));

        new Hotelverwaltung(TESTFILE).newDataFile(hotels);

        Hotelverwaltung Hotelverwaltung = new Hotelverwaltung(TESTFILE);

        Hotel hotel = new Hotel("Goetz", "Langenrohr", 123, true, 123, LocalDate.now(), "Fabian", false);
        hotels.add(hotel);
        Hotelverwaltung.appendHotel(hotel);

        hotel = new Hotel("Plasek", "Eichgraben", 123, false, 132, LocalDate.now(), "Basti", true);
        hotels.add(hotel);
        Hotelverwaltung.appendHotel(hotel);

        assertIterableEquals(hotels, Hotel.readAllHotels(TESTFILE));
        assertEquals(3, Hotel.getId(TESTFILE));
    }

    @Test
    public void insertHotelTest() throws IOException {
        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel("Huff", "Neulengbach", 16, true, 123, LocalDate.now(), "Benni", true));
        hotels.add(new Hotel("Frischmann", "Gablitz", 12, false, 23123, LocalDate.now(), "Tobi", false));
        hotels.add(new Hotel("Pfeiffer", "Tulln", 50, true, 6, LocalDate.now(), "Jonas", false));

        new Hotelverwaltung(TESTFILE).newDataFile(hotels);

        Hotelverwaltung Hotelverwaltung = new Hotelverwaltung(TESTFILE);

        Hotel hotel = new Hotel("Goetz", "Langenrohr", 123, true, 123, LocalDate.now(), "Fabian", false);
        hotels.add(2, hotel);
        Hotelverwaltung.insertHotel(hotel, 2);

        hotel = new Hotel("Plasek", "Eichgraben", 123, false, 132, LocalDate.now(), "Basti", true);
        hotels.add(1, hotel);
        Hotelverwaltung.insertHotel(hotel, 1);

        assertIterableEquals(hotels, Hotel.readAllHotels(TESTFILE));
        assertEquals(3, Hotel.getId(TESTFILE));
    }

    @Test
    public void setHotelValidTest() throws IOException {
        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel("Huff", "Neulengbach", 16, true, 123, LocalDate.now(), "Benni", true));
        hotels.add(new Hotel("Frischmann", "Gablitz", 12, false, 23123, LocalDate.now(), "Tobi", false));
        hotels.add(new Hotel("Pfeiffer", "Tulln", 50, true, 6, LocalDate.now(), "Jonas", false));

        new Hotelverwaltung(TESTFILE).newDataFile(hotels);

        Hotelverwaltung Hotelverwaltung = new Hotelverwaltung(TESTFILE);

        hotels.get(2).setValid(true);
        Hotelverwaltung.setHotelValid(true, 2);

        hotels.get(1).setValid(false);
        Hotelverwaltung.setHotelValid(false, 1);

        assertFalse(Hotelverwaltung.setHotelValid(false, 1));

        assertIterableEquals(hotels, Hotel.readAllHotels(TESTFILE));
        assertEquals(4, Hotel.getId(TESTFILE));
    }

    @Test
    public void changeHotelDataTest() throws IOException {
        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel("Huff", "Neulengbach", 16, true, 123, LocalDate.now(), "Benni", true));
        hotels.add(new Hotel("Frischmann", "Gablitz", 12, false, 23123, LocalDate.now(), "Tobi", false));
        hotels.add(new Hotel("Pfeiffer", "Tulln", 50, true, 6, LocalDate.now(), "Jonas", false));
        new Hotelverwaltung(TESTFILE).newDataFile(hotels);

        Hotelverwaltung Hotelverwaltung = new Hotelverwaltung(TESTFILE);

        Hotel hotel = new Hotel("Goetz", "Langenrohr", 123, true, 123, LocalDate.now(), "Fabian", false);
        hotels.set(2, hotel);
        Hotelverwaltung.changeHotelData(hotel, 2);

        hotel = new Hotel("Plasek", "Eichgraben", 123, false, 132, LocalDate.now(), "Basti", true);
        Hotel old = hotels.set(1, hotel);
        assertEquals(old, Hotelverwaltung.changeHotelData(hotel, 1));

        assertIterableEquals(hotels, Hotel.readAllHotels(TESTFILE));
        assertEquals(3, Hotel.getId(TESTFILE));
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Path path = Paths.get(TESTFILE);
        if (!Files.exists(path))
            return;
        Files.delete(path);
    }

}