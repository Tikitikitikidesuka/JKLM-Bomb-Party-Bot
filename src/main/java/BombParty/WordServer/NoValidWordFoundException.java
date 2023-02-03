package BombParty.WordServer;

public class NoValidWordFoundException extends WordServerException {
    public NoValidWordFoundException(String syllable) {
        super("No word was found which contains \"" + syllable + "\"");
    }
}
