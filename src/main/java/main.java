import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class main {
    public static void main(String[] args) {
        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
            webClient.getOptions().setFetchPolyfillEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            HtmlPage page = webClient.getPage("https://jklm.fun/QFHK");
            System.out.println(page.getTitleText());

            webClient.waitForBackgroundJavaScript(10_000);

            HtmlInput nicknamePrompt = page.getFirstByXPath("//input[@class='styled nickname']");
            nicknamePrompt.setValueAttribute("elGoog32");
            //nicknamePrompt.type("XD323232");
            //nicknamePrompt.type("\n");
            HtmlButton submitNameButton = page.getFirstByXPath("//button[.='OK']");
            submitNameButton.click();

            page.executeJavaScript("loadGame('bombparty')");

            webClient.waitForBackgroundJavaScript(10_000);

            System.out.println(page.getWebResponse().getContentAsString());
            /*
            System.out.println(page.getWebResponse().getContentAsString());
            DomElement gameIframe = page.createElement("iframe");
            gameIframe.setAttribute("src", "https://falcon.jklm.fun/games/bombparty");
            HtmlElement gameDiv = page.getFirstByXPath("//div[@class='game']");
            gameDiv.appendChild(gameIframe);

             */

            /*
            HtmlButton joinButton = page.getFirstByXPath("//input[@class='styled joinRound']");
            joinButton.click();
            webClient.waitForBackgroundJavaScript(5000);

             */

            //HtmlInput nicknameButton = page.getFirstByXPath("//input[@class, 'styled nickname')");
            //nicknamePrompt.type("XD323232");

            /*
            HtmlInput namePrompt = page.getFirstByXPath("/html/body/div[2]/div[3]/form/div[2]/input");
            namePrompt.setValueAttribute("elGoog32");
            HtmlButton submitNameButton = page.getFirstByXPath("/html/body/div[2]/div[3]/form/div[2]/button");
            submitNameButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            System.out.println(page.getWebResponse().getContentAsString());

            HtmlButton joinGameButton = page.getFirstByXPath("/html/body/div[2]/div[3]/div[1]/div[1]/button");
            joinGameButton.click();
            */

            while (true) {}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
