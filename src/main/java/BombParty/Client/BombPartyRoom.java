package BombParty.Client;

public interface BombPartyRoom {
    String getId();
    BombPartyTurnData getLastTurnData();

    void joinRound();
    BombPartyTurnData waitTurn();
    void typeWord(String word);
    void playWord(String word) throws InvalidWordPlayedException;

    void exit();
}