package BombParty.WordServer;

public class WordAlreadyInDatabaseException extends WordServerException {
    private final String word;

    public WordAlreadyInDatabaseException(String word) {
        super("The word \"" + word + "\" is already in the database");
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }
}
