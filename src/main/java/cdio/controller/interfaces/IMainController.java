package cdio.controller.interfaces;

public interface IMainController {

    void start() throws MainControllerException;

    class MainControllerException extends Exception {

        public MainControllerException(String msg) {
            super(msg);
        }

    }

}