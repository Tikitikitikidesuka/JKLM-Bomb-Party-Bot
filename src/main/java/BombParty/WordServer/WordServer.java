package BombParty.WordServer;

import java.util.Collection;

public interface WordServer {
    void insertWord(String word) throws WordAlreadyInDatabaseException;
    void deleteWord(String word) throws WordNotInDatabaseException;
    String getWordContaining(String syllable) throws NoMatchingWordException;
    String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException;
}
