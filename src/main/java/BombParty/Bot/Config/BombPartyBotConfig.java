package BombParty.Bot.Config;

import BombParty.Client.BombPartyClient;
import BombParty.WordServer.WordServer;

public class BombPartyBotConfig {
    private final BombPartyClient client;
    private final WordServer wordServer;
    private final BombPartyBotAnimationConfig animationConfig;

    public BombPartyBotConfig(
            BombPartyClient client,
            WordServer wordServer,
            BombPartyBotAnimationConfig animationConfig) {
        this.client = client;
        this.wordServer = wordServer;
        this.animationConfig = animationConfig;
    }

    public BombPartyClient getClient() {
        return this.client;
    }

    public WordServer getWordServer() {
        return this.wordServer;
    }

    public BombPartyBotAnimationConfig getAnimationConfig() {
        return this.animationConfig;
    }
}
