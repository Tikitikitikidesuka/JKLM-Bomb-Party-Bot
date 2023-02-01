package BombParty;

public class InvalidNicknameException extends BombPartyClientException {
    public InvalidNicknameException(String nickname) {
        super("The nickname \"" + nickname + "\" is not valid");
    }
}
