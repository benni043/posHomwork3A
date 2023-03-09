package pos;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class PathOfSurvival {

    private final List<Player> playersUnsorted;
    public static final int OFFSET_LENGTH = 1347;
    public static final String PATH = "src/main/resources/pos/playerdata_new.bin";

    public PathOfSurvival() throws IOException {
        playersUnsorted = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(PATH, "r")) {
            randomAccessFile.seek(OFFSET_LENGTH);

            while (randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                randomAccessFile.skipBytes(2);

                String alias = randomAccessFile.readUTF();
                int yearLastMatch = randomAccessFile.readInt();
                int monthLastMatch = randomAccessFile.readInt();
                int dayLastMatch = randomAccessFile.readInt();
                int scorePerMinute = randomAccessFile.readInt();
                int wins = randomAccessFile.readInt();
                int looses = randomAccessFile.readInt();

                Player player = new Player(alias, yearLastMatch, monthLastMatch, dayLastMatch, scorePerMinute, wins, looses);
                playersUnsorted.add(player);
            }
        }
    }

    public void findPlayer(String alias, int newScorePerMinute, int newWins, int newLosses) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(PATH, "rw")) {
            randomAccessFile.seek(OFFSET_LENGTH);

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

    public List<Player> getPlayerListSorted() {
        List<Player> sortedPlayers = new ArrayList<>(playersUnsorted);
        sortedPlayers.sort(null);
        return sortedPlayers;
    }

    public static void main(String[] args) throws IOException {
        PathOfSurvival pos = new PathOfSurvival();

        //orig file
        print(pos);

        System.out.println();

        pos.findPlayer("HunterKiller11111elf", 15000, 1337, 0);
        pos.findPlayer("CyberBob", 2, 1, 354);
        pos.findPlayer("ShadowDeath42", 3, 0, 400);

        //changed file with new values
        print(pos);

        System.out.println();

        PathOfSurvival pos2 = new PathOfSurvival();

        //open the file again with new class and check if data was written correctly
        print(pos2);
    }

    private static void print(PathOfSurvival pos) {
        for (Player player : pos.playersUnsorted) {
            switch (player.alias()) {
                case "HunterKiller11111elf", "CyberBob", "ShadowDeath42" -> {
                    System.out.println(player);
                    System.out.println();
                }
            }
        }
    }

}
