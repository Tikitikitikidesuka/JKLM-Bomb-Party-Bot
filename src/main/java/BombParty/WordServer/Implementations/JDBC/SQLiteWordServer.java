package BombParty.WordServer.Implementations.JDBC;

import BombParty.WordServer.NoMatchingWordException;
import BombParty.WordServer.WordAlreadyInDatabaseException;
import BombParty.WordServer.WordNotInDatabaseException;
import BombParty.WordServer.WordServer;
import org.sqlite.SQLiteConfig;

import java.nio.file.Path;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Collection;

public class SQLiteWordServer implements WordServer {
    private final Connection connection;

    private final Statement getWordBySyllableStmt;
    private final Statement getWordBySyllableAndLettersStmt;

    public SQLiteWordServer(Path databasePath) {
        SQLiteConfig config = new SQLiteConfig();

        config.setPageSize(4096);
        config.setCacheSize(4096);
        config.setJournalMode(SQLiteConfig.JournalMode.OFF);
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
        config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
        config.setTempStore(SQLiteConfig.TempStore.MEMORY);

        this.connection = config.createConnection("jdbc:sqlite:" + databasePath);

        this.
    }

    @Override
    public void insertWord(String word) throws WordAlreadyInDatabaseException {

    }

    @Override
    public void deleteWord(String word) throws WordNotInDatabaseException {

    }

    @Override
    public String getWordContaining(String syllable) throws NoMatchingWordException {
        state
    }

    @Override
    public String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException {
        return null;
    }

    private void prepareStatements() {

    }

    private void prepareGetWordBySyllableStmt() {
        this.getWordBySyllableStmt = this.connection.prepareStatement("""
            SELECT word
            FROM words
            WHERE word LIKE ? AND NOT used
            ORDER BY uniqueChars DESC
            LIMIT 1
            """);
    }

    private void prepareGetWordBySyllableAndLettersStmt() {

    }
}
