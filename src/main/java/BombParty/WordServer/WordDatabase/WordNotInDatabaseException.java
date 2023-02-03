package BombParty.WordServer.WordDatabase;

public class WordNotInDatabaseException extends WordDatabaseException {
    private final String word;

    public WordNotInDatabaseException(String word) {
        super("The word \"" + word + "\" is not in the database");
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }
}
