package BombParty.WordServer;

public class NoMatchingWordException extends WordServerException {
    public NoMatchingWordException() {
        super("No matching word was found in the server");
    }
}
