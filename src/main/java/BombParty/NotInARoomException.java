package BombParty;

public class NotInARoomException extends BombPartyClientException {
    public NotInARoomException() {
        super("The BombPartyClient is not in a room");
    }
}
