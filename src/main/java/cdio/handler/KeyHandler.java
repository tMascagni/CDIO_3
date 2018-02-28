package cdio.handler;

import cdio.handler.interfaces.IKeyHandler;

import java.awt.event.KeyEvent;

public final class KeyHandler implements IKeyHandler {

    private boolean[] keyPool = new boolean[120];

    /*
     * KEY INFORMATION:
     *
     * -- FLYING CONTROL --
     * flyForward:    W / arrow up
     * flyBackward:   S / arrow down
     * flyUp:         Q
     * flyDown:       E
     * flyLeft:       A / arrow left
     * flyRight:      D / arrow right
     *
     * -- ACTION CONTROL --
     * isArrows:      P (Switches between using WASD or arrow keys for flying the drone.)
     * isAutonomous:  O (Switches between autonomous and manual control of the drone. START needs to be pressed for autonomous mode to start.)
     * start:         I (Starts the drone, i.e. makes it ready to take off, etc. DOES NOT MAKE IT FLY YET!)
     * stop:          I (Stops the drone, shuts it down. I think this might be safe to do when flying? It might just land.)
     * takeoff:       Spacebar (The drone takes off!)
     * land:          Spacebar (The drone lands.)
     * hover:         H (The drone hovers (predefined milliseconds for this method call))
     * rotate:        R (Rotates the drone 360 degrees, and the drone scans stuff (?))
     * circleObject:  C (Circles around the nearest object (?))
     * flip:          F (Flips like cray.)
     * incSpeed:      N (Increase speed by 1.)
     * decSpeed:      M (Decrease speed by 1.)
     */

    /* Flying control keys */
    public boolean flyForward, flyBackward, flyUp, flyDown, flyLeft, flyRight;

    /* Action keys */
    public boolean isArrows, isAutonomous, start, stop, takeoff, land, hover, rotate, circleObject, flip, incSpeed, decSpeed;

    private static IKeyHandler instance;

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
        isArrows = keyPool[KeyEvent.VK_P];
        isAutonomous = keyPool[KeyEvent.VK_O];

        start = keyPool[KeyEvent.VK_I];
        stop = keyPool[KeyEvent.VK_I];

        takeoff = keyPool[KeyEvent.VK_SPACE];
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
        // TEST
        System.out.println("DEBUG: " + e.getKeyCode() + ":" + e.getKeyChar() + " pressed!");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPool[e.getKeyCode()] = false;
    }

}