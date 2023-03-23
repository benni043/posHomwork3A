package country;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Country implements Serializable {

    @Serial
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.name = objectInputStream.readUTF();
        this.hauptstadt = objectInputStream.readUTF();
    }

    @Serial
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeUTF(this.name.toUpperCase());
        objectOutputStream.writeUTF(this.hauptstadt.toUpperCase());
    }

    private String kurzzeichen;
    private String name;
    private String hauptstadt;
    private int flaeche;
    private int einwohnerzahl;

    public Country(String kurzzeichen, String name, String hauptstadt, int flaeche, int einwohnerzahl) {
        this.kurzzeichen = kurzzeichen;
        this.name = name;
        this.hauptstadt = hauptstadt;
        this.flaeche = flaeche;
        this.einwohnerzahl = einwohnerzahl;
    }

    public static List<Country> readCSV(String filename) throws IOException {
        List<Country> countryList = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw")) {
            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                try {
                    String[] values = randomAccessFile.readLine().split(";");
                    Country country = new Country(values[0], values[1], values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]));

                    countryList.add(country);
                } catch (IllegalArgumentException illegalArgumentException) {
                    System.err.println("error");
                }
            }
        }

        return countryList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHauptstadt() {
        return hauptstadt;
    }

    public void setHauptstadt(String hauptstadt) {
        this.hauptstadt = hauptstadt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (flaeche != country.flaeche) return false;
        if (einwohnerzahl != country.einwohnerzahl) return false;
        if (!Objects.equals(kurzzeichen, country.kurzzeichen)) return false;
        if (!Objects.equals(name, country.name)) return false;
        return Objects.equals(hauptstadt, country.hauptstadt);
    }

    @Override
    public int hashCode() {
        int result = kurzzeichen != null ? kurzzeichen.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (hauptstadt != null ? hauptstadt.hashCode() : 0);
        result = 31 * result + flaeche;
        result = 31 * result + einwohnerzahl;
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "kurzzeichen='" + kurzzeichen + '\'' +
                ", name='" + name + '\'' +
                ", hauptstadt='" + hauptstadt + '\'' +
                ", flaeche=" + flaeche +
                ", einwohnerzahl=" + einwohnerzahl +
                '}';
    }
}