import BombParty.Client.BombPartyClient;
import BombParty.Client.BombPartyRoom;
import BombParty.Client.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.Client.InvalidWordPlayedException;

public class main {
    public static void main(String[] args) {
        BombPartyClient bpClient = new SeleniumBombPartyClient("webdriver.chrome.driver",
                "driver/chromedriver.exe");

        BombPartyRoom room = bpClient.joinRoom("PYVU");

        while(true) {
            room.join();
            while (room.waitTurn()) {
                try {
                    room.playWord(room.getSyllable());
                } catch (InvalidWordPlayedException invalidWord) {

                }
            }
        }
    }
}
