package BombParty.Bot;

public class WordServerConnectionException extends BombPartyBotException {
    public WordServerConnectionException() {
        super("Connection to the word server failed");
    }
}
