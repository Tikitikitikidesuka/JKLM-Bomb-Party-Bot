package BombParty.Client;

/**
 * The BombPartyRoom interface provides methods to properly play a BombParty game
 */
public interface BombPartyRoom {
    /**
     * @return the ID of the current room
     */
    String getId();

    /**
     * @return the BombPartyTurnData of the last turn
     */
    BombPartyTurnData getLastTurnData();

    /**
     * Joins the round if it is possible
     */
    void joinRound();

    /**
     * @return the {@link BombPartyTurnData}
     * of the Client's turn, or null if the game has ended
     */
    BombPartyTurnData waitTurn();

    /**
     * Types the word in the for other players to see but does not submit it
     * @param word the word to type
     */
    void typeWord(String word);

    /**
     * Types the word and submit it
     * @param word the word to type
     * @throws InvalidWordPlayedException if the word played isn't valid
     */
    void playWord(String word) throws InvalidWordPlayedException;

    /**
     * Leaves the round if it is possible
     */
    void exit();
}