import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;

public class main {
    public static void main(String[] args) {
        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
            webClient.getOptions().setFetchPolyfillEnabled(true);
            final HtmlPage page = webClient.getPage("https://jklm.fun/PFCF");
            System.out.println(page.getTitleText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
