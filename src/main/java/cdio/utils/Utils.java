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

    public double distanceFromWidth(int width) {
        return Math.pow(5976.4 * width, -0.923);
    }

    public double distanceFromHeight(double qrCodeHeight) {
        return Math.pow(48722 * qrCodeHeight, -1.021);
    }

}