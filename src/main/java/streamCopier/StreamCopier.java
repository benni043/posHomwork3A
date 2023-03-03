package streamCopier;

import java.io.*;

public class StreamCopier {

    public static void copyByte(InputStream source, OutputStream output) throws IOException {
        int data;
        while ((data = source.read()) != -1)
            output.write(data);
    }

    public static void copyBuffered(InputStream source, OutputStream output, int size) throws IOException {
        int bytesRead;
        byte[] buffer = new byte[size];
        while ((bytesRead = source.read(buffer)) != -1)
            output.write(buffer, 0, bytesRead);
    }

    public static void doStuff(String fileName) throws IOException {
        try (PipedInputStream pipedInputStream = new PipedInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {

            try (PipedOutputStream pipedOutputStream = new PipedOutputStream()) {
                pipedOutputStream.connect(pipedInputStream);
                copyByte(System.in, pipedOutputStream);
            }
            copyBuffered(pipedInputStream, fileOutputStream, 4);
        }
    }

    public static void main(String[] args) throws IOException {
        doStuff("src/main/resources/streamCopier/file.txt");
    }

}