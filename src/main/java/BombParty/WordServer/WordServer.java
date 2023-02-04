package BombParty.WordServer;

import java.nio.file.Path;
import java.util.Collection;

public interface WordServer {
    void connect(Path databasePath) throws ConnectionException;
    void disconnect() throws ConnectionException;
    void insertWord(String word) throws WordAlreadyInDatabaseException, ConnectionException;
    void deleteWord(String word) throws WordNotInDatabaseException, ConnectionException;
    String getWordContaining(String syllable) throws NoMatchingWordException, ConnectionException;
    String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException, ConnectionException;
}
