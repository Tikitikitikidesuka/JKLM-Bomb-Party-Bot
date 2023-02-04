package BombParty.WordServer;

import java.nio.file.Path;
import java.util.Collection;

public interface WordServer {
    void connect(Path databasePath) throws ConnectionException;
    void disconnect() throws ConnectionException;
    void insertWord(String word) throws WordAlreadyInDatabaseException, ConnectionException;
    void deleteWord(String word) throws WordNotInDatabaseException, ConnectionException;
    void insertWords(Collection<String> words) throws WordsAlreadyInDatabaseException, ConnectionException;
    void deleteWords(Collection<String> words) throws WordsNotInDatabaseException, ConnectionException;
    String getWordContaining(String syllable) throws NoMatchingWordException, ConnectionException;
    String getWordContaining(String syllable, String letters) throws NoMatchingWordException, ConnectionException;
}
