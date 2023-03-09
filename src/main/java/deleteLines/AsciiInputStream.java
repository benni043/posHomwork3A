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

    public String readLine2() throws IOException {
        StringBuilder sb = new StringBuilder();
        int character = read();

        if (character == -1) return null;
        sb.append((char) character);

        while ((character = read()) != -1) {
            if (character == '\n') break;
            if (character == '\r') continue;

            sb.append((char) character);
        }
        sb.append("\n");

        return sb.toString();
    }

}
