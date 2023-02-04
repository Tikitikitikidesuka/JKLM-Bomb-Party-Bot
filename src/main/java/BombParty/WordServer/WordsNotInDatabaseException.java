package BombParty.WordServer;

import java.util.Collection;

public class WordsNotInDatabaseException extends WordServerException {
    private final Collection<String> words;

    public WordsNotInDatabaseException(Collection<String> words) {
        super("Some of the words given are not in the database");
        this.words = words;
    }

    public Collection<String> getWords() {
        return this.words;
    }
}
