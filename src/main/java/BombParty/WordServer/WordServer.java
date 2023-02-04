package BombParty.WordServer;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collection;

public interface WordServer {
    void connect(Path databasePath);
    void disconnect();
    void insertWord(String word) throws WordAlreadyInDatabaseException;
    void deleteWord(String word) throws WordNotInDatabaseException;
    String getWordContaining(String syllable) throws NoMatchingWordException;
    String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException;
}
