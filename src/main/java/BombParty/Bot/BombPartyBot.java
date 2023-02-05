package BombParty.Bot;

import BombParty.Bot.Config.BombPartyBotConfig;
import BombParty.Client.*;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.Client.InvalidWordPlayedException;
import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.Implementations.SQLite.SQLiteWordServer;
import BombParty.WordServer.NoMatchingWordException;
import BombParty.WordServer.WordServer;
import java.util.Random;

public class BombPartyBot {

    private final BombPartyClient client;
    private BombPartyRoom room;
    private final WordServer wordServer;
    private final BombPartyBotConfig config;
    private final Random rng;

    public BombPartyBot(BombPartyBotConfig config) throws WordServerConnectionException {
        this.rng = new Random(System.currentTimeMillis());
        this.config = config;
        this.client = new SeleniumBombPartyClient(
                config.getDriver(),
                config.getDriverPath());
        this.room = null;
        this.wordServer = new SQLiteWordServer(config.getDbPath());
        try {
            this.wordServer.connect();
        } catch (ConnectionException exception) {
            throw new WordServerConnectionException();
        }
    }

    public void joinRoom(String roomCode, String nickname) throws WordServerConnectionException {
        this.client.setNickname(nickname);

        if(room != null) {
            exitRoom();
            try {
                this.wordServer.clearUsed();
            } catch (ConnectionException exception) {
                throw new WordServerConnectionException();
            }
        }
        this.room = this.client.joinRoom(roomCode);
    }

    public void exitRoom() {
        this.room.exit();
    }

    public void playRound() throws WordServerConnectionException {
        this.room.joinRound();

        BombPartyTurnData turnData;
        while ((turnData = this.room.waitTurn()) != null) {
            try {
                boolean validWord = false;
                while (!validWord) {
                    String playWord = this.wordServer.getWordContaining(
                            turnData.getSyllable(), turnData.getMissingLetters());

                    try {
                        this.animateTypeWord(playWord);
                        this.room.playWord(playWord);
                        validWord = true;
                    } catch (InvalidWordPlayedException invalidWord) {
                        this.wordServer.deleteWord(playWord);
                    }

                    try {
                        Thread.sleep(getThinkMilliseconds());
                    } catch (InterruptedException ignored) {}
                }
            } catch (ConnectionException exception) {
                throw new WordServerConnectionException();
            } catch (NoMatchingWordException ignored) {} // Due to word database limitations
        }
    }

    private void animateTypeWord(String word) {
        for(int i = 1; i < word.length(); i++) {
            this.room.typeWord(word.substring(0, i));

            try {
                long ms = getKeystrokeMilliseconds();
                System.out.println(ms);
                Thread.sleep(ms);
            } catch (InterruptedException ignored) {}
        }
    }

    private long getKeystrokeMilliseconds() {
        if (this.config.getAnimationConfig() == null || this.config.getAnimationConfig().getKeystrokeDistribution() == null)
            return 0;

        return (long) Math.max(0, (this.rng.nextGaussian() *
                this.config.getAnimationConfig().getKeystrokeDistribution().stdDeviation +
                this.config.getAnimationConfig().getKeystrokeDistribution().mean));
    }

    private long getThinkMilliseconds() {
        if (this.config.getAnimationConfig() == null || this.config.getAnimationConfig().getThinkDistribution() == null)
            return 0;

        return (long) Math.max(0, (this.rng.nextGaussian() *
                this.config.getAnimationConfig().getThinkDistribution().stdDeviation +
                this.config.getAnimationConfig().getThinkDistribution().mean));
    }

    public BombPartyBotConfig getConfig() {
        return this.config;
    }
}
