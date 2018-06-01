package cdio.handler;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.handler.interfaces.IKeyHandler;

import java.awt.event.KeyEvent;

public final class KeyHandler implements IKeyHandler {

    private boolean[] keyPool = new boolean[120];

    /*
     * KEY INFORMATION:
     *
     * -- FLYING CONTROL --
     * flyForward:    W
     * flyBackward:   S
     * flyUp:         Q
     * flyDown:       E
     * flyLeft:       A
     * flyRight:      D
     *
     * -- ACTION CONTROL --
     * isAutonomous:  P (Switches between autonomous and manual control of the drone. START needs to be pressed for autonomous mode to start.)
     * start:         O (Starts the drone, i.e. makes it ready to take off, etc. DOES NOT MAKE IT FLY YET!)
     * stop:          I (Stops the drone, shuts it down. I think this might be safe to do when flying? It might just land.)
     * takeoff:       Enter (The drone takes off!)
     * land:          Spacebar (The drone lands.)
     * hover:         H (The drone hovers (predefined milliseconds for this method call))
     * rotate:        R (Rotates the drone 360 degrees, and the drone scans stuff (?))
     * circleObject:  C (Circles around the nearest object (?))
     * flip:          F (Flips like cray.)
     * dance:         G (Do the mixed theta-omega dance thingy.)
     * incSpeed:      N (Increase speed by 1.)
     * decSpeed:      M (Decrease speed by 1.)
     *
     * 19 keys.
     */

    /* Flying control keys */
    public boolean flyForward, flyBackward, flyUp, flyDown, flyLeft, flyRight;

    /* Action keys */
    public boolean isAutonomous, start, stop, takeoff, land, hover, rotate, circleObject, flip, incSpeed, decSpeed;

    private static IKeyHandler instance;

    private final IDroneCommander droneController = DroneCommander.getInstance();

    static {
        try {
            instance = new KeyHandler();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Singleton KeyHandler instance!");
        }
    }

    private KeyHandler() {

    }

    public static synchronized IKeyHandler getInstance() {
        return instance;
    }

    @Override
    public void update() {
        /* Flying Control */
        flyForward = keyPool[KeyEvent.VK_W];
        flyBackward = keyPool[KeyEvent.VK_S];
        flyUp = keyPool[KeyEvent.VK_Q];
        flyDown = keyPool[KeyEvent.VK_E];
        flyLeft = keyPool[KeyEvent.VK_A];
        flyRight = keyPool[KeyEvent.VK_D];

        /* Action Control */
        isAutonomous = keyPool[KeyEvent.VK_O];

        start = keyPool[KeyEvent.VK_I];
        stop = keyPool[KeyEvent.VK_I];

        takeoff = keyPool[KeyEvent.VK_ENTER];
        land = keyPool[KeyEvent.VK_SPACE];

        hover = keyPool[KeyEvent.VK_H];
        rotate = keyPool[KeyEvent.VK_R];
        circleObject = keyPool[KeyEvent.VK_C];
        flip = keyPool[KeyEvent.VK_F];
        incSpeed = keyPool[KeyEvent.VK_N];
        decSpeed = keyPool[KeyEvent.VK_M];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPool[e.getKeyCode()] = true;
        try {
            handleCommand(e);
        } catch (IDroneCommander.DroneCommanderException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPool[e.getKeyCode()] = false;
    }

    private void handleCommand(KeyEvent e) throws IDroneCommander.DroneCommanderException {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                droneController.addMessage("W pressed!");
                droneController.getDrone().forward();
                break;
            case KeyEvent.VK_S:
                droneController.addMessage("S pressed!");
                droneController.getDrone().backward();
                break;
            case KeyEvent.VK_Q:
                droneController.addMessage("Q pressed!");
                droneController.getDrone().up();
                break;
            case KeyEvent.VK_E:
                droneController.addMessage("E pressed!");
                droneController.getDrone().down();
                break;
            case KeyEvent.VK_A:
                droneController.addMessage("A pressed!");
                droneController.getDrone().goLeft();
                break;
            case KeyEvent.VK_D:
                droneController.addMessage("D pressed!");
                droneController.getDrone().goRight();
                break;
            case KeyEvent.VK_P:
                droneController.addMessage("P pressed!");
                break;
            case KeyEvent.VK_O:
                droneController.addMessage("O pressed!");
                droneController.startDrone();
                break;
            case KeyEvent.VK_I:
                droneController.addMessage("I pressed!");
                droneController.stopDrone();
                break;
            case KeyEvent.VK_ENTER:
                droneController.addMessage("ENTER pressed!");
                droneController.takeOffDrone();
                break;
            case KeyEvent.VK_SPACE:
                droneController.addMessage("SPACE pressed!");
                droneController.landDrone();
                break;
            case KeyEvent.VK_H:
                droneController.addMessage("H pressed!");
                droneController.hoverDrone();
                break;
            case KeyEvent.VK_R:
                droneController.addMessage("R pressed!");
                droneController.searchForQRCode();
                break;
            case KeyEvent.VK_C:
                droneController.addMessage("C pressed!");
                droneController.circleAroundObject();
                break;
            case KeyEvent.VK_F:
                droneController.addMessage("F pressed!");
                break;
            case KeyEvent.VK_G:
                droneController.addMessage("G pressed!");
                break;
            case KeyEvent.VK_N:
                droneController.addMessage("N pressed!");
                droneController.setSpeed(droneController.getSpeed() + 1);
                break;
            case KeyEvent.VK_M:
                droneController.addMessage("M pressed!");
                droneController.setSpeed(droneController.getSpeed() - 1);
                break;
            default:
                droneController.addMessage("Unknown key pressed!");
                break;
        }

    }

}