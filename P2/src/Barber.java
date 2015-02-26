/**
 * This class implements the barber's part of the
 * Barbershop thread synchronization example.
 */
public class Barber implements Runnable {

    private CustomerQueue queue;
    private Gui gui;
    private int pos;
    private Thread thread;

    /**
     * Creates a new barber.
     *
     * @param queue The customer queue.
     * @param gui   The GUI.
     * @param pos   The position of this barber's chair
     */
    public Barber(CustomerQueue queue, Gui gui, int pos) {
        this.queue = queue;
        this.gui = gui;
        this.pos = pos;
        this.thread = new Thread(this, "barber " + Integer.toString(pos));
    }

    /**
     * Starts the barber running as a separate thread.
     */
    public void startThread() {
        thread.start();
    }

    public void run() {
        while (true) {
            gui.barberIsSleeping(pos);
            gui.println("Barber " + Integer.toString(pos) + " start daydream");
            try {
                int min = -Globals.barberSleep / 2;
                int max = -Globals.barberSleep / 2;
                int r = min + (int) (Math.random() * (max - min + 1));
                Thread.sleep(Globals.barberSleep + r);
            } catch (InterruptedException e) {
                break;
            }
            gui.barberIsAwake(pos);

            Customer customer = getCustomerFromQueue();
            if (customer == null) {
                continue;
            }

            gui.fillBarberChair(pos, customer);
            gui.println("Barber " + Integer.toString(pos) + " start work");

            try {
                int min = -Globals.barberWork / 2;
                int max = -Globals.barberWork / 2;
                int r = min + (int) (Math.random() * (max - min + 1));
                Thread.sleep(Globals.barberWork + r);
            } catch (InterruptedException e) {
                break;
            }

            gui.emptyBarberChair(pos);
            gui.println("Barber " + Integer.toString(pos) + " end work");
        }
    }

    /**
     * Stops the barber thread.
     */
    public void stopThread() {}

    synchronized Customer getCustomerFromQueue() {
        return queue.dequeue();
    }
}

