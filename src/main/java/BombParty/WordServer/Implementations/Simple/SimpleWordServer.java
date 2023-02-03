package BombParty.WordServer.Implementations.Simple;

import BombParty.WordServer.NoMatchingWordException;
import BombParty.WordServer.WordAlreadyInDatabaseException;
import BombParty.WordServer.WordNotInDatabaseException;
import BombParty.WordServer.WordServer;

import java.util.Collection;

public class SimpleWordServer implements WordServer {
    @Override
    public void insertWord(String word) throws WordAlreadyInDatabaseException {

    }

    @Override
    public void deleteWord(String word) throws WordNotInDatabaseException {

    }

    @Override
    public String getWordContaining(String syllable) throws NoMatchingWordException {
        return null;
    }

    @Override
    public String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException {
        return null;
    }
}
