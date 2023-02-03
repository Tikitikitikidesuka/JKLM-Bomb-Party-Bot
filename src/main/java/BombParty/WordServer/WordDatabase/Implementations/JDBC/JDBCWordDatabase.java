package BombParty.WordServer.WordDatabase.Implementations.JDBC;

import BombParty.WordServer.WordDatabase.NoMatchingWordInDatabaseException;
import BombParty.WordServer.WordDatabase.WordAlreadyInDatabaseException;
import BombParty.WordServer.WordDatabase.WordDatabase;
import BombParty.WordServer.WordDatabase.WordNotInDatabaseException;

import java.util.Collection;

public class JDBCWordDatabase implements WordDatabase {
    @Override
    public void insertWord(String word) throws WordAlreadyInDatabaseException {

    }

    @Override
    public void deleteWord(String word) throws WordNotInDatabaseException {

    }

    @Override
    public String getWordContaining(String syllable) throws NoMatchingWordInDatabaseException {
        return null;
    }

    @Override
    public String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordInDatabaseException {
        return null;
    }
}
