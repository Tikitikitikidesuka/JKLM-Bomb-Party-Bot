package BombParty.Implementations.Selenium;

import BombParty.BombPartyRoom;
import BombParty.InvalidWordPlayedException;
import org.openqa.selenium.WebDriver;

class SeleniumBombPartyRoom implements BombPartyRoom {
    private String id = null;
    private WebDriver webDriver = null;

    public SeleniumBombPartyRoom(WebDriver webDriver, String id) {
        this.id = id;
        this.webDriver = webDriver;
    }
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void waitTurn(long timeoutSeconds) {
    }

    @Override
    public String getSyllable() {
        return null;
    }

    @Override
    public void playWord(String word) throws InvalidWordPlayedException {

    }

    @Override
    public void exit() {
        this.webDriver.quit();
    }
}
