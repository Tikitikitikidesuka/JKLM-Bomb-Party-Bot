package BombParty.Bot;

import BombParty.Client.BombPartyClient;
import BombParty.Client.BombPartyRoom;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.Client.InvalidWordPlayedException;
import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.Implementations.SQLite.SQLiteWordServer;
import BombParty.WordServer.WordServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class BombPartyBot {
    private final BombPartyClient client;
    private final WordServer wordServer;
    private final BombPartyBotConfig config;
    private BombPartyRoom room = null;

    public BombPartyBot(BombPartyBotConfig config) {
        this.config = config;
        this.client = new SeleniumBombPartyClient(
                config.getDriver(),
                config.getDriverPath());
        this.wordServer = new SQLiteWordServer();
        this.wordServer.connect(config.getDbPath());
    }

    public void joinRoom(String roomCode) throws ConnectionException {
        if (room != null) {
            this.exitRoom();
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
                    validWord = true;
                } catch (InvalidWordPlayedException invalidWord) {
                    validWord = false;
                    this.wordServer.deleteWord(word);
                }
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void animateTypeWord(String word) throws InterruptedException{
        Random random = new Random(System.currentTimeMillis());
        for(int i = 1; i < word.length(); i++) {
            this.room.typeWord(word.substring(0, i));
            Thread.sleep(random.nextLong(
                this.config.getMinTypingIntervalMs(),
                this.config.getMaxTypingIntervalMs()));
        }
    }

    public BombPartyBotConfig getConfig() {
        return config;
    }
}
