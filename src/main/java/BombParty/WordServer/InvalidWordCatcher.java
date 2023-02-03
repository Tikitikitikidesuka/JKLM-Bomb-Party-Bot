package BombParty.WordServer;

import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvalidWordCatcher {

    private Connection connection = null;
    private int batchCount = 0;
    PreparedStatement preparedStatement;

    public InvalidWordCatcher() {

    }

    public void connect() {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setJournalMode(SQLiteConfig.JournalMode.OFF);
            config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
            config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
            config.setTempStore(SQLiteConfig.TempStore.MEMORY);
            this.connection = config.createConnection("jdbc:sqlite:words.db");
            this.preparedStatement = connection.prepareStatement("""
                    DELETE FROM words
                    WHERE word = ?
                    """);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            this.preparedStatement.executeBatch();
        } catch (SQLException e) {
            System.out.println(e);
        }

        try {
            if(connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addWord(String word) {
        try {
            this.preparedStatement.clearParameters();
            this.preparedStatement.setString(1, word.trim().toUpperCase());
            this.preparedStatement.addBatch();
            this.batchCount++;
            if(this.batchCount%1000 == 0)
                this.preparedStatement.executeBatch();
        } catch (SQLException e) {
            //System.out.println(e);
        }

    }
}
