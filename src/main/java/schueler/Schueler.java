package schueler;

import country.Country;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Schueler implements Serializable {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Schueler> schuelerList = new ArrayList<>();
        List<Schueler> newSchuelerList;

        Schueler schueler1 = new Schueler("benni", "huff", 10000000, 7829280755L);
        Schueler schueler2 = new Schueler("tobi", "frischmann", 10000000, 4728220506L);
        Schueler schueler3 = new Schueler("jonas", "pfeifer", 10000000, 4728220506L);
        Schueler schueler4 = new Schueler("fabian", "goetz", 10000000, 4728220506L);
        Schueler schueler5 = new Schueler("basti", "plasek", 10000000, 4728220506L);

        schuelerList.add(schueler1);
        schuelerList.add(schueler2);
        schuelerList.add(schueler3);
        schuelerList.add(schueler4);
        schuelerList.add(schueler5);

        try (
                FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/schueler/writeSchueler.binary");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ) {
            for (Schueler schueler : schuelerList) {
                objectOutputStream.writeObject(schueler);
            }
        }

        try (
                FileInputStream fileInputStream = new FileInputStream("src/main/resources/schueler/writeSchueler.binary");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            newSchuelerList = new ArrayList<>();

            for (int i = 0; i < schuelerList.size(); i++) {
                newSchuelerList.add((Schueler) objectInputStream.readObject());
            }
        }

        for (Schueler schueler : schuelerList) {
            System.out.println(schueler);
        }

        System.out.println();

        for (Schueler schueler : newSchuelerList) {
            System.out.println(schueler);
        }
    }

    private String firstname;
    private String lastname;
    private transient int kennung;
    private transient long svn;

    public static final String[] NUM_VALUES_GERMAN_NAMES = {"null", "eins", "zwei", "drei", "vier", "fuenf", "sechs", "sieben", "acht", "neun"};

    @Serial
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();

        this.firstname = objectInputStream.readUTF();
        this.lastname = objectInputStream.readUTF();
        this.kennung = (int) parseStringNameToValue(decode(objectInputStream.readUTF().toLowerCase(), 26 + 4));
        this.svn = parseStringNameToValue(decode(objectInputStream.readUTF().toLowerCase(), 26 + 4));
    }

    @Serial
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();

        objectOutputStream.writeUTF(firstname);
        objectOutputStream.writeUTF(lastname);
        objectOutputStream.writeUTF(decode(parseValueToStringName(kennung), 4));
        objectOutputStream.writeUTF(decode(parseValueToStringName(svn), 4));
    }

    public static String parseValueToStringName(long numToParse) {
        StringBuilder stringBuilder = new StringBuilder();
        String numString = String.valueOf(numToParse);

        for (int i = 0; i < numString.length(); i++) {
            stringBuilder.append(NUM_VALUES_GERMAN_NAMES[Integer.parseInt(String.valueOf(numString.charAt(i)))]);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }

    public static long parseStringNameToValue(String stringToParse) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] valueStrings = stringToParse.split(" ");

        for (String valueString : valueStrings) {
            for (int i = 0; i < NUM_VALUES_GERMAN_NAMES.length; i++) {
                if (valueString.equalsIgnoreCase(NUM_VALUES_GERMAN_NAMES[i])) stringBuilder.append(i);
            }
        }

        return Long.parseLong(stringBuilder.toString());
    }

    public static String decode(String text, int shiftCount) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int currentLetter = text.charAt(i);

            if(currentLetter == ' ') {
                stringBuilder.append(" ");
                continue;
            }

            final int alphabetLength = 26;
            final int asciiValueOfA = 97;

            int value = ((alphabetLength - (currentLetter - asciiValueOfA) + shiftCount) % alphabetLength) + asciiValueOfA;

            stringBuilder.append(Character.toUpperCase((char) value));
        }

        return stringBuilder.toString();
    }

    public Schueler() {
        this("Max", "Mustermann", 10000000, 4728220506L);
    }

    public Schueler(String firstname, String lastname, int kennung, long svn) {
        this.firstname = firstname;
        this.lastname = lastname;
        setKennung(kennung);
        setSvn(svn);
    }

    public void setKennung(int kennung) {
        if (String.valueOf(kennung).length() != 8 || kennung < 0) throw new IllegalArgumentException();
        this.kennung = kennung;
    }

    public void setSvn(long svn) {
        String svnString = String.valueOf(svn);
        char[] list = svnString.toCharArray();

        if (list.length != 10) throw new IllegalArgumentException();

        int checkVal = Integer.parseInt(String.valueOf(list[3]));
        int[] checkValues = {3, 7, 9, 0, 5, 8, 4, 2, 1, 6};

        int sum = 0;
        for (int i = 0; i < list.length; i++) {
            sum += (Integer.parseInt(String.valueOf(list[i])) * checkValues[i]);
        }

        sum = sum % 11;

        if (sum != checkVal) throw new IllegalArgumentException();
        else this.svn = svn;
    }

    @Override
    public String toString() {
        return "Schueler{" +
                "vorname='" + firstname + '\'' +
                ", nachname='" + lastname + '\'' +
                ", kennung=" + kennung +
                ", svn=" + svn +
                '}';
    }

}