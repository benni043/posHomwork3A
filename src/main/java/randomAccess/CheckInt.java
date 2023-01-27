package randomAccess;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CheckInt {

    public static void createFile(String filename, double... values) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            randomAccessFile.setLength(0);

            randomAccessFile.writeInt(values.length);
            for (double value : values) {
                randomAccessFile.writeDouble(value);
            }
        }
    }

    public static boolean isValidFile(String filename) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
            int num = randomAccessFile.readInt();

            return randomAccessFile.length() == (8L * num + 4);
        }
    }

    public static String getFileInfo(String filename) throws IOException {
        if(!isValidFile(filename)) return "invalid";

        StringBuilder stringBuilder = new StringBuilder();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
            stringBuilder.append(randomAccessFile.readInt());
            stringBuilder.append("\n");

            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                stringBuilder.append(String.format("%1.2f", randomAccessFile.readDouble()));
                stringBuilder.append(" ");
            }

            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
        }

        return stringBuilder.toString();
    }

    public static void append(String filename, double toAppend) throws IOException {
        if(!isValidFile(filename)) throw new IllegalArgumentException(filename);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            int newLength = randomAccessFile.readInt()+1;

            randomAccessFile.seek(0);
            randomAccessFile.writeInt(newLength);

            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.writeDouble(toAppend);

        }
    }

}
