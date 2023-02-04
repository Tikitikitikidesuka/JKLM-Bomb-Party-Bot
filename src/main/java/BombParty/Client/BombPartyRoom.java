package BombParty.Client;

public interface BombPartyRoom {
    String getId();

    void joinRound();
    boolean waitTurn();
    String getSyllable();
    String getLetters();
    void typeWord(String word);
    void playWord(String word) throws InvalidWordPlayedException;

    void exit();
}