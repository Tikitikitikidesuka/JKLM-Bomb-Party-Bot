package BombParty.Bot;

import BombParty.Client.*;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.Implementations.SQLite.SQLiteWordServer;
import BombParty.WordServer.WordServer;
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

        BombPartyTurnData turnData;
        while ((turnData = this.room.waitTurn()) != null) {
            boolean validWord = false;
            while (!validWord) {
                String playWord = this.wordServer.getWordContaining(
                        turnData.getSyllable(), turnData.getMissingLetters());

                try {
                    try {
                        this.animateTypeWord(playWord);
                    } catch (InterruptedException ignored) {}
                    this.room.playWord(playWord);
                    validWord = true;
                } catch (InvalidWordPlayedException invalidWord) {
                    this.wordServer.deleteWord(playWord);
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
