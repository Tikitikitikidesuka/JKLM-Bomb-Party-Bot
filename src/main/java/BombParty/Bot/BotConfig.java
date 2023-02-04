package BombParty.Bot;

import java.nio.file.Path;

public class BotConfig {
    private final String driver;
    private final Path driverPath;
    private final Path dbPath;
    private long minTypingIntervalMs;
    private long maxTypingIntervalMs;

    public BotConfig(String driver, Path driverPath, Path dbPath, long minTypingInterval, long maxTypingIntervalDeviation) {
        this.driver = driver;
        this.driverPath = driverPath;
        this.dbPath = dbPath;
        this.minTypingIntervalMs = minTypingInterval;
        this.maxTypingIntervalMs = maxTypingIntervalDeviation;
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
