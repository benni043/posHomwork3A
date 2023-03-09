package pos;

import java.util.Comparator;

public record Player(String alias, int yearLastMatch, int monthLastMatch, int dayLastMatch, int scorePerMinute,
                     int wins, int looses) implements Comparable<Player> {


    @Override
    public int compareTo(Player o) {
        return Comparator.comparing(Player::scorePerMinute).reversed().compare(this, o);
    }
}