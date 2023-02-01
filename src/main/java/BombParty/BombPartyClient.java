package BombParty;

public interface  BombPartyClient {
    public void setNickname(String nickname) throws InvalidNicknameException, InARoomException;
    public String getNickname();

    public BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException;
}

