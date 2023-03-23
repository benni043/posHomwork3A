package schueler;

public class Schueler {

    private String vorname;
    private String nachname;
    private transient int kennung;
    private transient long svn;

    public static void main(String[] args) {

    }
    public Schueler(String vorname, String nachname, int kennung, long svn) {
        this.vorname = vorname;
        this.nachname = nachname;
        setKennung(kennung);
        setSvn(svn);
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setKennung(int kennung) {
        this.kennung = Math.max(kennung, 0);
    }

    public void setSvn(long svn) {
        // TODO: 23.03.2023
        this.svn = svn;
    }
}
