package cdio.handler;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.handler.interfaces.IKeyHandler;
import cdio.ui.interfaces.MessageListener;
import de.yadrone.base.command.FlightAnimation;

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

    private MessageListener messageListener;

    private static IKeyHandler instance;

    private final IDroneController droneController = DroneController.getInstance();

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
        } catch (IDroneController.DroneControllerException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPool[e.getKeyCode()] = false;
    }

    @Override
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    private void handleCommand(KeyEvent e) throws IDroneController.DroneControllerException {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                messageListener.messageCommandStartEventOccurred("Forward");
                messageListener.messageCommandEventOccurred(droneController, "Drone flying forward...");
                droneController.getDrone().forward();
                messageListener.messageCommandEventOccurred(droneController, "Drone finished flying forward!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_S:
                messageListener.messageCommandStartEventOccurred("Backward");
                messageListener.messageCommandEventOccurred(droneController, "Drone flying backward...");
                droneController.getDrone().backward();
                messageListener.messageCommandEventOccurred(droneController, "Drone finished flying backward!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_Q:
                messageListener.messageCommandStartEventOccurred("Up");
                messageListener.messageCommandEventOccurred(droneController, "Drone flying up...");
                droneController.getDrone().up();
                messageListener.messageCommandEventOccurred(droneController, "Drone finished flying up!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_E:
                messageListener.messageCommandStartEventOccurred("Down");
                messageListener.messageCommandEventOccurred(droneController, "Drone flying down...");
                droneController.getDrone().down();
                messageListener.messageCommandEventOccurred(droneController, "Drone finished flying down!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_A:
                messageListener.messageCommandStartEventOccurred("Left");
                messageListener.messageCommandEventOccurred(droneController, "Drone flying left...");
                droneController.getDrone().goLeft();
                messageListener.messageCommandEventOccurred(droneController, "Drone finished flying left!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_D:
                messageListener.messageCommandStartEventOccurred("Right");
                messageListener.messageCommandEventOccurred(droneController, "Drone flying right...");
                droneController.getDrone().goRight();
                messageListener.messageCommandEventOccurred(droneController, "Drone finished flying right!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_P:
                messageListener.messageCommandStartEventOccurred("Autonomous");
                messageListener.messageCommandEventOccurred(droneController, "Autonomous clicked!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_O:
                droneController.startDrone();
                break;
            case KeyEvent.VK_I:
                droneController.stopDrone();
                break;
            case KeyEvent.VK_ENTER:
                droneController.takeOffDrone();
                break;
            case KeyEvent.VK_SPACE:
                droneController.landDrone();
                break;
            case KeyEvent.VK_H:
                droneController.hoverDrone();
                break;
            case KeyEvent.VK_R:
                droneController.doSearchRotation();
                break;
            case KeyEvent.VK_C:
                droneController.circleAroundObject();
                break;
            case KeyEvent.VK_F:
                messageListener.messageCommandStartEventOccurred("Flip");
                messageListener.messageCommandEventOccurred(droneController, "Drone taking a flip...");
                droneController.getDrone().getCommandManager().animate(FlightAnimation.FLIP_BEHIND);
                messageListener.messageCommandEventOccurred(droneController, "Drone done flipping!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_G:
                messageListener.messageCommandStartEventOccurred("Dance");
                messageListener.messageCommandEventOccurred(droneController, "Drone dancing...");
                droneController.getDrone().getCommandManager().animate(FlightAnimation.FLIP_BEHIND);
                messageListener.messageCommandEventOccurred(droneController, "Drone done dancing!");
                messageListener.messageCommandEndEventOccurred();
                break;
            case KeyEvent.VK_N:
                droneController.setSpeed(droneController.getSpeed() + 1);
                break;
            case KeyEvent.VK_M:
                droneController.setSpeed(droneController.getSpeed() - 1);
                break;
            default:
                messageListener.messageCommandStartEventOccurred("Error");
                messageListener.messageCommandEventOccurred(this, "Unknown command '" + e.getKeyChar() + "'!");
                messageListener.messageCommandEndEventOccurred();
                break;
        }

    }

}