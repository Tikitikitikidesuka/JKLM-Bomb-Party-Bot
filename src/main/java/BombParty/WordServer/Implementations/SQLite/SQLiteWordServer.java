package BombParty.WordServer.Implementations.SQLite;

import BombParty.WordServer.NoMatchingWordException;
import BombParty.WordServer.WordAlreadyInDatabaseException;
import BombParty.WordServer.WordNotInDatabaseException;
import BombParty.WordServer.WordServer;
import org.sqlite.SQLiteConfig;

import java.nio.file.Path;
import java.sql.*;
import java.util.Collection;

public class SQLiteWordServer implements WordServer {
    private Connection connection;

    private PreparedStatement getWordBySyllableStmt;
    private PreparedStatement getWordBySyllableAndLettersStmt;
    private PreparedStatement insertWordStmt;
    private PreparedStatement deleteWordStmt;
    private PreparedStatement findWordStmt;
    private int insertBatchCount = 0;
    private int deleteBatchCount = 0;

    public SQLiteWordServer(Path databasePath) throws SQLException {
        SQLiteConfig config = new SQLiteConfig();

        config.setPageSize(4096);
        config.setCacheSize(4096);
        config.setJournalMode(SQLiteConfig.JournalMode.OFF);
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
        config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
        config.setTempStore(SQLiteConfig.TempStore.MEMORY);

        this.connection = config.createConnection("jdbc:sqlite:" + databasePath);


    }

    @Override
    public void insertWord(String word) throws WordAlreadyInDatabaseException {
        try {
            this.findWordStmt.clearParameters();
            this.findWordStmt.setString(1, word);
            ResultSet result = this.findWordStmt.executeQuery();

            // If the word is not in the database
            if(!result.getBoolean("found")) {
                this.insertWordStmt.clearParameters();
                this.insertWordStmt.setString(1, word.trim().toUpperCase());
                this.insertWordStmt.addBatch();
                this.insertBatchCount++;
                if(this.insertBatchCount %1000 == 0)
                    this.insertWordStmt.executeBatch();
            }
            else // The word is in the database
                throw new WordAlreadyInDatabaseException(word);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWord(String word) throws WordNotInDatabaseException {
        try {
            this.findWordStmt.clearParameters();
            this.findWordStmt.setString(1, word);
            ResultSet result = this.findWordStmt.executeQuery();

            // If the word is in the database
            if(result.getBoolean("found")) {
                this.deleteWordStmt.clearParameters();
                this.deleteWordStmt.setString(1, word.trim().toUpperCase());
                this.deleteWordStmt.addBatch();
                this.deleteBatchCount++;
                if(this.deleteBatchCount %1000 == 0)
                    this.deleteWordStmt.executeBatch();
            }
            else // The word is not in the database
                throw new WordNotInDatabaseException(word);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getWordContaining(String syllable) throws NoMatchingWordException {
        return null;
    }

    @Override
    public String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException {
        return null;
    }

    private void prepareStatements() {

    }

    private void prepareGetWordBySyllableStmt() throws SQLException {
        this.getWordBySyllableStmt = this.connection.prepareStatement("""
            SELECT word
            FROM words
            WHERE word LIKE ? AND NOT used
            ORDER BY uniqueChars DESC
            LIMIT 1
            """);
    }

    private void prepareGetWordBySyllableAndLettersStmt() throws SQLException{
        this.getWordBySyllableAndLettersStmt = this.connection.prepareStatement("""
            SELECT word
            FROM words
            WHERE word LIKE ? AND NOT used
            ORDER BY uniqueChars DESC
            LIMIT 1
            """);
    }

    private void prepareInsertWordStmt() throws SQLException {
        this.insertWordStmt = this.connection.prepareStatement("""
            INSERT INTO words (word, uniqueChars, used)
            VALUES (?, ?, FALSE)
            """);
    }

    private void prepareDeleteWordStmt() throws SQLException {
        this.deleteWordStmt = this.connection.prepareStatement("""
            DELETE FROM words
            WHERE word = ?
            """);
    }

    private void prepareFindWordStmt() throws SQLException {
        this.findWordStmt = this.connection.prepareStatement("""
            SELECT COUNT(word) > 0 AS found FROM words
            WHERE word = ?
            """);
    }
}
