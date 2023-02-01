package BombParty.Implementations.Selenium;

import BombParty.BombPartyRoom;
import BombParty.InvalidWordPlayedException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class SeleniumBombPartyRoom implements BombPartyRoom {
    private String id = null;
    private WebDriver webDriver = null;

    public SeleniumBombPartyRoom(WebDriver webDriver, String id) {
        this.id = id;
        this.webDriver = webDriver;
    }
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean waitTurn(long timeoutSeconds) {
        while(!attemptTurnWait(timeoutSeconds));
        return true;
    }

    @Override
    public boolean waitTurn(long timeoutSeconds, long attempts) {
        long attempt = 0;
        boolean turn = false;

        while(!turn && attempt++ < attempts) {
            turn = attemptTurnWait(timeoutSeconds);
        }

        return turn;
    }

    @Override
    public String getSyllable() {
        return this.webDriver.findElement(By.className("syllable")).getText();
    }

    @Override
    public void playWord(String word) throws InvalidWordPlayedException {

    }

    @Override
    public void exit() {
        // This will close the browser but if the match is ongoing, the bot will not leave it.
        // Future updates should fix that problem by pressing the leave match button on the page.
        this.webDriver.quit();
    }

    private boolean attemptTurnWait(long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(this.webDriver, timeoutSeconds);

        boolean turn = false;

        try {
            turn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("selfTurn"))) != null;
        } catch (TimeoutException ignored) {}

        return turn;
    }
}
