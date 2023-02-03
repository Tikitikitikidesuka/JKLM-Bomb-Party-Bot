package BombParty;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DuckDBExample {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        Connection conn = DriverManager.getConnection("jdbc:duckdb:words.duckdb");

        Statement statement = conn.createStatement();
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS words(
                word TEXT UNIQUE NOT NULL,
                uniqueChars INT NOT NULL,
                used BOOLEAN NOT NULL)
            """);

        PreparedStatement insertStmt = conn.prepareStatement("""
            INSERT INTO words (word, uniqueChars, used)
            VALUES (?, ?, FALSE)
            """);

        File words = new File("mit_words.txt");
        Scanner reader = new Scanner(words);

        while (reader.hasNextLine()) {
            String word = reader.nextLine();

            insertStmt.clearParameters();
            insertStmt.setString(1, word.trim().toUpperCase());
            insertStmt.setInt(2, (int) word.chars().distinct().count());
            insertStmt.executeUpdate();
        }

        System.out.println("Done inserting");

        String syllable = "OG";

        PreparedStatement queryStmt = conn.prepareStatement("""
            SELECT word, uniqueChars
            FROM words
            WHERE word LIKE ?
            ORDER BY uniqueChars DESC
        """);

        queryStmt.setString(1, syllable);

        ResultSet validWords = queryStmt.executeQuery();

        while(validWords.next())
            System.out.println(validWords.getString("word") + ", " + validWords.getString("uniqueChars"));
    }
}
