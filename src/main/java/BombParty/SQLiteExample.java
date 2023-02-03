package BombParty;

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SQLiteExample {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Create a connection to the database
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:words.db");

            // Create a table to store the words
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                                        CREATE TABLE IF NOT EXISTS words(
                                            word TEXT UNIQUE NOT NULL,
                                            used BOOLEAN NOT NULL)
                                        """);

            File words = new File("mit_words.txt");
            Scanner reader = new Scanner(words);
            while(reader.hasNextLine()) {
                String word = reader.nextLine();
                try {
                    statement.executeUpdate("INSERT INTO words (word, used) VALUES ('" + word.trim().toUpperCase() + "', FALSE)");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }

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