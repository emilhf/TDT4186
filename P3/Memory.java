/**
 * This class implements functionality associated with
 * the memory device of the simulated system.
 */
public class Memory {
    private Queue memoryQueue;
    private Statistics statistics;
    private long memorySize;
    private long freeMemory;

    // Constructor
    public Memory(Queue memoryQueue, long memorySize, Statistics statistics) {
        this.memoryQueue = memoryQueue;
        this.memorySize = memorySize;
        this.statistics = statistics;
        freeMemory = memorySize;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void insertProcess(Process p) {
        memoryQueue.insert(p);
    }

    // Admits a process if there is memory for it.
    public Process checkMemory(long clock) {
        if (!memoryQueue.isEmpty()) {
            Process nextProcess = (Process) memoryQueue.getNext();
            if (nextProcess.getMemoryNeeded() <= freeMemory) {
                freeMemory -= nextProcess.getMemoryNeeded();
                nextProcess.leftMemoryQueue(clock);
                memoryQueue.removeNext();
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
        statistics.memStats.update(timePassed, memoryQueue.getQueueLength());
    }
}

