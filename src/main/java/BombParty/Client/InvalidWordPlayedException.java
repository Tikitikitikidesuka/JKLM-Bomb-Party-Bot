package BombParty.Client;

public class InvalidWordPlayedException extends BombPartyRoomException {
    private final String word;

    public InvalidWordPlayedException(BombPartyRoom room, String word) {
        super(room, "Invalid word \"" + word + "\" played for syllable \"" + room.getSyllable() + "\"");
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
