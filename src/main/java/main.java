import BombParty.Bot.BombPartyBot;
import BombParty.Bot.Config.BombPartyBotConfig;
import BombParty.Bot.Config.BombPartyBotAnimationConfig;
import BombParty.Bot.WordServerConnectionException;

import java.nio.file.Paths;

public class main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage:\n\tbombpartybot <room> <nickname>");
            System.exit(-1);
        }

        String roomCode = args[0];
        String nickname = args[1];

        BombPartyBotAnimationConfig animationConfig = new BombPartyBotAnimationConfig();
        animationConfig.setKeystrokeDistribution(226, 15); // 89.56 WPM
        animationConfig.setThinkTimeDistribution(500, 150);
        BombPartyBotConfig config = new BombPartyBotConfig(
                "webdriver.chrome.driver",
                Paths.get("driver/chromedriver.exe"),
                Paths.get("jklm_words.db"),
                animationConfig);

        try {
            BombPartyBot bot = new BombPartyBot(config);

            bot.joinRoom(roomCode, nickname);
            while (true) {
                bot.playRound();
            }
        } catch (WordServerConnectionException exception) {

        }
    }
}
