package BombParty.WordServer;

public class WordServerException extends RuntimeException {
        public WordServerException(String error) {
            super("WordServerException: " + error);
        }
}
