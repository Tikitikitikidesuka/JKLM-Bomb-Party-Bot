package BombParty.Bot;

import BombParty.Bot.Config.BombPartyBotConfig;
import BombParty.Client.*;
import BombParty.Client.InvalidWordPlayedException;
import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.NoMatchingWordException;
import java.util.Random;

public class BombPartyBot {
    private BombPartyRoom room;
    private final BombPartyBotConfig config;
    private final Random rng;

    public BombPartyBot(BombPartyBotConfig config) throws ConnectionException {
        this.rng = new Random(System.currentTimeMillis());
        this.config = config;
        this.room = null;
        this.config.getWordServer().connect();
    }

    public void joinRoom(String roomCode, String nickname) throws ConnectionException, InvalidRoomCodeException, RoomNotFoundException {
        this.config.getClient().setNickname(nickname);

        if(this.room != null) {
            exitRoom();
            this.config.getWordServer().clearUsed();
        }

        this.room = this.config.getClient().joinRoom(roomCode);
    }

    public void exitRoom() {
        this.room.exit();
    }

    public void playRound() throws ConnectionException {
        this.room.joinRound();

        BombPartyTurnData turnData;
        while ((turnData = this.room.waitTurn()) != null) {
            // Insert used words into the word server if they are not in it
            // and mark them as used for this session
            this.config.getWordServer().insertWords(turnData.getUsedWords());
            this.config.getWordServer().markUsedWords(turnData.getUsedWords());

            try {
                boolean validWord = false;
                while (!validWord) {
                    String playWord = this.config.getWordServer().getWordContaining(
                            turnData.getSyllable(), turnData.getMissingLetters());

                    try {
                        this.animateTypeWord(playWord);
                        this.room.playWord(playWord);
                        validWord = true;
                    } catch (InvalidWordPlayedException invalidWord) {
                        this.config.getWordServer().deleteWord(playWord);
                    }

                    try {
                        Thread.sleep(getThinkMilliseconds());
                    } catch (InterruptedException ignored) {}
                }
            } catch (NoMatchingWordException ignored) {} // Due to word database limitations
        }
    }

    private void animateTypeWord(String word) {
        for(int i = 1; i < word.length(); i++) {
            this.room.typeWord(word.substring(0, i));

            try {
                Thread.sleep(getKeystrokeMilliseconds());
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
