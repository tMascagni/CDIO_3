package controlcenter.console;

public final class Console {

    private static Console instance;

    static {
        try {
            instance = new Console();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton Console instance!");
        }
    }

    private Console() {

    }

    public static synchronized Console getInstance() {
        return instance;
    }

    public void debug(Object classObj, String msg) {
        System.out.println("[DEBUG: " + classObj.getClass().getSimpleName() + "]" + msg);
    }

    public void log(Object classObj, String msg) {
        System.out.println("[" + classObj.getClass().getSimpleName() + "] " + msg);
    }

}