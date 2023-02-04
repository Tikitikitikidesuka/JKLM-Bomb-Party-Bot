package BombParty.Client.Implementations.Selenium;

import BombParty.Client.BombPartyRoom;
import BombParty.Client.InvalidWordPlayedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

class SeleniumBombPartyRoom implements BombPartyRoom {
    private String id = null;
    private WebDriver webDriver = null;
    private JavascriptExecutor js = null;
    private boolean waitTurnInitialized = false;

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
    public void joinRound() {
        WebDriverWait wait = new WebDriverWait(this.webDriver, 2);
        this.js.executeScript("socket.emit(\"joinRound\")");
    }

    @Override
    public boolean waitTurn() {
        if (!this.waitTurnInitialized) {
            this.js.executeScript("""
               round_render = (function() {
                    var cached_function = round_render;
                                
                    return function() {
                        if (milestone.currentPlayerPeerId === selfPeerId)
                            window.dispatchEvent(new CustomEvent("selfTurn", {detail: {valid: true}}));
                                
                        cached_function.apply(this, arguments);
                    };
                })();
                                
                round_exit = (function() {
                    var cached_function = round_exit;
                                
                    return function() {
                        window.dispatchEvent(new CustomEvent("selfTurn", {detail: {valid: false}}));
                                
                        cached_function.apply(this, arguments);
                    };
                })(); 
                """);
            this.waitTurnInitialized = true;
        }

        // Check if it is already the bot's turn
        if ((boolean) this.js.executeScript("return milestone.currentPlayerId === selfPeerId;"))
            return true;

        // Wait if it is not
        return (boolean) this.js.executeAsyncScript("""
                function selfTurnInjectionCallback(event) {
                    window.removeEventListener("selfTurn", selfTurnInjectionCallback);
                    callback(event.detail.valid);
                }
                                
                var callback = arguments[arguments.length - 1];                
                window.addEventListener("selfTurn", selfTurnInjectionCallback);
                """);
    }

    @Override
    public String getSyllable() {
        return (String) this.js.executeScript("return milestone.syllable");
    }

    @Override
    public String getLetters() {
        return null;
    }

    @Override
    public void typeWord(String word) {
        this.js.executeScript("socket.emit(\"setWord\", \"" +  word + "\", false);");
    }

    @Override
    public void playWord(String word) throws InvalidWordPlayedException {
        boolean validWord = (boolean) this.js.executeAsyncScript(String.format("""
                    function incorrectWordInjectionCallback() {
                        disconnectWordInjectionSocket();
                        callback(false);
                    }
                    
                    function correctWordInjectionCallback() {
                        disconnectWordInjectionSocket();
                        callback(true);
                    }
                    
                    function disconnectWordInjectionSocket() {
                        socket.off("failWord", incorrectWordInjectionCallback);
                        socket.off("correctWord", correctWordInjectionCallback);
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
        this.js.executeScript("socket.emit(\"leaveRound\");");
    }
}