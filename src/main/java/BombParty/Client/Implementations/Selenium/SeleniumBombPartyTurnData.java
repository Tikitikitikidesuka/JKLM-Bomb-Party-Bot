package BombParty.Client.Implementations.Selenium;

import BombParty.Client.BombPartyTurnData;

import java.util.ArrayList;
import java.util.Collection;

public class SeleniumBombPartyTurnData implements BombPartyTurnData {
    private final String syllable;
    private final Collection<Character> missingLetters;

    public SeleniumBombPartyTurnData(String syllable, Collection<Character> missingLetters) {
        this.syllable = syllable;
        this.missingLetters = missingLetters;
    }

    @Override
    public String getSyllable() {
        return this.syllable;
    }

    @Override
    public Collection<String> getUsedWords() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Character> getMissingLetters() {
        return this.missingLetters;
    }
}
