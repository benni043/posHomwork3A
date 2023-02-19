package hotel;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Die Tests greifen direkt auf die Datei zu, um nicht den Produktionscode zu spoilen.
 */
public class HotelTest {

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

    @Test
    public void readsColumns() throws IOException {
        Map<String, Short> expected = getColumnsMap();

        Map<String, Short> result = Hotel.readColumns("src/main/resources/hotel/hotels.db");

        assertIterableEquals(expected.entrySet(), result.entrySet());
    }

    @Test
    public void getsStartingOffset() throws IOException {
        int offset = Hotel.getStartingOffset("src/main/resources/hotel/hotels.db");

        assertEquals(74, offset);
    }

    @Test
    public void createsHotel() {
        byte[] data = null;
        Map<String, Short> columns = null;

        Hotel hotel = new Hotel(data, columns);

        throw new UnsupportedOperationException("""
                TODO 
                Testlogik, welche sicherstellt, dass der angeführte Konstruktor 
                mit den übergebenen Argumenten funktioniert und das richtige Hotel erzeugt
                Dazu müssen die null Zuweisungen natürlich entsprechend modifiziert werden.
                """);
    }

    @Test
    public void cannotReadFromInvalidFile() {
        String filename = "src/main/resources/hotels/invalid.db";

        String errorMsg = assertThrows(IllegalArgumentException.class, () -> Hotel.readHotels(filename)).getMessage();
        assertTrue(errorMsg.contains(filename));
    }

    @Test
    public void readsAllUndeletedHotelsFromGivenFile() throws IOException {
        Hotel contained = new Hotel("Mausefalle",
                "Krems",
                36,
                true,
                10_000,
                LocalDate.of(2019, 11, 12),
                "MAUS");

        Set<Hotel> result = Hotel.readHotels("src/main/resources/hotel/hotels.db");

        assertEquals(31, result.size());
        assertTrue(result.contains(contained));
    }

    @Test
    public void readsAllHotelsFromGivenFile() throws IOException {
        Hotel deleted = new Hotel("Hotel Nr. Eins",
                "Wien",
                4,
                false,
                40_000,
                LocalDate.of(2018, 12, 10),
                "Michael");

        Set<Hotel> result = Hotel.readHotels("src/main/resources/hotel/hotels.db");

        assertFalse(result.contains(deleted));
    }

    @Test
    public void readsHotelsInCorrectOrder() throws IOException {
        Hotel first = new Hotel("Bed & Breakfast & Business",
                "Atlantis",
                4,
                false,
                19_000,
                LocalDate.of(2003, 10, 5),
                "");
        Hotel last = new Hotel("Dew Drop Inn",
                "Xanadu",
                4,
                true,
                20_000,
                LocalDate.of(2003, 1, 19),
                "");

        SortedSet<Hotel> result = (SortedSet<Hotel>) Hotel.readHotels("src/main/resources/hotel/hotels.db");

        assertEquals(first, result.first());
        assertEquals(last, result.last());
    }
}