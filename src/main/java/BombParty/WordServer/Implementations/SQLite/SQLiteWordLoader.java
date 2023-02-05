package BombParty.WordServer.Implementations.SQLite;

import BombParty.WordServer.ConnectionException;
import BombParty.WordServer.WordServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;

public class SQLiteWordLoader {
    /**
     * This class is intended for creating an SQLite database from
     * a plain text file containing a word per line.
     *
     * Usage:
     *     sqliteWordLoader <wordFile> <databaseFile>
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage:\n\tsqliteWordLoader <wordFile> <databaseFile>");
            System.exit(1);
        }

        String wordFilePath = args[0];
        String databasePath = args[1];

        try {
            WordServer wordServer = new SQLiteWordServer();
            wordServer.connect(Path.of(databasePath));

            try {
                File wordFile = new File(wordFilePath);
                Scanner reader = new Scanner(wordFile);

                int batchSize = 0;
                final int maxBatchSize = 4096;
                List<String> wordBatch = new LinkedList<>();

                while (reader.hasNextLine()) {
                    wordBatch.add(reader.nextLine());
                    ++batchSize;

                    if (batchSize >= maxBatchSize) {
                        wordServer.insertWords(wordBatch);
                        wordBatch.clear();
                        batchSize = 0;
                    }
                }

                wordServer.insertWords(wordBatch);

            } catch (FileNotFoundException exception) {
                System.out.println("Could not find the word file \"" + wordFilePath + "\"");
                System.out.println(exception.getMessage());
                System.exit(1);
            }

            wordServer.disconnect();
        } catch (ConnectionException exception) {
            System.out.println("Could not connect to the word database \"" + databasePath + "\"");
            System.out.println(Arrays.toString(exception.getStackTrace()));
            System.exit(1);
        }
    }
}
