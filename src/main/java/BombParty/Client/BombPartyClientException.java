package BombParty.Client;

public class BombPartyClientException extends RuntimeException {
    public BombPartyClientException(String error) {
        super("BombPartyException: " + error);
    }
}
