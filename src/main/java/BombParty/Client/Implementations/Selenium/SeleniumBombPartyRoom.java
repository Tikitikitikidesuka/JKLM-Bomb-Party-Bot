package BombParty.Client.Implementations.Selenium;

import BombParty.Client.BombPartyRoom;
import BombParty.Client.InvalidWordPlayedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class SeleniumBombPartyRoom implements BombPartyRoom {
    private String id = null;
    private WebDriver webDriver = null;
    private JavascriptExecutor js = null;
    private String missingLetters = null;
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
    public boolean waitTurn(Collection<Character> missingLetters) {
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
        boolean turn =  (boolean) this.js.executeAsyncScript("""
                function selfTurnInjectionCallback(event) {
                    window.removeEventListener("selfTurn", selfTurnInjectionCallback);
                    callback(event.detail.valid);
                }
                                
                var callback = arguments[arguments.length - 1];                
                window.addEventListener("selfTurn", selfTurnInjectionCallback);
                """);

        String alphabet = ((String) this.js.executeScript("" +
                "return milestone.dictionaryManifest.bonusAlphabet"))
                .trim().toUpperCase();
        String playedLetters = ((Collection<String>) this.js.executeScript("" +
                "return milestone.playerStatesByPeerId[milestone.currentPlayerPeerId].bonusLetters"))
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining())
                .trim()
                .toUpperCase();

        missingLetters.clear();
        for (Character c : alphabet.toCharArray()) {
            if (!playedLetters.contains(String.valueOf(c)))
                missingLetters.add(c);
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