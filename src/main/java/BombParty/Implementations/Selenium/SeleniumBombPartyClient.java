package BombParty.Implementations.Selenium;

import BombParty.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SeleniumBombPartyClient implements BombPartyClient {
    static final long PAGE_LOAD_TIMEOUT = 10;
    static final long IMPLICIT_WAIT_TIMEOUT = 5;
    static final long FIND_ROOM_TIMEOUT = 2;

    private WebDriver webDriver;
    private String nickname = "BombPartyBot";

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
        if (webDriver != null)
            webDriver.quit();

        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();
        webDriver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);

        if (!validateRoomCode(roomCode)) {
            webDriver.quit();
            throw new InvalidRoomCodeException(roomCode);
        }

        webDriver.get("https://jklm.fun/" + roomCode.toUpperCase());

        WebElement nicknameField = webDriver.findElement(By.className("nickname"));
        nicknameField.sendKeys(this.nickname);
        WebElement playButton = webDriver.findElement(By.xpath("//button[text()='OK']"));
        playButton.click();

        // Check that the game exists
        WebDriverWait wait = new WebDriverWait(webDriver, FIND_ROOM_TIMEOUT);
        if (wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("disconnected"))) != null) {
            webDriver.quit();
            throw new RoomNotFoundException(roomCode);
        }

        return new SeleniumBombPartyRoom();
    }

    @Override
    public void exitRoom() throws NotInARoomException {

    }

    static private boolean validateRoomCode(String roomCode) {
        final Pattern validRoomCodePattern = Pattern.compile("^[a-zA-Z]{4}$");
        return validRoomCodePattern.matcher(roomCode).find();
    }
}
