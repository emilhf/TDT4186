/**
 * Created by peter on 4/8/15.
 */

/**
 * This class implements functionality associated with
 * the CPU of the simulated system.
 */

public class CPU {

    // These are self explanatory
    private Queue cpuQueue;
    private long maxCpuTime;
    private Process currentProcess;
    private Statistics statistics;
    private boolean idle = true;

    public Event handleEnd(long clock) {
        if (cpuQueue.isEmpty()) {
            this.idle = true;
            this.currentProcess = null;
            return null;
        }
        currentProcess = popQueue();
        return currentProcess.run(maxCpuTime, clock);
    }

    public Event handleSwitch(long clock) {
        insertProcess(currentProcess, clock);
        currentProcess = popQueue();
        return currentProcess.run(maxCpuTime, clock);
    }

    public Event handleIO(long clock) {
        if (cpuQueue.isEmpty()) {
            this.idle = true;
            currentProcess = null;
            return null;
        }
        currentProcess = popQueue();
        return currentProcess.run(maxCpuTime, clock);
    }

    public CPU(Queue cpuQueue, long maxCpuTime, Statistics statistics) {
        this.cpuQueue = cpuQueue;
        this.statistics = statistics;
        this.maxCpuTime = maxCpuTime;
    }

    private Process popQueue() {
        if (!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.removeNext();
            return nextProcess;
        }
        return null;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    /**
     * Adds a process to the CPU queue.
     * If the Queue is empty we add an event to get the ball rolling.
     * If adding the process leads to an event it is returned
     *
     * @param p The process to be added.
     */
    public Event insertProcess(Process p, long clock) {
        cpuQueue.insert(p);
        if (idle) {
            currentProcess = (Process) cpuQueue.removeNext();
            idle = false;
            return p.run(maxCpuTime, clock);
        }
        return null;
    }

    public void timePassed(long timePassed) {
        statistics.memoryQueueLengthTime += cpuQueue.getQueueLength() * timePassed;
        if (cpuQueue.getQueueLength() > statistics.memoryQueueLargestLength) {
            statistics.memoryQueueLargestLength = cpuQueue.getQueueLength();
        }
    }
}




