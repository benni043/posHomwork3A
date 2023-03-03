package deleteLines;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class DeleteLines {
    public static Set<Integer> parse(String[] linesToParse) {
        Set<Integer> deleteLines = new HashSet<>();

        for (String s : linesToParse) {
            if (s.length() != 1) {
                String[] startAndEnd = s.split("-");

                int firstNum = Integer.parseInt(startAndEnd[0]);
                int lastNum = Integer.parseInt(startAndEnd[1]);

                for (int i = firstNum; i < lastNum + 1; i++) {
                    deleteLines.add(i);
                }
            } else {
                deleteLines.add(Integer.parseInt(s));
            }
        }

        return deleteLines;
    }

    public static void writeLines(String inputFileName, String outputFileName, Set<Integer> numbers) throws IOException {
        try (AsciiInputStream asciiInputStream = new AsciiInputStream(inputFileName);
             FileOutputStream fileOutputStream = new FileOutputStream(outputFileName)) {

            String line;
            int lineCount = 0;
            while ((line = asciiInputStream.readLine2()) != null) {
                if (!numbers.contains(lineCount)) {
                    fileOutputStream.write(line.getBytes());
                }
                lineCount++;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String inputFileName = args[0];
        String outputFileName = args[1];
        String[] linesToDelete = new String[args.length - 2];
        System.arraycopy(args, 2, linesToDelete, 0, args.length - 2);

//        String[] linesToDelete = Arrays.copyOfRange(args, 2, args.length - 1);
//        System.out.println(Arrays.toString(linesToDelete));

        Set<Integer> numberOfLinesToDelete = parse(linesToDelete);

        writeLines(inputFileName, outputFileName, numberOfLinesToDelete);
    }

}
