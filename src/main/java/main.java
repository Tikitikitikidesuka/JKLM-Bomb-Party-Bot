import BombParty.BombPartyClient;
import BombParty.BombPartyRoom;
import BombParty.Implementations.Selenium.SeleniumBombPartyClient;

public class main {
    public static void main(String[] args) {
        BombPartyClient bpClient = new SeleniumBombPartyClient("webdriver.chrome.driver",
                "driver/chromedriver.exe");

        BombPartyRoom room = bpClient.joinRoom("PYVU");
        room.join();

        System.out.println(room.waitTurn());
        System.out.println(room.getSyllable());
        room.playWord(room.getSyllable());

        System.out.println(room.waitTurn());
        System.out.println(room.getSyllable());
        room.playWord(room.getSyllable());

        System.out.println(room.waitTurn());
        System.out.println(room.getSyllable());
        room.playWord(room.getSyllable());

        room.exit();
    }
}
