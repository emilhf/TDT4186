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

    private Process popQueue(long clock) {
        if (!ioQueue.isEmpty()) {
            Process nextProcess = (Process) ioQueue.removeNext();
            nextProcess.leftIoQueue(clock);
            return nextProcess;
        }
        return null;
    }

    public Event handleEndIO(long clock) {
        this.currentProcess.leftIo(clock);
        if (ioQueue.isEmpty()) {
            this.idle = true;
            this.currentProcess = null;
            return null;
        }
        currentProcess = popQueue(clock);
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
        p.ioQueued();
        if (idle) {
            currentProcess = (Process) ioQueue.removeNext();
            idle = false;
            long time = clock + getIOtime();
            return new Event(END_IO, time);
        }
        return null;
    }

    // Update queue related vars
    public void timePassed(long timePassed) {
        statistics.ioStats.update(timePassed, ioQueue.getQueueLength(), idle);
    }

    public void statisticizeRemaining() {
        for (int i = 0; i < ioQueue.getQueueLength(); i++) {
            statistics.logProcess((Process) ioQueue.get(i));
        }
    }
}
