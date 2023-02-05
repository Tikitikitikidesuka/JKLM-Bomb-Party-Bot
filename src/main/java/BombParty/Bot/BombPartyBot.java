package BombParty.Bot;

import BombParty.Client.BombPartyClient;
import BombParty.Client.BombPartyRoom;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.Client.InvalidWordPlayedException;
import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.Implementations.SQLite.SQLiteWordServer;
import BombParty.WordServer.WordServer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class BombPartyBot {

    private final BombPartyClient client;
    private BombPartyRoom room;
    private final WordServer wordServer;
    private final BombPartyBotConfig config;

    public BombPartyBot(BombPartyBotConfig config) {
        this.config = config;
        this.client = new SeleniumBombPartyClient(
                config.getDriver(),
                config.getDriverPath());
        this.room = null;
        this.wordServer = new SQLiteWordServer();
        this.wordServer.connect(config.getDbPath());
    }

    public void joinRoom(String roomCode) throws ConnectionException {
        if(room != null) {
            exitRoom();
            this.wordServer.clearUsed();
        }
        this.room = this.client.joinRoom(roomCode);
    }

    public void exitRoom() {
        this.room.exit();
    }

    public void playRound() {
        this.room.joinRound();
        Collection<Character> missingLetters = new ArrayList<>();
        while (this.room.waitTurn(missingLetters)) {
            boolean validWord = false;
            while (!validWord) {
                String syllable = this.room.getSyllable();
                String word = this.wordServer.getWordContaining(syllable, missingLetters);
                try {
                    try {
                        this.animateTypeWord(word);
                    } catch (InterruptedException ignored) {}
                    this.room.playWord(word);
                } catch (InvalidWordPlayedException invalidWord) {
                    validWord = false;
                    this.wordServer.deleteWord(word);
                }
                try {
                    Thread.sleep(getMilliseconds()*2);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void animateTypeWord(String word) throws InterruptedException{
        for(int i = 1; i < word.length(); i++) {
            this.room.typeWord(word.substring(0, i));
            Thread.sleep(getMilliseconds());
        }
    }

    private long getMilliseconds() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextLong(
                this.config.getMinTypingIntervalMs(),
                this.config.getMaxTypingIntervalMs());
    }

    public BombPartyBotConfig getConfig() {
        return this.config;
    }
}
