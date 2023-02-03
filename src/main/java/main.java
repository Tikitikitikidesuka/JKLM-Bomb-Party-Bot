import BombParty.BombPartyClient;
import BombParty.BombPartyRoom;
import BombParty.Implementations.Selenium.SeleniumBombPartyClient;
import BombParty.InvalidWordPlayedException;

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
                } catch (InvalidWordPlayedException ignored) {}
            }
        }

//        room.exit();
    }
}
