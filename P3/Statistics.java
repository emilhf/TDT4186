/**
 * This class contains a lot of public variables that can be updated
 * by other classes during a simulation, to collect information about
 * the run.
 */
public class Statistics {
    /**
     * A lot of redundant garbage comments
     */
    public long nofCompletedProcesses = 0;
    public long nofCreatedProcesses = 0;
    public long totalTimeSpentWaitingForMemory = 0;
    public long memoryQueueLengthTime = 0;
    public long memoryQueueLargestLength = 0;

    /**
     * New stuff
     */
    // CPU
    public long totalCpuProcessing = 0;
    public long fractionCupProcessing = 0;
    public long totalCpuWaiting = 0;
    public long fractionCupWaiting = 0;

    // I/O


    /**
     * Prints out a report summarizing all collected data about the simulation.
     *
     * @param simulationLength The number of milliseconds that the simulation covered.
     */
    public void printReport(long simulationLength) {
        System.out.println();
        System.out.println("Simulation statistics:");
        System.out.println();
        System.out.println("Number of completed processes:                                " + nofCompletedProcesses);
        System.out.println("Number of created processes:                                  " + nofCreatedProcesses);
        System.out.println();
        System.out.println("Largest occuring memory queue length:                         " + memoryQueueLargestLength);
        System.out.println("Average memory queue length:                                  " + (float) memoryQueueLengthTime / simulationLength);
        if (nofCompletedProcesses > 0) {
            System.out.println("Average # of times a process has been placed in memory queue: " + 1);
            System.out.println("Average time spent waiting for memory per process:            " +
                    totalTimeSpentWaitingForMemory / nofCompletedProcesses + " ms");
        }
    }
}
