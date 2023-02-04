package BombParty.Client;

import java.util.Collection;

public interface BombPartyRoom {
    String getId();

    void joinRound();
    boolean waitTurn(Collection<Character> missingLetters);
    String getSyllable();
    void typeWord(String word);
    void playWord(String word) throws InvalidWordPlayedException;

    void exit();
}