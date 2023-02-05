package BombParty.Bot.Config;

public class BombPartyBotAnimationConfig {
    protected NormalDistribution keystrokeDistribution = null;
    protected NormalDistribution thinkDistribution = null;

    public void setKeystrokeDistribution(double meanMs, double deviationMs) {
        this.keystrokeDistribution = new NormalDistribution(meanMs, deviationMs);
    }

    public void setThinkTimeDistribution(double meanMs, double deviationMs) {
        this.thinkDistribution = new NormalDistribution(meanMs, deviationMs);
    }

    public NormalDistribution getKeystrokeDistribution() {
        return this.keystrokeDistribution;
    }

    public NormalDistribution getThinkDistribution() {
        return this.thinkDistribution;
    }
}

