package dhondt;

import java.util.*;
import java.util.stream.Collectors;

public class SeatCalculator {

    private final Set<String> parties;

    /**
     * @param parties Parteien, welche für die Wahl registriert sind, darf nicht leer sein
     */
    public SeatCalculator(Set<String> parties) {
        if (parties.isEmpty()) throw new IllegalArgumentException("parties field is empty");
        this.parties = parties;
    }

    /**
     * Berechnet die Sitz-/Mandatsverteilung nach D'Hondt
     *
     * @param votesPerParty für jede Partei die Anzahl an abgegeben Stimmen für diese Partei, darf nicht leer sein
     * @param seats         Anzahl zu vergebender Sitze/Mandate, muss positiv sein
     * @return für jede Partei die Anzahl ihr zugeteilter Sitze/Mandate
     */
    public Map<String, Integer> calculate(Map<String, Long> votesPerParty, int seats) {
        if (seats <= 0) throw new IllegalArgumentException("negative seats");

        List<Party> partyList = new ArrayList<>();

        for (String party : parties) {
            partyList.add(new Party(party, votesPerParty.get(party)));
        }

        if(partyList.size() == 0) throw new IllegalArgumentException();

        for (int i = 0; i < seats; i++) {
            Party wonParty = null;
            double votesPerMandate = 0;

            for (Party party : partyList) {
                if (votesPerMandate < party.calculateVotesPerMandate()) {
                    wonParty = party;
                    votesPerMandate = party.calculateVotesPerMandate();
                }
            }

            assert wonParty != null;
            wonParty.addMandat();
        }

        return partyList.stream()
                .filter(party -> party.getMandate() > 0)
                .collect(Collectors.toMap(Party::getPartyName, Party::getMandate));
    }
}
