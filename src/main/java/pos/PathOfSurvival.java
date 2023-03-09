package pos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class PathOfSurvival {

    private final List<Player> players;
    private final List<Player> playersUnsorted;
    public static final int offsetLength = 1347;
    public static final String path = "src/main/resources/pos/playerdata_new.bin";

    public PathOfSurvival() throws IOException {
        players = new ArrayList<>();
        playersUnsorted = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r")) {
            randomAccessFile.seek(offsetLength);

            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                randomAccessFile.seek(randomAccessFile.getFilePointer() + 2);

                String alias = randomAccessFile.readUTF();
                int yearLastMatch = randomAccessFile.readInt();
                int monthLastMatch = randomAccessFile.readInt();
                int dayLastMatch = randomAccessFile.readInt();
                int scorePerMinute = randomAccessFile.readInt();
                int wins = randomAccessFile.readInt();
                int looses = randomAccessFile.readInt();

                Player player = new Player(alias, yearLastMatch, monthLastMatch, dayLastMatch, scorePerMinute, wins, looses);
                players.add(player);
                playersUnsorted.add(player);
            }
        }

        players.sort(null);
    }

    public void findPlayer(String alias, int newScorePerMinute, int newWins, int newLosses) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            randomAccessFile.seek(offsetLength);

            for (int i = 0; i < playersUnsorted.size(); i++) {
                if (playersUnsorted.get(i).alias().equals(alias)) {
                    randomAccessFile.skipBytes(2);
                    randomAccessFile.readUTF();
                    randomAccessFile.skipBytes(12);

                    randomAccessFile.writeInt(newScorePerMinute);
                    randomAccessFile.writeInt(newWins);
                    randomAccessFile.writeInt(newLosses);

                    Player player = playersUnsorted.get(i);

                    String aliasOld = player.alias();
                    int yearLastMatch = player.yearLastMatch();
                    int monthLastMatch = player.monthLastMatch();
                    int dayLastMatch = player.dayLastMatch();

                    playersUnsorted.remove(i);
                    playersUnsorted.add(i, new Player(aliasOld, yearLastMatch, monthLastMatch, dayLastMatch, newScorePerMinute, newWins, newLosses));
                    break;
                }
                randomAccessFile.skipBytes(randomAccessFile.readShort());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        PathOfSurvival pos = new PathOfSurvival();

        //orig file
        for (Player player : pos.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> System.out.println(player);
            }
        }

        System.out.println();

        pos.findPlayer("HunterKiller11111elf", 15000, 1337, 0);
        pos.findPlayer("CyberBob", 2, 1, 354);
        pos.findPlayer("ShadowDeath42", 3, 0, 400);

        //changed file with new values
        for (Player player : pos.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> System.out.println(player);
            }
        }

        System.out.println();

        PathOfSurvival pos2 = new PathOfSurvival();

        //open the file again with new class and check if data was written correctly
        for (Player player : pos2.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> System.out.println(player);
            }
        }
    }

}
