package BombParty.WordServer;

import java.util.Collection;
import java.util.List;

public interface WordServer {
    void connect() throws ConnectionException;
    void disconnect() throws ConnectionException;
    void clearUsed() throws ConnectionException;
    void insertWord(String word) throws ConnectionException;
    void deleteWord(String word) throws ConnectionException;
    void insertWords(List<String> words) throws ConnectionException;
    void deleteWords(List<String> words) throws ConnectionException;
    String getWordContaining(String syllable) throws NoMatchingWordException, ConnectionException;
    String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException, ConnectionException;
}
