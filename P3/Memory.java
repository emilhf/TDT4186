/**
 * This class implements functionality associated with
 * the memory device of the simulated system.
 */
public class Memory extends ProcessQueue {
    private long memorySize;
    private long freeMemory;

    public Memory(Queue<Process> memoryQueue, long memorySize, Statistics statistics) {
        super(memoryQueue, statistics);
        this.memorySize = memorySize;
        freeMemory = memorySize;
    }

    public long getMemorySize() {
        return memorySize;
    }

    // Admits a process if there is memory for it.
    public Process checkMemory(long clock) {
        if (!isEmpty()) {
            Process nextProcess = getNext();
            if (nextProcess.getMemoryNeeded() <= freeMemory) {
                freeMemory -= nextProcess.getMemoryNeeded();
                nextProcess.leftMemoryQueue(clock);
                getQueue().removeNext();
                return nextProcess;
            }
        }
        return null;
    }

    // Called when a process terminates
    public void updateMemory(Process p) {
        this.freeMemory += p.getMemoryNeeded();
    }

    // Update stats
    public void timePassed(long timePassed) {
        getStatistics().memStats.update(timePassed, getQueueLength());
    }
}

