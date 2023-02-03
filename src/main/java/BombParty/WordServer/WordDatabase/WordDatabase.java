package BombParty.WordServer.WordDatabase;

import java.util.Collection;

public interface WordDatabase {
    void insertWord(String word) throws WordAlreadyInDatabaseException;
    void deleteWord(String word) throws WordNotInDatabaseException;

    String getWordContaining(String syllable) throws NoMatchingWordInDatabaseException;
    String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordInDatabaseException;
}
