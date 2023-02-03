package BombParty.Client;

public class BombPartyRoomException extends BombPartyClientException {
    public BombPartyRoomException(BombPartyRoom room, String error) {
        super("In room \"" + room.getId() + "\": " + error);
    }
}
