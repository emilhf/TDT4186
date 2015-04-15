/**
 * This class implements functionality associated with
 * the I/O of the simulated system.
 */
public class IO extends ProcessQueue implements Constants {
    private Process currentProcess;
    private boolean idle = true;
    private long ioTime;

    private long getIOtime() {
        return ioTime;
    }

    public IO(Queue<Process> ioQueue, long IOtime, Statistics statistics) {
        super(ioQueue, statistics);
        this.ioTime = IOtime;
        this.idle = true;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    private Process popQueue(long clock) {
        if (!isEmpty()) {
            Process nextProcess = (Process) removeNext();
            nextProcess.leftIoQueue(clock);
            return nextProcess;
        }
        return null;
    }

    public Event handleEndIO(long clock) {
        this.currentProcess.leftIo(clock);
        if (isEmpty()) {
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
        insertProcess(p);
        p.ioQueued();
        if (idle) {
            currentProcess = (Process) removeNext();
            idle = false;
            long time = clock + getIOtime();
            return new Event(END_IO, time);
        }
        return null;
    }

    // Update queue related vars
    public void timePassed(long timePassed) {
        getStatistics().ioStats.update(timePassed, getQueueLength(), idle);
    }
}
