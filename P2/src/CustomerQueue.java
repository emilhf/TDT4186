/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
    private Gui gui;
    private Customer[] queue;
    private int queueHeadPointer;
    private int queueTailPointer;

    /**
     * Creates a new customer queue.
     *
     * @param queueLength The maximum length of the queue.
     * @param gui         A reference to the GUI interface.
     */
    public CustomerQueue(int queueLength, Gui gui) {
        this.gui = gui;
        this.queue = new Customer[queueLength];
        this.queueHeadPointer = 0;
        this.queueTailPointer = -1;
    }

    public synchronized int enqueue(Customer customer) {
        int currentQueueTail = queueTailPointer;
        int nextQueueTail = (queueTailPointer + 1) % queue.length;

        while (queue[nextQueueTail] != null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        queue[nextQueueTail] = customer;
        gui.fillLoungeChair(nextQueueTail, customer);

        queueTailPointer = nextQueueTail;

        notifyAll();
        return currentQueueTail;
    }

    public synchronized Customer dequeue() {
        Customer r;
        while (true) {
            r = queue[queueHeadPointer];

            if (r == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            } else {
                break;
            }
        }

        queue[queueHeadPointer] = null;
        gui.emptyLoungeChair(queueHeadPointer);

        queueHeadPointer = (queueHeadPointer + 1) % queue.length;

        notifyAll();
        return r;
    }
}
