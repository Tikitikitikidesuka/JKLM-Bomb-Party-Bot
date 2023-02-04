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
    private final BotConfig config;

    public BombPartyBot(BotConfig config) {
        this.config = config;
        this.client = new SeleniumBombPartyClient(
                "webdriver.chrome.driver",
                Paths.get("driver/chromedriver.exe")); // Change later in config
        this.room = null;
        this.wordServer = new SQLiteWordServer();
        this.wordServer.connect(Paths.get("words.db")); // Change later in config
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
        Collection<Character> letters = new ArrayList<>();
        while (this.room.waitTurn(letters)) {
            String syllable = this.room.getSyllable();
            String word = this.wordServer.getWordContaining(syllable, letters);
            animateTypeWord(word);
            try {
                this.room.playWord(word);
            } catch (InvalidWordPlayedException invalidWord) {
                this.wordServer.deleteWord(word);
            }
        }
    }

    private void animateTypeWord(String word) {
        Random random = new Random(System.currentTimeMillis());
        for(int i = 1; i < word.length(); i++) {
            this.room.typeWord(word.substring(0, i));
            try {
                wait(this.config.getMinTypingIntervalMs()
                        + random.nextLong()%this.config.getMaxTypingIntervalMs());
            } catch (InterruptedException ignored) {}
        }
    }

    public BotConfig getConfig() {
        return config;
    }
}
