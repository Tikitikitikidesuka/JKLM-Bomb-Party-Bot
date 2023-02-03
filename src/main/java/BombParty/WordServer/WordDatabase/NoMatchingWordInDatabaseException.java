package BombParty.WordServer.WordDatabase;

import BombParty.WordServer.WordServerException;

public class NoMatchingWordInDatabaseException extends WordDatabaseException {
    public NoMatchingWordInDatabaseException() {
        super("No matching word was found in the database");
    }
}
