package cdio.controller;

import cdio.controller.interfaces.IKeyController;

import java.awt.event.KeyEvent;

public final class KeyController implements IKeyController {

    private static IKeyController instance;

    static {
        try {
            instance = new KeyController();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Single KeyController instance!");
        }
    }

    private KeyController() {

    }

    public static synchronized IKeyController getInstance() {
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