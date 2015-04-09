/**
 * This class implements functionality associated with
 * the memory device of the simulated system.
 */
public class Memory {
    private Queue memoryQueue;
    private Statistics statistics;
    private long memorySize;
    private long freeMemory;

    /**
     * Creates a new memory device with the given parameters.
     *
     * @param memoryQueue The memory queue to be used.
     * @param memorySize  The amount of memory in the memory device.
     * @param statistics  A reference to the statistics collector.
     */
    public Memory(Queue memoryQueue, long memorySize, Statistics statistics) {
        this.memoryQueue = memoryQueue;
        this.memorySize = memorySize;
        this.statistics = statistics;
        freeMemory = memorySize;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void insertProcess(Process p) {
        memoryQueue.insert(p);
    }

    /**
     * Checks whether or not there is enough free memory to let
     * the first process in the memory queue proceed to the cpu queue.
     * If there is, the process that was granted memory is returned,
     * otherwise null is returned.
     *
     * @param clock The current time.
     */
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

    /**
     * When a process leaves we update free memory
     */
    public void updateMemory(Process p) {
        this.freeMemory += p.getMemoryNeeded();
    }

    /**
     * This method is called when a discrete amount of time has passed.
     *
     * @param timePassed The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        statistics.memoryQueueLengthTime += memoryQueue.getQueueLength() * timePassed;
        if (memoryQueue.getQueueLength() > statistics.memoryQueueLargestLength) {
            statistics.memoryQueueLargestLength = memoryQueue.getQueueLength();
        }
    }
}

