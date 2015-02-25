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
    public void startThread() {
        while (true) {
            addCustomer();
            try {
                sleep(Globals.doormanSleep);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Stops the doorman thread.
     */
    public void stopThread() {
        System.out.println("goodbye");
    }

    public void addCustomer() {
        gui.println("New customer");
        int pos = queue.enqueue(new Customer());
        gui.println(pos == -1 ? "No room :(" : "Customer placed in pos. " + Integer.toString(pos));
    }
}
