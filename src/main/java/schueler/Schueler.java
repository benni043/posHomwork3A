package schueler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Schueler implements Serializable {

    private String vorname;
    private String nachname;
    private transient int kennung;
    private transient long svn;

    @Serial
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }

    @Serial
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();

    }

    public Schueler() {
        this("Max", "Mustermann", 10000000, 4728220506L);
    }

    public Schueler(String vorname, String nachname, int kennung, long svn) {
        this.vorname = vorname;
        this.nachname = nachname;
        setKennung(kennung);
        setSvn(svn);
    }

    public static String decode(String text, int shiftCount) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);


        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(decode("b", 30));
    }

    public void setKennung(int kennung) {
        if (String.valueOf(kennung).length() != 8) throw new IllegalArgumentException();
        this.kennung = kennung;
    }

    public void setSvn(long svn) {
        String svnString = String.valueOf(svn);
        List<Character> list = new ArrayList<>(svnString.chars().mapToObj(c -> (char) c).toList());

        if (list.size() != 10) throw new IllegalArgumentException();

        int checkVal = Integer.parseInt(String.valueOf(list.get(3)));
        list.remove(3);
        int[] checkValues = {3, 7, 9, 5, 8, 4, 2, 1, 6};

        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += (Integer.parseInt(String.valueOf(list.get(i))) * checkValues[i]);
        }

        sum = sum % 11;

        if (sum != checkVal) throw new IllegalArgumentException();
        else this.svn = svn;

//        String svnString = String.valueOf(svn);
//        int sum = 0;
//        int[] checkValues = {3, 7, 9, 5, 8, 4, 2, 1, 6};
//
//        for (int i = 0; i < svnString.length(); i++) {
//            int digit = Character.getNumericValue(svnString.charAt(i));
//            if (i < 3) {
//                sum += digit * checkValues[i];
//            } else if (i == 3 && sum % 11 == digit) {
//                this.svn = svn;
//                break;
//            }
//        }
    }

    @Override
    public String toString() {
        return "Schueler{" +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", kennung=" + kennung +
                ", svn=" + svn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schueler schueler = (Schueler) o;

        if (kennung != schueler.kennung) return false;
        if (svn != schueler.svn) return false;
        if (!Objects.equals(vorname, schueler.vorname)) return false;
        return Objects.equals(nachname, schueler.nachname);
    }

    @Override
    public int hashCode() {
        int result = vorname != null ? vorname.hashCode() : 0;
        result = 31 * result + (nachname != null ? nachname.hashCode() : 0);
        result = 31 * result + kennung;
        result = 31 * result + (int) (svn ^ (svn >>> 32));
        return result;
    }

//    public static void main(String[] args) {
//        Schueler schueler1 = new Schueler("huff", "huff", 10000000, 7829280755L);
//        Schueler schueler2 = new Schueler("tobi", "huff", 10000000, 4728220506L);
//        Schueler schueler3 = new Schueler("jonas", "huff", 10000000, 4728220506L);
//        Schueler schueler4 = new Schueler("fabian", "huff", 10000000, 4728220506L);
//        Schueler schueler5 = new Schueler("huff", "null", 10000000, 4728220506L);
//    }
}
