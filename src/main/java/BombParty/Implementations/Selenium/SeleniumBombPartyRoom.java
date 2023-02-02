package BombParty.Implementations.Selenium;

import BombParty.BombPartyRoom;
import BombParty.InvalidWordPlayedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class SeleniumBombPartyRoom implements BombPartyRoom {
    private String id = null;
    private WebDriver webDriver = null;
    private JavascriptExecutor js = null;

    public SeleniumBombPartyRoom(WebDriver webDriver, String id) {
        this.id = id;
        this.webDriver = webDriver;
        this.js = (JavascriptExecutor) this.webDriver;
    }
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void join() {
        WebDriverWait wait = new WebDriverWait(this.webDriver, 2);
        this.js.executeScript("socket.emit(\"joinRound\")");
    }

    @Override
    public boolean waitTurn(long timeoutSeconds) {
        while(!attemptTurnWait(timeoutSeconds));
        return true;
    }

    @Override
    public boolean waitTurn(long timeoutSeconds, long attempts) {
        // Check iframe's javascript variable milestone.currentPlayerPeerId
        long attempt = 0;
        boolean turn = false;

        while(!turn && attempt++ < attempts) {
            turn = attemptTurnWait(timeoutSeconds);
        }

        return turn;
    }

    @Override
    public String getSyllable() {
        String syllable = (String) this.js.executeScript("return milestone.syllable");
        return syllable;
    }

    @Override
    public void typeWord(String word) {
        this.js.executeScript("socket.emit(\"setWord\", \"" +  word + "\", false);");
    }

    @Override
    public void playWord(String word) throws InvalidWordPlayedException {
        // Run iterations with false to simulate typing (false shows the input but does not submit it)
        //js.executeScript("socket.emit(\"setWord\", \"" +  word + "\", false);");
        this.js.executeScript("socket.emit(\"setWord\", \"" +  word + "\", true);");
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
