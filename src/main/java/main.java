import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;

public class main {
    public static void main(String[] args) {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://htmlunit.sourceforge.io/");
            System.out.println(page.getTitleText());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
