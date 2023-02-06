package BombParty.Client;

import java.util.Collection;

/**
 * The BombPartyTurnData interface provides methods to get relevant data from a playedBomb Party turn
 */
public interface BombPartyTurnData {
    /**
     * Gets the syllable the turn's word must contain.
     *
     * @return the syllable the turn's word must contain
     */
    String getSyllable();

    /**
     * Gets a collection containing all the words played by the opponents since the previous turn.
     *
     * @return a collection containing all the words played by the opponents since the previous turn
     */
    Collection<String> getUsedWords();

    /**
     * Gets a collection of all the necessary characters to earn another life.
     *
     * @return a collection of all the necessary characters to earn another life
     */
    Collection<Character> getMissingLetters();
}
