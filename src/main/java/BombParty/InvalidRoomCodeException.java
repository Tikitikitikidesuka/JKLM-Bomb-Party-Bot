package BombParty;

public class InvalidRoomCodeException extends BombPartyClientException {
    public InvalidRoomCodeException(String roomCode) {
        super("The code \"" + roomCode + "\" is not valid");
    }
}
