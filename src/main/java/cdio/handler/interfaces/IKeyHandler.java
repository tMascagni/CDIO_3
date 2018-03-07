package cdio.handler.interfaces;

import cdio.ui.interfaces.MessageListener;

import java.awt.event.KeyListener;

public interface IKeyHandler extends KeyListener {

    void update();
    void setMessageListener(MessageListener messageListener);

    class KeyHandlerException extends Exception {

        public KeyHandlerException(String msg) {
            super(msg);
        }

    }

}