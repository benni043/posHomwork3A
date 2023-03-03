package deleteLines;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsciiInputStream extends FileInputStream {
    public AsciiInputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public AsciiInputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public AsciiInputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int character = read();

        while (character != -1 && character != '\n') {
            if(character == '\r') {
                character = read();
                continue;
            }

            sb.append((char) character);
            character = read();
        }
        return sb.toString();
    }

    public String readLine2() throws IOException {
        StringBuilder sb = new StringBuilder();
        int character;

        while ((character = read()) != -1) {
            if (character == '\n') break;
            if(character == '\r') continue;

            sb.append((char) character);
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

    public static void main(String[] args) throws IOException {
        AsciiInputStream asciiInputStream = new AsciiInputStream("src/main/resources/deleteLines/input.txt");

        List<String> list = new ArrayList<>();
        String line;
        while ((line = asciiInputStream.readLine2()) != null) {
            list.add(line);
        }

        System.out.println(list);
    }
}
