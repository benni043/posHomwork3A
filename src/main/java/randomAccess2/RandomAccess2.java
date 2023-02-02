package randomAccess2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.*;

public class RandomAccess2 {

    public static void main(String[] args) throws IOException {
        create("src/main/resources/ra2.txt", "src/main/resources/ra2New.txt");

        List<Character> list = new ArrayList<>();
        Map<String, List<Object>> map = read("src/main/resources/ra2New.txt", list);

        createNewFile(map, list, "src/main/resources/raNewNew.txt");
    }

    public static void create(String filename, String newFileName) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")) {
            try (RandomAccessFile randomAccessFileNew = new RandomAccessFile(newFileName, "rw")) {
                randomAccessFileNew.setLength(0);

                while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                    String line = randomAccessFile.readLine();

                    try {
                        int lineInt = Integer.parseInt(line);

                        randomAccessFileNew.writeChar('i');
                        randomAccessFileNew.writeInt(lineInt);
                        continue;
                    } catch (NumberFormatException ignore) {
                    }

                    try {
                        double lineDouble = Double.parseDouble(line);

                        randomAccessFileNew.writeChar('d');
                        randomAccessFileNew.writeDouble(lineDouble);
                        continue;
                    } catch (NumberFormatException ignore) {
                    }

                    if (line.equals("true")) {
                        randomAccessFileNew.writeChar('t');
                    } else if (line.equals("false")) {
                        randomAccessFileNew.writeChar('f');
                    }
                }
            }
        }
    }

    public static Map<String, List<Object>> read(String filename, List<Character> protList) throws IOException {
        Map<String, List<Object>> map = new HashMap<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                char elem = randomAccessFile.readChar();

                switch (elem) {
                    case 'i' -> {
                        if (!map.containsKey("Integer")) map.put("Integer", new ArrayList<>());

                        map.get("Integer").add(randomAccessFile.readInt());
                    }
                    case 'd' -> {
                        if (!map.containsKey("Double")) map.put("Double", new ArrayList<>());

                        map.get("Double").add(randomAccessFile.readDouble());
                    }
                    case 't' -> {
                        if (!map.containsKey("Boolean")) map.put("Boolean", new ArrayList<>());

                        map.get("Boolean").add(true);
                    }
                    case 'f' -> {
                        if (!map.containsKey("Boolean")) map.put("Boolean", new ArrayList<>());

                        map.get("Boolean").add(false);
                    }
                }

                protList.add(elem);
            }
        }

        return map;
    }

    public static void createNewFile(Map<String, List<Object>> map, List<Character> protList, String fileName) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw")) {
            randomAccessFile.setLength(0);

            for (Character character : protList) {
                switch (character) {
                    case 'i' -> {
                        randomAccessFile.writeBytes((map.get("Integer").get(0) + "\n"));
                        map.get("Integer").remove(map.get("Integer").get(0));
                    }
                    case 'd' -> {
                        randomAccessFile.writeBytes(map.get("Double").get(0) + "\n");
                        map.get("Double").remove(map.get("Double").get(0));
                    }
                    case 't', 'f' -> {
                        randomAccessFile.writeBytes(map.get("Boolean").get(0) + "\n");
                        map.get("Boolean").remove(map.get("Boolean").get(0));
                    }
                }
            }
        }
    }
}
