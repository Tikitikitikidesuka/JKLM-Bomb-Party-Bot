package BombParty.WordServer;

public class WordServerException extends Exception {
        public WordServerException(String error) {
            super("WordServerException: " + error);
        }
}
