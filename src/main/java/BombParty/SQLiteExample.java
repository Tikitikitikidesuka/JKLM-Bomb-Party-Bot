package BombParty;

import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.Implementations.SQLite.SQLiteWordServer;
import BombParty.WordServer.WordAlreadyInDatabaseException;
import BombParty.WordServer.WordServer;
import org.sqlite.SQLiteConfig;

import java.nio.file.Paths;
import java.sql.*;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class SQLiteExample {
    public static void main(String[] args) throws SQLException {
        //dictionary();
        WordServer wordServer = new SQLiteWordServer();
        wordServer.connect(Paths.get("words.db"));

        try {
            wordServer.insertWord("A");
            //throw new WordAlreadyInDatabaseException("HELLO");
        } catch (WordAlreadyInDatabaseException exception) {
            System.out.println(exception.getMessage());
        } catch (ConnectionException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println(wordServer.getWordContaining("as", "xdf"));
        wordServer.disconnect();
    }

    public static void dictionary() {
        Connection connection = null;
        try {
            // Create a connection to the database
            SQLiteConfig config = new SQLiteConfig();
            config.setPageSize(4096);
            config.setCacheSize(4096);
            config.setJournalMode(SQLiteConfig.JournalMode.OFF);
            config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
            config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
            config.setTempStore(SQLiteConfig.TempStore.MEMORY);
            connection = config.createConnection("jdbc:sqlite:words.db");

            // Create a table to store the words
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                                        CREATE TABLE IF NOT EXISTS words(
                                            id INTEGER PRIMARY KEY,
                                            word TEXT UNIQUE NOT NULL)
                                        """);
                                        /*
                                        ,
                                            uniqueChars INT NOT NULL,
                                            used BOOLEAN NOT NULL)
                                         */

            PreparedStatement pstmt = connection.prepareStatement("""
                        INSERT INTO words (word)
                        VALUES (?)
                        """);

            File words = new File("words_alpha.txt");
            Scanner reader = new Scanner(words);
            int count = 0;
            while(reader.hasNextLine()) {
                String word = reader.nextLine();
                try {
                    pstmt.clearParameters();
                    pstmt.setString(1, word.trim().toUpperCase());
                    //pstmt.setInt(2, (int) word.chars().distinct().count());
                    pstmt.addBatch();
                    count++;
                    if(count%1000 == 0)
                        pstmt.executeBatch();
                } catch (SQLException e) {
                    //System.out.println(e);
                }
            }
            try {
                pstmt.executeBatch();
            } catch (SQLException e) {
                //System.out.println(e);
            }


/*
            System.out.println("Done inserting");

            String syllable = "OG";

            ResultSet validWords = statement.executeQuery(String.format("""
                SELECT word, uniqueChars
                FROM words
                WHERE word LIKE '%%%s%%'
                ORDER BY uniqueChars DESC
                """, syllable));

            while(validWords.next())
                System.out.println(validWords.getString("word") + ", " + validWords.getString("uniqueChars"));
*/

            /*ResultSet result = statement.executeQuery("SELECT * FROM words");
            while(result.next())
                System.out.println(result.getString("word"));*/

            /*// Load the words into the table
            String[] words = {"apple", "banana", "cherry", "date", "elderberry"};
            for (String word : words) {
                statement.executeUpdate("INSERT INTO words (word) VALUES ('" +word  + "')");
            }
            System.out.println("Words have been loaded into the database");*/

            /*ResultSet result = statement.executeQuery("SELECT * FROM words");
            while(result.next())
                System.out.println("oleolelos caracole " + result.getString("word"));*/

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }
}