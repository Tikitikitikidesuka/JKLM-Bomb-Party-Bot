package BombParty.WordServer.Implementations.SQLite;

import BombParty.WordServer.*;
import org.sqlite.Function;
import org.sqlite.SQLiteConfig;

import java.nio.file.Path;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SQLiteWordServer implements WordServer {
    private Connection connection;

    private PreparedStatement getWordBySyllableStmt;
    private PreparedStatement getWordBySyllableAndLettersStmt;
    private PreparedStatement insertWordStmt;
    private PreparedStatement deleteWordStmt;
    private PreparedStatement findWordStmt;
    private int insertBatchCount = 0;
    private int deleteBatchCount = 0;

    public void connect(Path databasePath) throws ConnectionException {
        try {
            SQLiteConfig config = new SQLiteConfig();

            config.setPageSize(4096);
            config.setCacheSize(4096);
            config.setJournalMode(SQLiteConfig.JournalMode.OFF);
            config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
            config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
            config.setTempStore(SQLiteConfig.TempStore.MEMORY);

            this.connection = config.createConnection("jdbc:sqlite:" + databasePath);

            this.ensureDatabase();
            this.prepareStatements();
        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    public void disconnect() throws ConnectionException {
        // Haría un método que sea insertar conjunto de palabras para poder quitar este
        // código de disconnect, que viola su responsabilidad única y hace que pueda tirar
        // WordAlreadyInDatabaseException y WordNotInDatabaseException
        try {
            this.insertWordStmt.executeBatch();
            this.deleteWordStmt.executeBatch();
        } catch (SQLException exception) {
            throw new ConnectionException();
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public void insertWord(String word) throws WordAlreadyInDatabaseException, ConnectionException {
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

        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public void deleteWord(String word) throws WordNotInDatabaseException, ConnectionException {
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

        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public String getWordContaining(String syllable) throws NoMatchingWordException, ConnectionException {
        String word;

        try {
            this.getWordBySyllableStmt.setString(1, String.format("%%%s%%", syllable));
            ResultSet results = this.getWordBySyllableStmt.executeQuery();
            if (results.next()) {
                word = results.getString("word");
            } else {
                throw new NoMatchingWordException();
            }
        } catch (SQLException exception) {
            throw new ConnectionException();
        }

        return word;
    }

    @Override
    public String getWordContaining(String syllable, String letters) throws NoMatchingWordException, ConnectionException {
        String word;

        try {
            this.getWordBySyllableAndLettersStmt.setString(1, String.format("%%%s%%", syllable));
            this.getWordBySyllableAndLettersStmt.setString(2, letters);
            ResultSet results = this.getWordBySyllableAndLettersStmt.executeQuery();
            if (results.next()) {
                word = results.getString("word");
            } else {
                throw new NoMatchingWordException();
            }
        } catch (SQLException exception) {
            throw new ConnectionException();
        }

        return word;
    }

    private void ensureDatabase() throws SQLException {
        Statement createStmt = connection.createStatement();
        createStmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS words(
                word TEXT UNIQUE NOT NULL,
                uniqueChars INT NOT NULL,
                used BOOLEAN NOT NULL)
        """);
    }

    private void prepareStatements() throws SQLException {
        this.prepareGetWordBySyllableStmt();
        this.prepareGetWordBySyllableAndLettersStmt();
        this.prepareInsertWordStmt();
        this.prepareInsertWordStmt();
        this.prepareDeleteWordStmt();
        this.prepareFindWordStmt();
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

    private void prepareGetWordBySyllableAndLettersStmt() throws SQLException {
        Function.create(this.connection, "numberOfGivenLettersInWord", new Function() {
            @Override
            protected void xFunc() {
                try {
                    final String word = value_text(0);
                    final String letters = value_text(1);

                    Set<Character> letterSet = new HashSet<>();
                    for (int i = 0; i < letters.length(); ++i)
                        letterSet.add(letters.charAt(i));

                    int matches = 0;
                    for (int i = 0; i < word.length(); ++i) {
                        Character character = word.charAt(i);
                        if (letterSet.contains(character)) {
                            letterSet.remove(character);
                            ++matches;
                        }
                    }

                    result(matches);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        this.getWordBySyllableAndLettersStmt = this.connection.prepareStatement("""
            SELECT word
            FROM words
            WHERE word LIKE ? AND NOT used
            ORDER BY numberOfGivenLettersInWord(word, ?) DESC
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
