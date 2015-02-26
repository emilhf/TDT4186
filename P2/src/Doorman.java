/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 */
public class Doorman implements Runnable {
    /**
     * Creates a new doorman.
     *
     * @param queue        The customer queue.
     * @param gui        A reference to the GUI interface.
     */

    CustomerQueue queue;
    Gui gui;
    Thread thread;

    public Doorman(CustomerQueue queue, Gui gui) {
        this.queue = queue;
        this.gui = gui;
        this.thread = new Thread(this, "doorman");
    }

    /**
     * Starts the doorman running as a separate thread.
     */
    public void startThread() {
        thread.start();
    }

    public void run() {
        while (true) {
            addCustomer();
            doormanSleep();
        }
    }

    private void doormanSleep() {
        try {
            int min = -Globals.doormanSleep / 2;
            int max = -Globals.doormanSleep / 2;
            int r = min + (int) (Math.random() * (max - min + 1));
            Thread.sleep(Globals.doormanSleep + r);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Stops the doorman thread.
     */
    public void stopThread() {}

    public void addCustomer() {
        gui.println("New customer");
        int pos = queue.enqueue(new Customer());
        gui.println("Customer placed in pos. " + Integer.toString(pos));
    }
}
