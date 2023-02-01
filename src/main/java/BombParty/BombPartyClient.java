package BombParty;

public interface  BombPartyClient {
    public void setNickname(String nickname) throws InvalidNicknameException, InARoomException;

    public BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException;
    public void exitRoom() throws NotInARoomException;
}

