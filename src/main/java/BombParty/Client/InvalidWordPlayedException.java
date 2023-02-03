package BombParty.Client;

public class InvalidWordPlayedException extends BombPartyRoomException {
    public InvalidWordPlayedException(BombPartyRoom room, String word) {
        super(room, "Invalid word \"" + word + "\" played for syllable \"" + room.getSyllable() + "\"");
    }
}
