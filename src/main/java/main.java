import BombParty.Bot.BombPartyBot;
import BombParty.Bot.Config.BombPartyBotAnimationConfig;
import BombParty.Bot.Config.BombPartyBotConfig;

import java.nio.file.Paths;

public class main {
    public static void main(String[] args) throws InterruptedException {
        BombPartyBotAnimationConfig animationConfig = new BombPartyBotAnimationConfig();
        animationConfig.setKeystrokeDistribution(226, 15); // 89.56 WPM
        animationConfig.setThinkTimeDistribution(500, 150);
        BombPartyBotConfig config = new BombPartyBotConfig(
                "webdriver.chrome.driver",
                Paths.get("driver/chromedriver.exe"),
                Paths.get("jklm_words.db"),
                animationConfig);

        BombPartyBot bot = new BombPartyBot(config);

        bot.joinRoom("RKRJ");
        while (true) {
            bot.playRound();
        }
    }
}
