package BombParty.Bot;

public class BombPartyBotException extends Exception {
    public BombPartyBotException(String message) {
        super("BombPartyBot Exception: " + message);
    }
}
