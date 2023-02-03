package BombParty.WordServer.WordDatabase;

public class WordDatabaseException extends RuntimeException {
    public WordDatabaseException(String error) {
        super("WordDatabaseException: " + error);
    }
}
