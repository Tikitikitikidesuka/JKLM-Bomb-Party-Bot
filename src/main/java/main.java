import BombParty.Bot.BombPartyBot;
import BombParty.Bot.BombPartyBotConfig;
import BombParty.Client.BombPartyClient;
import BombParty.Client.BombPartyRoom;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.Client.InvalidWordPlayedException;

import java.nio.file.Paths;

public class main {
    public static void main(String[] args) throws InterruptedException {
        BombPartyBotConfig config = new BombPartyBotConfig(
                "webdriver.chrome.driver",
                Paths.get("driver/chromedriver.exe"),
                Paths.get("words.db"),
                4,
                12);
        BombPartyBot bot = new BombPartyBot(config);

        bot.joinRoom("GXVA");
        while (true) {
            bot.playRound();
        }
        /*BombPartyClient bpClient = new SeleniumBombPartyClient("webdriver.chrome.driver",
                Paths.get("driver/chromedriver.exe"));

        BombPartyRoom room = bpClient.joinRoom("PYVU");

        while(true) {
            room.joinRound();
            while (room.waitTurn()) {
                try {
                    room.playWord(room.getSyllable());
                } catch (InvalidWordPlayedException invalidWord) {

                }
            }
        }*/
    }
}
