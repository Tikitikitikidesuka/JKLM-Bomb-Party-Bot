import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) {
        /*
        WebDriver driver = new FirefoxDriver();



        WebElement nicknameField = driver.findElement(By.className("styled nickname"));

         */

        System.setProperty("webdriver.chrome.driver", "/home/miguel/Downloads/chromedriver_linux64(1)/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.get("https://jklm.fun/HMRR");

        WebElement nicknameField = driver.findElement(By.className("nickname"));
        nicknameField.sendKeys("elGoog64");
        WebElement playButton = driver.findElement(By.xpath("//button[text()='OK']"));
        playButton.click();

        // Check that game exists
        if (driver.findElement(By.className("disconnected")).isDisplayed()) {
            System.out.println("Game not found");
            driver.quit();
            System.exit(1);
        }

        System.out.println("Game found");

        driver.switchTo().frame(driver.findElement(By.className("game")).findElement(By.tagName("iframe")));
        WebElement joinButton = driver.findElement(By.xpath("//button[@data-text='joinGame']"));
        joinButton.click();

        System.out.println("Game joined");


        while(true) {
            WebDriverWait wait = new WebDriverWait(driver, 60);
            if (wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("selfTurn"))) != null) {
                System.out.println("Mi turno!");
                String syllable = driver.findElement(By.className("syllable")).getText();
                System.out.println("La sílaba es: " + syllable);
                WebElement inputField = driver.findElement(By.className("selfTurn")).findElement(By.tagName("input"));
                inputField.sendKeys("XDXDXD\n");
            } else {
                System.out.println("Todavía no...");
            }
        }




        //while(true) {}
        //driver.quit();
    }
}
