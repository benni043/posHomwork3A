package randomAccess;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.stream.Collectors;

public class Numbers {

    public static List<Number> getContents(String filename) throws IOException {
        List<Number> list = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
            while (randomAccessFile.length() != randomAccessFile.getFilePointer()) {
                if (randomAccessFile.readByte() == 0) list.add(randomAccessFile.readInt());
                else list.add(randomAccessFile.readDouble());
            }
        } catch (EOFException e) {
            throw new IllegalArgumentException("file is invalid");
        }

        return list;
    }

    public static Map<String, Set<Number>> groupByType(List<? extends Number> numbers) {
        return numbers.stream().collect(Collectors.groupingBy(s -> s.getClass().getSimpleName(), Collectors.mapping((number) -> (Number) number, Collectors.toSet())));
    }

    public static void createFile(String filename, List<Number> list) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            for (Number number : list) {
                if (number instanceof Integer) {
                    randomAccessFile.writeByte(0);
                    randomAccessFile.writeInt(number.intValue());
                } else {
                    randomAccessFile.writeByte(1);
                    randomAccessFile.writeDouble(number.doubleValue());
                }
            }

        }
    }


}
