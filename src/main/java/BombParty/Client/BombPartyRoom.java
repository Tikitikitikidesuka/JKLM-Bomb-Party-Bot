package BombParty.Client;

/**
 * The BombPartyRoom interface provides methods to properly play a BombParty game.
 */
public interface BombPartyRoom {
    /**
     * Gets the ID of the current room.
     *
     * @return the ID of the current room
     */
    String getId();

    /**
     * Gets the BombPartyTurnData of the last turn.
     *
     * @return the BombPartyTurnData of the last turn
     */
    BombPartyTurnData getLastTurnData();

    /**
     * Joins the round if it is possible.
     */
    void joinRound();

    /**
     * Gets the {@link BombPartyTurnData}
     * of the Client's turn, or null if the game has ended.
     *
     * @return the {@link BombPartyTurnData}
     * of the Client's turn, or null if the game has ended
     */
    BombPartyTurnData waitTurn();

    /**
     * Types the word in the for other players to see but does not submit it.
     *
     * @param word the word to type
     */
    void typeWord(String word);

    /**
     * Types the word and submits it.
     *
     * @param word the word to type
     * @throws InvalidWordPlayedException if the word played isn't valid
     */
    void playWord(String word) throws InvalidWordPlayedException;

    /**
     * Leaves the round if it is possible.
     */
    void exit();
}