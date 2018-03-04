package cdio.controller;

import cdio.controller.interfaces.IController;

public final class Controller implements IController {

    private static IController instance;

    static {
        try {
            instance = new Controller();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating Singleton Controller instance!");
        }
    }

    private Controller() {

    }

    public static synchronized IController getInstance() {
        return instance;
    }

}