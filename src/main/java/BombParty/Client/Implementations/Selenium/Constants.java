package BombParty.Client.Implementations.Selenium;

public final class Constants {
    private Constants() {
        // restrict instantiation
    }

    public static final long PAGE_LOAD_TIMEOUT = 10;
    public static final long IMPLICIT_WAIT_TIMEOUT = 5;
    public static final long EXPLICIT_WAIT_TIMEOUT = 5;
    public static final long ASYNC_SCRIPT_TIMEOUT = (long) (Math.pow(2, 16) - 1);
}
