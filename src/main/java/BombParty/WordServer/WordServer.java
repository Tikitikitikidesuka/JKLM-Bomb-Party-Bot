package BombParty.WordServer;

import java.util.Collection;

/**
 * The WordServer interface provides methods for connecting to, disconnecting from, and interacting with a word server.
 */
public interface WordServer {
    /**
     * Connects to the word server.
     *
     * @throws ConnectionException if there is an issue connecting to the server
     */
    void connect() throws ConnectionException;

    /**
     * Disconnects from the word server.
     *
     * @throws ConnectionException if there is an issue disconnecting from the server
     */
    void disconnect() throws ConnectionException;

    /**
     * Resets all the words marked as used.
     *
     * @throws ConnectionException if there is an issue communicating with the server
     */
    void clearUsed() throws ConnectionException;

    /**
     * Marks a given word as used.
     *
     * All words marked as used will be reset at the end of the session.
     *
     * @param word the word to be marked as used
     * @throws ConnectionException if there is an issue communicating with the server
     */
    void markUsed(String word) throws ConnectionException;

    /**
     * Inserts a word into the word server.
     * If the word is already in the server, it will not be reinserted.
     *
     * @param word the word to be inserted
     * @throws ConnectionException if there is an issue communicating with the server
     */
    void insertWord(String word) throws ConnectionException;

    /**
     * Deletes a word from the word server if it is in.
     * No error will be raised if the word is not in the server.
     *
     * @param word the word to be deleted
     * @throws ConnectionException if there is an issue communicating with the server
     */
    void deleteWord(String word) throws ConnectionException;

    /**
     * Inserts a collection of words into the word server.
     * If any of the words are already in the server, they will not be reinserted.
     *
     * @param words the list of words to be inserted
     * @throws ConnectionException if there is an issue communicating with the server
     */
    void insertWords(Collection<String> words) throws ConnectionException;

    /**
     * Deletes a collection of words from the word server.
     * No error will be raised if any of the words are not in the server.
     *
     * @param words the list of words to be deleted
     * @throws ConnectionException if there is an issue communicating with the server
     */
    void deleteWords(Collection<String> words) throws ConnectionException;

    /**
     * Gets a word from the word server that contains a specified syllable.
     *
     * @param syllable the syllable to search for
     * @return a word containing the syllable
     * @throws NoMatchingWordException if no word containing the syllable is found
     * @throws ConnectionException if there is an issue communicating with the server
     */
    String getWordContaining(String syllable) throws NoMatchingWordException, ConnectionException;

    /**
     * Gets a word from the word server that contains a specified syllable and
     * as many of the specified letters as possible.
     *
     * @param syllable the syllable to search for
     * @param letters the letters to include in the search
     * @return a word containing the syllable and as many of the letters as possible
     * @throws NoMatchingWordException if no word containing the syllable is found
     * @throws ConnectionException if there is an issue communicating with the server
     */
    String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException, ConnectionException;
}
