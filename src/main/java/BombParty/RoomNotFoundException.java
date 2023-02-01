package BombParty;

public class RoomNotFoundException extends BombPartyClientException {
    public RoomNotFoundException(String roomCode) {
        super("The room \"" + roomCode + "\" could not be found");
    }
}
