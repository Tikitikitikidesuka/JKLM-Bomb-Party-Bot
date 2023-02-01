import BombParty.BombPartyClient;
import BombParty.BombPartyClientException;
import BombParty.Implementations.Selenium.SeleniumBombPartyClient;

public class main {
    public static void main(String[] args) {
        BombPartyClient bpClient = new SeleniumBombPartyClient("webdriver.chrome.driver",
                "chromedriver");

        bpClient.joinRoom("TFPB");
    }
}
