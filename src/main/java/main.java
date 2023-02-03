import BombParty.BombPartyClient;
import BombParty.BombPartyRoom;
import BombParty.Implementations.Selenium.SeleniumBombPartyClient;

public class main {
    public static void main(String[] args) {
        BombPartyClient bpClient = new SeleniumBombPartyClient("webdriver.chrome.driver",
                "driver/chromedriver.exe");

        BombPartyRoom room = bpClient.joinRoom("ZYWE");
        room.join();
        room.waitTurn();
        room.playWord(room.getSyllable());
        System.out.println(room.getSyllable());
        //room.exit();
    }
}
