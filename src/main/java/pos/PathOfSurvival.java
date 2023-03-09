package pos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class PathOfSurvival {

    private List<Player> players;
    private List<Player> playersUnsorted;
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

                players.add(new Player(alias, yearLastMatch, monthLastMatch, dayLastMatch, scorePerMinute, wins, looses));
                playersUnsorted.add(new Player(alias, yearLastMatch, monthLastMatch, dayLastMatch, scorePerMinute, wins, looses));
            }
        }

        players.sort(null);
    }

    public void findPlayer(String alias, int newScorePerMinute, int newWins, int newLosses) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            randomAccessFile.seek(offsetLength);

            for (int i = 0; i < playersUnsorted.size(); i++) {
                if (playersUnsorted.get(i).alias().equals(alias)) {
                    randomAccessFile.readShort();
                    randomAccessFile.readUTF();
                    randomAccessFile.readInt();
                    randomAccessFile.readInt();
                    randomAccessFile.readInt();

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
                } else {
                    short x = randomAccessFile.readShort();
                    randomAccessFile.seek(randomAccessFile.getFilePointer() + x);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        PathOfSurvival pos = new PathOfSurvival();

        //orig Datei
        for (Player player : pos.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> System.out.println(player);
            }
        }

        System.out.println();

        pos.findPlayer("HunterKiller11111elf", 15000, 1337, 0);
        pos.findPlayer("CyberBob", 2, 1, 354);
        pos.findPlayer("ShadowDeath42", 3, 0, 400);

        //changed datei with new values
        for (Player player : pos.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> System.out.println(player);
            }
        }

        System.out.println();

        PathOfSurvival pos2 = new PathOfSurvival();

        //open the file again with new class and check if data write was correct
        for (Player player : pos2.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> System.out.println(player);
            }
        }
    }

}
