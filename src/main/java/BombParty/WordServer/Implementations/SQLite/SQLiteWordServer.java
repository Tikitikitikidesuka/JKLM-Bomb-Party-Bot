package BombParty.WordServer.Implementations.SQLite;

import BombParty.WordServer.*;
import org.sqlite.Function;
import org.sqlite.SQLiteConfig;

import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SQLiteWordServer implements WordServer {
    private Connection connection;
    private PreparedStatement getWordBySyllableStmt,
            getWordBySyllableAndLettersStmt,
            insertWordStmt,
            deleteWordStmt,
            useWordStmt;

    @Override
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

    @Override
    public void disconnect() throws ConnectionException {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public void clearUsed() throws ConnectionException {
        try {
            Statement createStmt = connection.createStatement();
            createStmt.executeUpdate("""
            DELETE FROM used
        """);
        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public void insertWord(String word) throws WordAlreadyInDatabaseException, ConnectionException {
        try {
            this.insertWords(Collections.singletonList(word));
        } catch (WordsAlreadyInDatabaseException exception) {
            throw new WordAlreadyInDatabaseException(exception.getWords().iterator().next());
        }
    }

    @Override
    public void deleteWord(String word) throws WordNotInDatabaseException, ConnectionException {
        try {
            this.deleteWords(Collections.singletonList(word));
        } catch (WordsAlreadyInDatabaseException exception) {
            throw new WordNotInDatabaseException(exception.getWords().iterator().next());
        }
    }

    @Override
    public void insertWords(List<String> words) throws WordsAlreadyInDatabaseException, ConnectionException {
        // No WordsAlreadyInDatabaseExceptions will be thrown. Please fix.

        List<String> duplicateWords = new ArrayList<>();

        try {
            for (String word : words) {
                word = word.trim().toUpperCase();
                this.insertWordStmt.setString(1, word);
                this.insertWordStmt.addBatch();
            }

            int[] insertCounts = this.insertWordStmt.executeBatch();

            for(int i = 0; i < insertCounts.length; i++) {
                if(insertCounts[i] == Statement.EXECUTE_FAILED)
                    duplicateWords.add(words.get(i));
            }

            if(!duplicateWords.isEmpty())
                throw new WordsAlreadyInDatabaseException(duplicateWords);

        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public void deleteWords(List<String> words) throws WordsAlreadyInDatabaseException, ConnectionException {
        // No WordsNotInDatabaseExceptions will be thrown. Please fix.

        List<String> missingWords = new ArrayList<>();

        try {
            for (String word : words) {
                word = word.trim().toUpperCase();
                this.deleteWordStmt.setString(1, word);
                this.deleteWordStmt.addBatch();
            }

            int[] deleteCounts = this.deleteWordStmt.executeBatch();

            for(int i = 0; i < deleteCounts.length; i++) {
                if(deleteCounts[i] == Statement.EXECUTE_FAILED)
                    missingWords.add(words.get(i));
            }

            if(!missingWords.isEmpty())
                throw new WordsAlreadyInDatabaseException(missingWords);

        } catch (SQLException exception) {
            throw new ConnectionException();
        }
    }

    @Override
    public String getWordContaining(String syllable) throws NoMatchingWordException, ConnectionException {
        String word;

        syllable = syllable.trim().toUpperCase();

        try {
            this.getWordBySyllableStmt.setString(1, String.format("%%%s%%", syllable));
            ResultSet results = this.getWordBySyllableStmt.executeQuery();
            if (results.next()) {
                word = results.getString("word");
            } else {
                throw new NoMatchingWordException();
            }

            this.useWordStmt.setInt(1, results.getInt("id"));
            this.useWordStmt.executeUpdate();

        } catch (SQLException exception) {
            throw new ConnectionException();
        }

        return word;
    }

    @Override
    public String getWordContaining(String syllable, Collection<Character> letters) throws NoMatchingWordException, ConnectionException {
        String word;

        syllable = syllable.trim().toUpperCase();
        String lettersString = letters.stream().
                map(String::valueOf).
                collect(Collectors.joining()).
                trim().toUpperCase();

        try {
            this.getWordBySyllableAndLettersStmt.setString(1, String.format("%%%s%%", syllable));
            this.getWordBySyllableAndLettersStmt.setString(2, lettersString);
            ResultSet results = this.getWordBySyllableAndLettersStmt.executeQuery();
            if (results.next()) {
                word = results.getString("word");
            } else {
                throw new NoMatchingWordException();
            }

            this.useWordStmt.setInt(1, results.getInt("id"));
            this.useWordStmt.executeUpdate();

        } catch (SQLException exception) {
            throw new ConnectionException();
        }

        return word;
    }

    private void ensureDatabase() throws SQLException {
        Statement createStmt = connection.createStatement();
        createStmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS words(
                word TEXT UNIQUE NOT NULL)
        """);
        createStmt.executeUpdate("""
            CREATE TEMPORARY TABLE used(
                wordId INTEGER,
                FOREIGN KEY (wordId)
                REFERENCES words (id)
                ON DELETE CASCADE)
        """);
    }

    private void prepareStatements() throws SQLException {
        this.prepareGetWordBySyllableStmt();
        this.prepareGetWordBySyllableAndLettersStmt();
        this.prepareInsertWordStmt();
        this.prepareDeleteWordStmt();
        this.prepareUseWordStmt();
    }

    private void prepareGetWordBySyllableStmt() throws SQLException {
        this.getWordBySyllableStmt = this.connection.prepareStatement("""
            SELECT word, id
            FROM words
            WHERE word LIKE ?
            AND id NOT IN (SELECT wordId FROM used)
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
            SELECT word, id
            FROM words
            WHERE word LIKE ?
            AND id NOT IN (SELECT wordId FROM used)
            ORDER BY numberOfGivenLettersInWord(word, ?) DESC
            LIMIT 1
        """);
    }

    private void prepareInsertWordStmt() throws SQLException {
        this.insertWordStmt = this.connection.prepareStatement("""
            INSERT OR IGNORE INTO words (word)
            VALUES (?)
        """);
    }

    private void prepareDeleteWordStmt() throws SQLException {
        this.deleteWordStmt = this.connection.prepareStatement("""
            DELETE FROM words
            WHERE word = ?
        """);
    }

    private void prepareUseWordStmt() throws SQLException {
        this.useWordStmt = this.connection.prepareStatement("""
            INSERT OR IGNORE INTO used (wordId)
            VALUES (?)
        """);
    }
}
