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
        currentProcess.processTerminated(clock, statistics);
        if (cpuQueue.isEmpty()) {
            this.idle = true;
            this.currentProcess = null;
            return null;
        }
        currentProcess = popQueue(clock);
        return currentProcess.run(maxCpuTime, clock);
    }

    public Event handleSwitch(long clock) {
        currentProcess.leftCpu(clock);
        insertProcess(currentProcess, clock);
        currentProcess = popQueue(clock);
        return currentProcess.run(maxCpuTime, clock);
    }

    public Event handleIO(long clock) {
        currentProcess.leftCpu(clock);
        if (cpuQueue.isEmpty()) {
            this.idle = true;
            currentProcess = null;
            return null;
        }
        currentProcess = popQueue(clock);
        return currentProcess.run(maxCpuTime, clock);
    }

    public CPU(Queue cpuQueue, long maxCpuTime, Statistics statistics) {
        this.cpuQueue = cpuQueue;
        this.statistics = statistics;
        this.maxCpuTime = maxCpuTime;
    }

    private Process popQueue(long clock) {
        if (!cpuQueue.isEmpty()) {
            Process nextProcess = (Process) cpuQueue.removeNext();
            nextProcess.leftCpuQueue(clock);
            return nextProcess;
        }
        return null;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    // Inserts a process and wakes up the processor (generates a CPU event) if found idle
    public Event insertProcess(Process p, long clock) {
        cpuQueue.insert(p);
        p.cpuQueued();
        if (idle) {
            currentProcess = (Process) cpuQueue.removeNext();
            idle = false;
            return p.run(maxCpuTime, clock);
        }
        return null;
    }

    // update queue related vars
    public void timePassed(long timePassed) {
        statistics.cpuStats.update(timePassed, cpuQueue.getQueueLength(), idle);
    }
}




