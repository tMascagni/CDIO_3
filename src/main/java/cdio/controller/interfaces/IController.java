package cdio.controller.interfaces;

public interface IController {

    class ControllerException extends Exception {

        public ControllerException(String msg) {
            super(msg);
        }

    }

}