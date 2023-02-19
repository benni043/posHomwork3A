package hotel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.*;

public class Hotel {

    private String name;
    private String location;
    private int size;
    private boolean smoking;
    private int rate;
    private Date date;
    private String owner;

    public Hotel(byte[] data, Map<String, Short> columns) {
    }

    public Hotel(String mausefalle, String krems, int i, boolean b, int i1, LocalDate of, String maus) {
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

                short colLength = randomAccessFile.readShort();

                map.put(name, colLength);
            }
        }

        return map;
    }

    public static Set<Hotel> readHotels(String s) throws IOException {
        Set<Hotel> set = new LinkedHashSet<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "rw")) {
            randomAccessFile.seek(getStartingOffset(s));

            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                int checkByte = randomAccessFile.readShort();
                if(checkByte != 0x0000) throw new IllegalArgumentException();

                Map<String, Short> map = readColumns(s);

                String location;
                String name;
                int size;
                boolean smoking;
                int rate;
                Date date;
                String owner;

                for (Short value : map.values()) {
                    name =
                }
            }
        }

        return set;
    }


}
