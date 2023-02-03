package BombParty;

import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SQLiteExample {
    public static void main(String[] args) {

    }

    public static void dictionary() {
        Connection connection = null;
        try {
            // Create a connection to the database
            SQLiteConfig config = new SQLiteConfig();
            config.setJournalMode(SQLiteConfig.JournalMode.OFF);
            config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
            config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
            config.setTempStore(SQLiteConfig.TempStore.MEMORY);
            connection = config.createConnection("jdbc:sqlite:words.db");

            // Create a table to store the words
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                                        CREATE TABLE IF NOT EXISTS words(
                                            word TEXT UNIQUE NOT NULL,
                                            uniqueChars INT NOT NULL,
                                            used BOOLEAN NOT NULL)
                                        """);

            PreparedStatement pstmt = connection.prepareStatement("""
                        INSERT INTO words (word, uniqueChars, used)
                        VALUES (?, ?, FALSE)
                        """);

            File words = new File("words_alpha.txt");
            Scanner reader = new Scanner(words);
            int count = 0;
            while(reader.hasNextLine()) {
                String word = reader.nextLine();
                try {
                    pstmt.clearParameters();
                    pstmt.setString(1, word.trim().toUpperCase());
                    pstmt.setInt(2, (int) word.chars().distinct().count());
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