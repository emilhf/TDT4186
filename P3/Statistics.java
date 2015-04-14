




public class Statistics {

    // All process stats are accumulated, averages computed at the end
    // Processes are updated via the processTerminated method in Process.java
    // This method is called by CPU.java
    // TODO: Round up remaining processes in system
    private class Processes_stats{
        private long lifetime = 0;
        private long created = 0;
        private long finished = 0;
        private long timeInIo = 0;
        private long timeInCpu = 0;
        private long timeInIoQueue = 0;
        private long timeInCpuQueue = 0;

        public void update(Process p){
            lifetime += p.lifeTime;
            created++;
            if(p.terminated){finished++;}
            timeInCpuQueue += p.timeSpentWaitingForCpu;
            timeInIoQueue += p.timeSpentWaitingForIo;
            timeInIo += p.timeSpentInIo;
            timeInCpu += p.timeSpentInCpu;
        }
        public void print(){
            System.out.println("Process stats:");
            System.out.printf("Processes created: %d\n", created);
            System.out.printf("Processes completed: %d\n", finished);
            System.out.printf("Average lifetime: %dms\n", lifetime/finished);
            System.out.printf("Average time spent in IO: %dms\n", timeInIo/created);
            System.out.printf("Average time spent in IO queue %dms\n", timeInIoQueue/created);
            System.out.printf("Average time spent in CPU %dms\n", timeInCpu/created);
            System.out.printf("Average time spent in CPU queue %dms\n", timeInCpuQueue/created);
            System.out.printf("Average time spent in memory queue %dms\n", (lifetime-(timeInCpu + timeInCpuQueue + timeInIo + timeInIoQueue))/created);
        }
    }

    // Stats are accumulated as with Processes
    public class IO_stats{
        private long timeQueue = 0;
        private long longest = 0;
        private long timeBusy = 0;

        public void update(long timePassed, long queueLength, boolean idle){
            timeQueue += timePassed*queueLength;
            if(!idle){timeBusy += timePassed;}
            if(longest<queueLength){longest = queueLength;}
        }

        private void print(){
            System.out.println("IO unit stats:");
            System.out.printf("Time spent processing IO request: %dms\n", timeBusy);
            System.out.printf("Time spent idle: %dms\n", simulationTime - timeBusy);
            System.out.printf("Fraction time spent busy: %.2f%%\n", (float)(100*timeBusy)/(simulationTime));
            System.out.printf("Fraction time spent idle: %.2f%%\n", (100.0 - ((float)100*timeBusy)/(simulationTime)));
            System.out.printf("Average queue length: %.2f\n", (float) timeQueue / simulationTime);
            System.out.printf("Longest CPU queue: %d\n\n", longest);

        }
    }

    public class CPU_stats{
        private long timeQueue = 0;
        private long longest = 0;
        private long timeBusy = 0;

        public void update(long timePassed, long queueLength, boolean idle){
            timeQueue += timePassed*queueLength;
            if(!idle){timeBusy += timePassed;}
            if(longest<queueLength){longest = queueLength;}
        }

        public void print(){
            System.out.printf("CPU unit stats:");
            System.out.printf("Time spent processing: %dms\n", timeBusy);
            System.out.printf("Time spent idle: %d\n", simulationTime - timeBusy);
            System.out.printf("Fraction time spent busy: %.2f%%\n", (float)(100*timeBusy)/(simulationTime));
            System.out.printf("Fraction time spent idle: %.2f%%\n", (100.0 - ((float)100*timeBusy)/(simulationTime)));
            System.out.printf("Average queue length: %.2f\n", (float)timeQueue/simulationTime);
            System.out.printf("Longest CPU queue: %d\n\n", longest);
        }
    }

    public class Memory_stats{
        private long timeQueue = 0;
        private long longest = 0;

        public void update(long timePassed, long queueLength){
            timeQueue += timePassed*queueLength;
            if(longest<queueLength){longest = queueLength;}
        }
    }

    private long simulationTime = 0;
    public Processes_stats pStats;
    public IO_stats ioStats;
    public CPU_stats cpuStats;
    public Memory_stats memStats;

    public void logTerminatedProcess(Process p){

        this.pStats.update(p);
    }

    public Statistics(){
        this.pStats = new Processes_stats();
        this.ioStats = new IO_stats();
        this.memStats = new Memory_stats();
        this.cpuStats = new CPU_stats();
    }

    public void printSim(long simulationLength){
        this.simulationTime = simulationLength;
        this.cpuStats.print();
        this.ioStats.print();
        this.pStats.print();
    }

    // Holy SHIT what the FUCK is this!???-
    /**
    public void shame(long simulationLength) {
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
    */


}
