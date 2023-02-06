package BombParty.Client;

/**
 * The BombPartyClient interface provides methods to connect to a BombParty game.
 */
public interface  BombPartyClient {
    /**
     * Sets the Client's nickname.
     *
     * @param nickname the nickname to use in the room
     */
    void setNickname(String nickname);

    /**
     * Gets the Client's nickname.
     *
     * @return the Client's nickname
     */
    String getNickname();

    /**
     * Joins a room with a code.
     *
     * @param roomCode the code of the room to join
     * @return the {@link BombPartyRoom} that the Client has joined
     * @throws InvalidRoomCodeException if the room code is invalid
     * @throws RoomNotFoundException if the room isn't found
     */
    BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException;
}

