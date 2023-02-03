package BombParty.Implementations.Selenium;

import BombParty.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SeleniumBombPartyClient implements BombPartyClient {
    private WebDriver webDriver = null;
    private JavascriptExecutor js = null;
    private String nickname = "BombPartyBot";
    private SeleniumBombPartyRoom room = null;

    public SeleniumBombPartyClient(String driver, String driverPath) {
        System.setProperty(driver, driverPath);
    }

    @Override
    public void setNickname(String nickname) throws InvalidNicknameException, InARoomException {
        this.nickname = nickname;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    @Override
    public BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException {
        if (this.webDriver != null)
            this.webDriver.quit();

        this.webDriver = new ChromeDriver();
        this.webDriver.manage().window().maximize();
        this.webDriver.manage().deleteAllCookies();
        this.webDriver.manage().timeouts().pageLoadTimeout(Constants.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        this.webDriver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
        System.out.println(Constants.ASYNC_SCRIPT_TIMEOUT);
        this.webDriver.manage().timeouts().setScriptTimeout(Constants.ASYNC_SCRIPT_TIMEOUT, TimeUnit.SECONDS);

        if (!validateRoomCode(roomCode)) {
            this.webDriver.quit();
            throw new InvalidRoomCodeException(roomCode);
        }

        this.webDriver.get("https://jklm.fun/" + roomCode.toUpperCase());
        this.js = (JavascriptExecutor) this.webDriver;

        WebDriverWait loadWait = new WebDriverWait(this.webDriver, Constants.EXPLICIT_WAIT_TIMEOUT);

        // Wait until page loads
        WebElement nicknameField = this.webDriver.findElement(By.className("nickname"));
        loadWait.until(ExpectedConditions.elementToBeClickable(nicknameField));

        // Assign the nickname
        this.js.executeScript(String.format("""
                nicknameField.value = "%s";
                const joinEvent = new Event('submit');
                setNicknameForm_onSubmit(joinEvent);
                """, this.nickname));

        // Check that the game exists
        try{
            if (loadWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("disconnected"))) != null) {
                this.webDriver.quit();
                throw new RoomNotFoundException(roomCode);
            }
        } catch (TimeoutException ignored){}

        WebElement gameFrame = this.webDriver.findElement(By.className("game")).findElement(By.tagName("iframe"));
        loadWait.until(ExpectedConditions.elementToBeClickable(gameFrame));
        this.webDriver.switchTo().frame(gameFrame);

        this.room = new SeleniumBombPartyRoom(webDriver, roomCode);
        return this.room;
    }

    static private boolean validateRoomCode(String roomCode) {
        final Pattern validRoomCodePattern = Pattern.compile("^[a-zA-Z]{4}$");
        return validRoomCodePattern.matcher(roomCode).find();
    }
}