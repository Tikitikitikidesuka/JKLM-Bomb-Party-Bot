package BombParty;

public class InARoomException extends BombPartyClientException {
    public InARoomException() {
        super("The BombPartyClient is in a room");
    }
}