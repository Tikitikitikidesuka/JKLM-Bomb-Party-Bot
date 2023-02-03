package BombParty;

public interface BombPartyRoom {
    String getId();

    void join();
    boolean waitTurn();
    boolean waitTurn(long timeoutSeconds);
    boolean waitTurn(long timeoutSeconds, long attempts);
    String getSyllable();
    void typeWord(String word);
    void playWord(String word) throws InvalidWordPlayedException;

    void exit();
}