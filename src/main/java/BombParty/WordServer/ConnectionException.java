package BombParty.WordServer;

public class ConnectionException extends WordServerException {
    public ConnectionException() {
        super("Connection to the word server failed");
    }
}
