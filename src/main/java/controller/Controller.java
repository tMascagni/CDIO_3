package controller;

public final class Controller implements IController {

    private static IController instance;

    static {
        try {
            instance = new Controller();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating Singleton DroneController instance!");
        }
    }

    private Controller() {

    }

    public static synchronized IController getInstance() {
        return instance;
    }

}