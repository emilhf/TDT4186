/**
 * Created by peter on 4/8/15.
 */

/**
 * This class implements functionality associated with
 * the I/O of the simulated system.
 */
public class IO implements Constants {

    private Queue ioQueue;
    private Process currentProcess;
    private Statistics statistics;
    private boolean idle = true;
    private long ioTime;

    private long getIOtime() {
        return ioTime;
    }

    public IO(Queue ioQueue, long IOtime, Statistics statistics) {
        this.ioQueue = ioQueue;
        this.ioTime = IOtime;
        this.statistics = statistics;
        this.idle = true;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    private Process popQueue() {
        if (!ioQueue.isEmpty()) {
            Process nextProcess = (Process) ioQueue.removeNext();
            return nextProcess;
        }
        return null;
    }

    public Event handleEndIO(long clock) {
        if (ioQueue.isEmpty()) {
            this.idle = true;
            this.currentProcess = null;
            return null;
        }
        currentProcess = popQueue();
        return new Event(END_IO, clock + getIOtime());
    }

    /**
     * Adds a process to the IO queue.
     * If the Queue is empty we add an event to get the ball rolling.
     * If adding the process leads to an event it is returned
     *
     * @param p The process to be added.
     */
    public Event insertProcess(Process p, long clock) {
        ioQueue.insert(p);
        if (idle) {
            currentProcess = (Process) ioQueue.removeNext();
            idle = false;
            long time = clock + getIOtime();
            return new Event(END_IO, time);
        }
        return null;
    }

    // TODO this shittu
    public void timePassed(long timePassed) {
        statistics.memoryQueueLengthTime += ioQueue.getQueueLength() * timePassed;
        if (ioQueue.getQueueLength() > statistics.memoryQueueLargestLength) {
            statistics.memoryQueueLargestLength = ioQueue.getQueueLength();
        }
    }
}
