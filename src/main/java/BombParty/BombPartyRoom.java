package BombParty;

public interface BombPartyRoom {
    public String getId();

    public void waitTurn(long timeoutSeconds);
    public String getSyllable();
    public void playWord(String word) throws InvalidWordPlayedException;
}

