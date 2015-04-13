




public class Statistics {

    // All process stats are accumulated, averages computed at the end
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
            this.created++;
            if(p.terminated){this.finished++;}
            this.timeInCpuQueue += p.timeSpentWaitingForCpu;
            this.timeInIoQueue += p.timeSpentWaitingForIo;
            this.timeInIo += p.timeSpentInIo;
            this.timeInCpu += p.timeSpentInCpu;
        }
        public void print(){
            System.out.println("Process statz");
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
            System.out.println("IO statz");
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
            System.out.println("CPU statz");
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

    // Holy shit what the FUCK is this!???
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
