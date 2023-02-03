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

    public boolean oldWaitTurn() {
        return waitTurn(Constants.DEFAULT_TURN_WAIT_TIMEOUT);
    }

    @Override
    public boolean waitTurn() {
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
                                
                round_render = (function() {
                    var cached_function = round_render;
                                
                    return function() {
                        if (milestone.currentPlayerPeerId === selfPeerId)
                            window.dispatchEvent(new CustomEvent("selfTurn", {detail: {valid: true}}));
                                
                        cached_function.apply(this, arguments); // use .apply() to call it
                    };
                })();
                """);
        /*
        function callback(event) {
          window.alert(event.detail);
        }

        function selfTurnInjectionCallback(event) {
          window.removeEventListener("selfTurn", selfTurnInjectionCallback);
          callback(event.detail);
        }

        window.addEventListener("selfTurn", selfTurnInjectionCallback);

        round_exit = (function() {
          var cached_function = round_exit;

          return function() {
            console.log("Emitting...");
            window.dispatchEvent(new CustomEvent("selfTurn", { "detail" : false }));

            cached_function.apply(this, arguments); // use .apply() to call it
          };
        })();
         */

        /*
        function callback(event) {
  window.alert(event.detail);
}

function selfTurnInjectionCallback(event) {
  window.removeEventListener("selfTurn", selfTurnInjectionCallback);
  callback(event.detail);
}

window.addEventListener("selfTurn", selfTurnInjectionCallback);

var original_round_exit = round_exit;

function round_exit() {
  //window.dispatchEvent(new CustomEvent("selfTurn", { "detail" : false }));
  console.log("Emitting...");
  original_round_exit();
}



round_exit = (function() {
  var cached_function = round_exit;

  return function() {
    console.log("Emitting...");

    cached_function.apply(this, arguments); // use .apply() to call it
  };
})();
         */
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

    private boolean attemptTurnWait(long timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(this.webDriver, timeoutSeconds);

        boolean turn = false;

        try {
            turn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("selfTurn"))) != null;
        } catch (TimeoutException ignored) {}

        return turn;
    }
}