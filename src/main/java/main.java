import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class main {
    public static void main(String[] args) {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setFetchPolyfillEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            HtmlPage page = webClient.getPage("https://jklm.fun/NXSV");
            System.out.println(page.getTitleText());

            HtmlInput namePrompt = page.getFirstByXPath("/html/body/div[2]/div[3]/form/div[2]/input");
            namePrompt.setValueAttribute("elGoog32");
            HtmlButton submitNameButton = page.getFirstByXPath("/html/body/div[2]/div[3]/form/div[2]/button");
            submitNameButton.click();
            webClient.waitForBackgroundJavaScript(5000);
            System.out.println(page.getWebResponse().getContentAsString());




            HtmlButton joinGameButton = null;
            while(joinGameButton == null) {
                System.out.println("Loading...");
                page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
                webClient.waitForBackgroundJavaScript(5000);
                System.out.println(page.getWebResponse().getContentAsString());
                joinGameButton = page.getFirstByXPath("/html/body/div[2]/div[3]/div[1]/div[1]/button");
            }
            joinGameButton.click();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
