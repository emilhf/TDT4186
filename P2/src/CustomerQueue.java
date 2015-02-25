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
        this.lastInQueue = 0;
	}

	public int enqueue(Customer customer) {
        int queueCurrentLast = lastInQueue;
        int nextQueuePos = (lastInQueue + 1) % queue.length;

        if (queue[nextQueuePos] != null) {
            return -1;
        }

        queue[nextQueuePos] = customer;
        gui.fillLoungeChair(nextQueuePos, customer);

        lastInQueue = nextQueuePos;

        return queueCurrentLast;
    }

    public Customer dequeue() {
        Customer r = queue[firstInQueue];
        if (r == null) {
            return null;
        }

        queue[firstInQueue] = null;
        gui.emptyLoungeChair(firstInQueue);

        firstInQueue = (firstInQueue + 1) % queue.length;

        return r;
    }
}
