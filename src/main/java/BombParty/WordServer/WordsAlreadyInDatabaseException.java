package BombParty.WordServer;

import java.util.Collection;

public class WordsAlreadyInDatabaseException extends WordServerException {
    private final Collection<String> words;

    public WordsAlreadyInDatabaseException(Collection<String> words) {
        super("Some of the words given are already in the database");
        this.words = words;
    }

    public Collection<String> getWords() {
        return this.words;
    }
}
