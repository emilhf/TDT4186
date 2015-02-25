/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
    private Gui gui;
    private Customer[] queue;
    private int firstInQueue;
    private int lastInQueue;

    /**
	 * Creates a new customer queue.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */
    public CustomerQueue(int queueLength, Gui gui) {
        this.gui = gui;
        this.queue = new Customer[queueLength];
        this.firstInQueue = 0;
        this.lastInQueue = -1;
	}

	public boolean enqueue(Customer customer) {
        if (queue[lastInQueue] != null) {
            return false;
        }

        queue[lastInQueue] = customer;

        lastInQueue = (lastInQueue + 1) % queue.length;

        return true;
    }

    public Customer dequeue() {
        Customer r = queue[firstInQueue];
        if (r == null) {
            return null;
        }

        queue[firstInQueue] = null;

        firstInQueue = (firstInQueue + 1) % queue.length;

        return r;
    }
}
