package BombParty.Implementations.Selenium;

import BombParty.BombPartyRoom;
import BombParty.InvalidWordPlayedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
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
    public boolean waitTurn() {
        return waitTurn(Constants.DEFAULT_TURN_WAIT_TIMEOUT);
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
        return (String) this.js.executeScript("return milestone.syllable");
    }

    @Override
    public void typeWord(String word) {
        this.js.executeScript("socket.emit(\"setWord\", \"" +  word + "\", false);");
    }

    @Override
    public void playWord(String word) throws InvalidWordPlayedException {
        boolean validWord = (boolean) this.js.executeAsyncScript(String.format("""
                    function incorrectWordInjectionCallback() {
                        socket.off("failWord", incorrectWordInjectionCallback);
                        callback(false);
                    }
                    
                    function correctWordInjectionCallback() {
                        socket.off("correctWord", correctWordInjectionCallback);
                        callback(true);
                    }
                    
                    var callback = arguments[arguments.length - 1];
                    socket.on("failWord", incorrectWordInjectionCallback);
                    socket.on("correctWord", correctWordInjectionCallback);
                     
                    socket.emit("setWord", "%s", true);
                """, word));

        if (!validWord)
            throw new InvalidWordPlayedException(this, word);
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