import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class main2 {
    public static void main(String[] args) {
        /*
        WebDriver driver = new FirefoxDriver();



        WebElement nicknameField = driver.findElement(By.className("styled nickname"));

         */

        System.setProperty("webdriver.chrome.driver", "/home/miguel/Downloads/chromedriver_linux64(1)/chromedriver");
        WebDriver driver = new ChromeDriver(); //Creating an object of FirefoxDriver
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        driver.get("https://jklm.fun/QFHK");

        WebElement nicknameField = driver.findElement(By.className("nickname"));
        nicknameField.sendKeys("elGoog64");
        WebElement playButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/form/div[2]/button"));
        playButton.click();

        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/div[2]/div[4]/div[1]/iframe")));
        WebElement joinButton = driver.findElement(By.className("joinRound"));
        joinButton.click();

        /*
        driver.findElement(By.name("q")).sendKeys("Browserstack Guide"); //name locator for text box
        WebElement searchbutton = driver.findElement(By.name("btnK"));//name locator for google search
        searchbutton.click();
         */
        while(true) {}
        //driver.quit();
    }
}
