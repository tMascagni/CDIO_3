package cdio.handler;

import cdio.handler.interfaces.IKeyHandler;

import java.awt.event.KeyEvent;

public final class KeyHandler implements IKeyHandler {

    private static IKeyHandler instance;

    static {
        try {
            instance = new KeyHandler();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Single KeyController instance!");
        }
    }

    private KeyHandler() {

    }

    public static synchronized IKeyHandler getInstance() {
        return instance;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}