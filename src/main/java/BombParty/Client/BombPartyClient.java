package BombParty.Client;

public interface  BombPartyClient {
    void setNickname(String nickname);
    String getNickname();

    BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException;
}

