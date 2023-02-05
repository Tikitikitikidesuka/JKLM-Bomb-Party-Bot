package BombParty.Client;

public interface  BombPartyClient {
    void setNickname(String nickname) throws InvalidNicknameException;
    String getNickname();

    BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException;
}

