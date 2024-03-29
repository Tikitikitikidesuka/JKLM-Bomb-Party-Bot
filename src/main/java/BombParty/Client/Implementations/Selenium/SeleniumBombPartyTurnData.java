package BombParty.Client.Implementations.Selenium;

import BombParty.Client.BombPartyTurnData;

import java.util.Collection;

public class SeleniumBombPartyTurnData implements BombPartyTurnData {
    private final String syllable;
    private final Collection<String> usedWords;
    private final Collection<Character> missingLetters;

    public SeleniumBombPartyTurnData(String syllable,
                                     Collection<String> usedWords,
                                     Collection<Character> missingLetters) {
        this.syllable = syllable;
        this.usedWords = usedWords;
        this.missingLetters = missingLetters;
    }

    @Override
    public String getSyllable() {
        return this.syllable;
    }

    @Override
    public Collection<String> getUsedWords() {
        return this.usedWords;
    }

    @Override
    public Collection<Character> getMissingLetters() {
        return this.missingLetters;
    }
}
