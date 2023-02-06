package BombParty.Client;

public class BombPartyClientException extends Exception {
    public BombPartyClientException(String error) {
        super("BombPartyException: " + error);
    }
}
