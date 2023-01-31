import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class main {
    public static void main(String[] args) {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setFetchPolyfillEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            final HtmlPage page = webClient.getPage("https://jklm.fun/GYGE");
            System.out.println(page.getTitleText());

            HtmlInput namePrompt = page.getFirstByXPath("/html/body/div[2]/div[3]/form/div[2]/input");
            namePrompt.setValueAttribute("elGoog");
            HtmlButton submitNameButton = page.getFirstByXPath("/html/body/div[2]/div[3]/form/div[2]/button");
            submitNameButton.click();
            webClient.waitForBackgroundJavaScript(50 * 500);
            System.out.println(page.getWebResponse().getContentAsString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
