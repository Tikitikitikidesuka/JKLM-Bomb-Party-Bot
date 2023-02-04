package BombParty.Bot;

import BombParty.Client.BombPartyClient;
import BombParty.Client.BombPartyRoom;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.Client.InvalidWordPlayedException;
import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.Implementations.SQLite.SQLiteWordServer;
import BombParty.WordServer.WordServer;

import java.nio.file.Paths;

public class BombPartyBot {

    private BombPartyClient client;
    private BombPartyRoom room;
    private WordServer wordServer;

    public BombPartyBot() {
        this.client = new SeleniumBombPartyClient(
                "webdriver.chrome.driver",
                "driver/chromedriver.exe"); // Change later in config
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
        while (this.room.waitTurn()) {
            try {
                String syllable = this.room.getSyllable();
                // We need to get the letters
                String word = this.wordServer.getWordContaining(syllable);
                this.room.typeWord(word); // Can be deactivated by SPOOKY_MODE = ON
                this.room.playWord(word);
            } catch (InvalidWordPlayedException invalidWord) {

            }
        }
    }
}
