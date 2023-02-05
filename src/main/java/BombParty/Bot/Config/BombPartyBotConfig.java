package BombParty.Bot.Config;

import java.nio.file.Path;

public class BombPartyBotConfig {
    private final String driver;
    private final Path driverPath;
    private final Path dbPath;
    private BombPartyBotAnimationConfig animationConfig;

    public BombPartyBotConfig(String driver, Path driverPath, Path dbPath) {
        this(driver, driverPath, dbPath, null);
    }

    public BombPartyBotConfig(String driver, Path driverPath, Path dbPath,
                              BombPartyBotAnimationConfig animationConfig) {
        this.driver = driver;
        this.driverPath = driverPath;
        this.dbPath = dbPath;
        this.animationConfig = animationConfig;
    }

    public String getDriver() {
        return driver;
    }

    public Path getDriverPath() {
        return driverPath;
    }

    public Path getDbPath() {
        return dbPath;
    }

    public BombPartyBotAnimationConfig getAnimationConfig() {
        return this.animationConfig;
    }
}
