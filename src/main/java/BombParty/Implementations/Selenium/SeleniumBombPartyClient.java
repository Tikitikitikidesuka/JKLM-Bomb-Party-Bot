package BombParty.Implementations.Selenium;

import BombParty.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SeleniumBombPartyClient implements BombPartyClient {
    final long PAGE_LOAD_TIMEOUT = 10;
    final long IMPLICIT_WAIT_TIMEOUT = 5;
    final long FIND_ROOM_TIMEOUT = 2;
    final long TURN_TIMEOUT = 60;

    private WebDriver webDriver = null;
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
        this.webDriver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        this.webDriver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);

        if (!validateRoomCode(roomCode)) {
            this.webDriver.quit();
            throw new InvalidRoomCodeException(roomCode);
        }

        this.webDriver.get("https://jklm.fun/" + roomCode.toUpperCase());

        WebElement nicknameField = this.webDriver.findElement(By.className("nickname"));
        nicknameField.sendKeys(this.nickname);
        WebElement playButton = this.webDriver.findElement(By.xpath("//button[text()='OK']"));
        playButton.click();

        // Check that the game exists
        WebDriverWait wait = new WebDriverWait(this.webDriver, FIND_ROOM_TIMEOUT);
        try{
            if (wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("disconnected"))) != null) {
                this.webDriver.quit();
                throw new RoomNotFoundException(roomCode);
            }
        } catch (TimeoutException ignored){}

        this.webDriver.switchTo().frame(this.webDriver.findElement(By.className("game")).findElement(By.tagName("iframe")));
        JavascriptExecutor js = (JavascriptExecutor) this.webDriver;
        js.executeScript("socket.emit(\"joinRound\")");

        this.room = new SeleniumBombPartyRoom(webDriver, roomCode);
        return this.room;
    }

    static private boolean validateRoomCode(String roomCode) {
        final Pattern validRoomCodePattern = Pattern.compile("^[a-zA-Z]{4}$");
        return validRoomCodePattern.matcher(roomCode).find();
    }
}