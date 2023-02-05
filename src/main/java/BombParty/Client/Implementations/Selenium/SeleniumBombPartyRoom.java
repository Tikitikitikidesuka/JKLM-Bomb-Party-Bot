package BombParty.Client.Implementations.Selenium;

import BombParty.Client.BombPartyRoom;
import BombParty.Client.BombPartyTurnData;
import BombParty.Client.InvalidWordPlayedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

class SeleniumBombPartyRoom implements BombPartyRoom {
    private String id = null;
    private WebDriver webDriver = null;
    private JavascriptExecutor js = null;
    private BombPartyTurnData lastTurnData = null;
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
    public BombPartyTurnData getLastTurnData() {
        return this.lastTurnData;
    }

    @Override
    public void joinRound() {
        WebDriverWait wait = new WebDriverWait(this.webDriver, 2);
        this.js.executeScript("socket.emit(\"joinRound\")");
    }

    @Override
    public BombPartyTurnData waitTurn() {
        if (!this.waitTurnInitialized) {
            this.initializeWaitTurnJs();
            this.waitTurnInitialized = true;
        }

        // Wait for the client's turn and detect the end of the round
        if (!this.isTurnAlready() && !this.waitTurnOrGameEnd())
            return null;

        // This code will execute when it is the bot's turn
        BombPartyTurnData turnData = new SeleniumBombPartyTurnData(getSyllableOnTurn(), getMissingLettersOnTurn());
        lastTurnData = turnData;

        return turnData;
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

    private void initializeWaitTurnJs() {
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
    }

    private boolean isTurnAlready() {
        return (boolean) this.js.executeScript("return milestone.currentPlayerId === selfPeerId;");
    }

    private boolean waitTurnOrGameEnd() {
        return (boolean) this.js.executeAsyncScript("""
                function selfTurnInjectionCallback(event) {
                    window.removeEventListener("selfTurn", selfTurnInjectionCallback);
                    callback(event.detail.valid);
                }
                                
                var callback = arguments[arguments.length - 1];                
                window.addEventListener("selfTurn", selfTurnInjectionCallback);
                """) ;
    }

    private String getSyllableOnTurn() {
        return (String) this.js.executeScript("return milestone.syllable");
    }

    private Collection<Character> getMissingLettersOnTurn() {
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

        Collection<Character> missingLetters = new LinkedList<>();

        for (Character c : alphabet.toCharArray()) {
            if (!playedLetters.contains(String.valueOf(c)))
                missingLetters.add(c);
        }

        return missingLetters;
    }
}