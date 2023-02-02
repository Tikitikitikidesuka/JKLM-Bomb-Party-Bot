package BombParty;

public interface BombPartyRoom {
    public String getId();

    public void join();
    public boolean waitTurn(long timeoutSeconds);
    public boolean waitTurn(long timeoutSeconds, long attempts);
    public String getSyllable();
    public void typeWord(String word);
    public void playWord(String word) throws InvalidWordPlayedException;

    public void exit();
}