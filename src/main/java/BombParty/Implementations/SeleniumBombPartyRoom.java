package BombParty.Implementations;

import BombParty.BombPartyRoom;
import BombParty.InvalidWordPlayedException;

public class SeleniumBombPartyRoom implements BombPartyRoom {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void waitTurn(long timeoutSeconds) {

    }

    @Override
    public String getSyllable() {
        return null;
    }

    @Override
    public void playWord(String word) throws InvalidWordPlayedException {

    }
}
