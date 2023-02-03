package BombParty.Client;

public interface BombPartyRoom {
    String getId();

    void join();
    boolean waitTurn();
    String getSyllable();
    void typeWord(String word);
    void playWord(String word) throws InvalidWordPlayedException;

    void exit();
}