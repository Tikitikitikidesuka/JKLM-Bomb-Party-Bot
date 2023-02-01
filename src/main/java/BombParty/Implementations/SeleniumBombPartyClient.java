package BombParty.Implementations;

import BombParty.*;

public class SeleniumBombPartyClient implements BombPartyClient {
    @Override
    public void setNickname(String nickname) throws InvalidNicknameException, InARoomException {
    }

    @Override
    public BombPartyRoom joinRoom(String roomCode) throws InvalidRoomCodeException, RoomNotFoundException {
        return null;
    }

    @Override
    public void exitRoom() throws NotInARoomException {

    }
}
