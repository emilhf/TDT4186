/**
 * This class implements functionality associated with
 * the CPU of the simulated system.
 */

public class CPU extends ProcessQueue {
    private long maxCpuTime;
    private Process currentProcess;
    private boolean idle = true;

    public Event handleEnd(long clock) {
        currentProcess.processTerminated(clock, getStatistics());
        if (isEmpty()) {
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
        if (isEmpty()) {
            this.idle = true;
            currentProcess = null;
            return null;
        }
        currentProcess = popQueue(clock);
        return currentProcess.run(maxCpuTime, clock);
    }

    public CPU(Queue<Process> cpuQueue, long maxCpuTime, Statistics statistics) {
        super(cpuQueue, statistics);
        this.maxCpuTime = maxCpuTime;
    }

    private Process popQueue(long clock) {
        if (!isEmpty()) {
            Process nextProcess = removeNext();
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
        insertProcess(p);
        p.cpuQueued();
        if (idle) {
            currentProcess = (Process) getQueue().removeNext();
            idle = false;
            return p.run(maxCpuTime, clock);
        }
        return null;
    }

    // update queue related vars
    public void timePassed(long timePassed) {
        getStatistics().cpuStats.update(timePassed, getQueueLength(), idle);
    }
}




