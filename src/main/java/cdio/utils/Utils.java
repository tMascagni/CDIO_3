package cdio.utils;

public final class Utils {

    private static Utils instance;

    private Utils() {

    }

    static {
        try {
            instance = new Utils();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton instance Utils!");
        }
    }

    public static synchronized Utils getInstance() {
        return instance;
    }

    public static double distanceFromWidth(int qrCodeWidth) {
        return Math.pow(5976.4 * qrCodeWidth, -0.923);
    }

}