package cdio.main;

import cdio.controller.MainController;
import cdio.controller.interfaces.IMainController;

public final class Main {

    private static final IMainController mainController = MainController.getInstance();

    public static void main(String[] args) {
        try {
            mainController.start();
        } catch (IMainController.MainControllerException e) {
            e.printStackTrace();
        }
    }

}