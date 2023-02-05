package BombParty.Bot;

import java.nio.file.Path;

public class BombPartyBotConfig {
    private final String driver;
    private final Path driverPath;
    private final Path dbPath;
    private long minTypingIntervalMs;
    private long maxTypingIntervalMs;

    public BombPartyBotConfig(String driver, Path driverPath, Path dbPath, long minTypingInterval, long maxTypingInterval) {
        this.driver = driver;
        this.driverPath = driverPath;
        this.dbPath = dbPath;
        this.minTypingIntervalMs = minTypingInterval;
        this.maxTypingIntervalMs = maxTypingInterval;
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

    public long getMinTypingIntervalMs() {
        return minTypingIntervalMs;
    }

    public void setMinTypingIntervalMs(long minTypingIntervalMs) {
        this.minTypingIntervalMs = minTypingIntervalMs;
    }

    public long getMaxTypingIntervalMs() {
        return maxTypingIntervalMs;
    }

    public void setMaxTypingIntervalMs(long maxTypingIntervalMs) {
        this.maxTypingIntervalMs = maxTypingIntervalMs;
    }
}
