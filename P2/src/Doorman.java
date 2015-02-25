/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 */
public class Doorman extends Thread {
    /**
     * Creates a new doorman.
     *
     * @param queue        The customer queue.
     * @param gui        A reference to the GUI interface.
     */

    CustomerQueue queue;
    Gui gui;

    public Doorman(CustomerQueue queue, Gui gui) {
        this.queue = queue;
        this.gui = gui;
    }

    /**
     * Starts the doorman running as a separate thread.
     */
    public void startThread() throws InterruptedException {
        this.sleep(Globals.barberSleep);
    }

    /**
     * Stops the doorman thread.
     */
    public void stopThread() {
        // Incomplete
    }

    public void addCustomer() {

    }
}
