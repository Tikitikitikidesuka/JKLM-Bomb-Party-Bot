package BombParty.Client;

public interface  BombPartyClient {
    void setNickname(String nickname) throws InvalidNicknameException, InARoomException;
    String getNickname();

    BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException;
}

