import BombParty.BombPartyClient;
import BombParty.BombPartyRoom;
import BombParty.Implementations.Selenium.SeleniumBombPartyClient;

public class main {
    public static void main(String[] args) {
        BombPartyClient bpClient = new SeleniumBombPartyClient("webdriver.chrome.driver",
                "chromedriver");

        BombPartyRoom room = bpClient.joinRoom("PHRR");
        room.waitTurn(5);
        System.out.println(room.getSyllable());
        room.exit();
    }
}
