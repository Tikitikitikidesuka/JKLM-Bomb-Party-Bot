package BombParty.WordServer;

import java.util.Collection;

public interface WordServer {
    String getWordContaining(String syllable) throws NoValidWordFoundException;
    String getWordContaining(String syllable, Collection<Character> letters) throws NoValidWordFoundException;
}
