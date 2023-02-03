package BombParty.WordServer.Implementations.Simple;

import BombParty.WordServer.NoValidWordFoundException;
import BombParty.WordServer.WordDatabase.WordDatabase;
import BombParty.WordServer.WordServer;

import java.util.Collection;

public class SimpleWordServer implements WordServer {
    //WordDatabase wordDatabase =

    @Override
    public String getWordContaining(String syllable) throws NoValidWordFoundException {
        return null;
    }

    @Override
    public String getWordContaining(String syllable, Collection<Character> letters) throws NoValidWordFoundException {
        return null;
    }
}
