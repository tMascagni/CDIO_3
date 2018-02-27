package cdio.ui;

public final class Tui {

    private static Tui instance;

    static {
        try {
            instance = new Tui();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton Tui instance!");
        }
    }

    private Tui() {

    }

    public static synchronized Tui getInstance() {
        return instance;
    }

    public void debug(Object classObj, String msg) {
        System.out.println("[DEBUG: " + classObj.getClass().getSimpleName() + "]: " + msg);
    }

    public void log(Object classObj, String msg) {
        System.out.println("[" + classObj.getClass().getSimpleName() + "]: " + msg);
    }

}