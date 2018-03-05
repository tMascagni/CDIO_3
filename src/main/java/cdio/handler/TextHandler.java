package cdio.handler;

public final class TextHandler {

    private static TextHandler instance;

    public static String GUI_TITLE = "Group 3 - DroneX Control Center";

    static {
        try {
            instance = new TextHandler();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton TextHandler instance!");
        }
    }

    private TextHandler() {

    }

    public static synchronized TextHandler getInstance() {
        return instance;
    }

}