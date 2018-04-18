package cdio.handler.interfaces;

import java.awt.event.KeyListener;

public interface IKeyHandler extends KeyListener {

    void update();

    class KeyHandlerException extends Exception {

        public KeyHandlerException(String msg) {
            super(msg);
        }

    }

}