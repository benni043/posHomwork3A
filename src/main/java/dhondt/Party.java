package dhondt;

/**
 * Created 19.01.2023
 *
 * @author Benedikt Huff (Benedikt Huff)
 */
public class Party {

    private final String partyName;
    private int mandate;
    private final long stimmen;

    public Party(String partyName, long stimmen) {
        this.partyName = partyName;
        this.stimmen = stimmen;
        this.mandate = 0;
    }

    public double calculateVotesPerMandate() {
        return (stimmen) / (mandate + 1.0);
    }

    public void addMandat() {
        mandate++;
    }

    public String getPartyName() {
        return partyName;
    }

    public int getMandate() {
        return mandate;
    }
}