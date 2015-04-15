/**
 * The main class of the P3 exercise. This class is only partially complete.
 */
public class Simulator implements Constants {

    private EventQueue eventQueue;
    private Memory memory;
    private Gui gui;
    private Statistics statistics;
    private long clock;
    private long simulationLength;
    private long avgArrivalInterval;

    // NEW
    private CPU cpu;
    private IO io;


    /**
     * Constructs a scheduling simulator with the given parameters.
     *
     * @param memoryQueue        The memory queue to be used.
     * @param cpuQueue           The CPU queue to be used.
     * @param ioQueue            The I/O queue to be used.
     * @param memorySize         The size of the memory.
     * @param maxCpuTime         The maximum time quant used by the RR algorithm.
     * @param avgIoTime          The average length of an I/O operation.
     * @param simulationLength   The length of the simulation.
     * @param avgArrivalInterval The average time between process arrivals.
     * @param gui                Reference to the GUI interface.
     */
    public Simulator(Queue memoryQueue, Queue cpuQueue, Queue ioQueue, long memorySize,
                     long maxCpuTime, long avgIoTime, long simulationLength, long avgArrivalInterval, Gui gui) {
        this.simulationLength = simulationLength;
        this.avgArrivalInterval = avgArrivalInterval;
        this.gui = gui;
        statistics = new Statistics();
        eventQueue = new EventQueue();
        memory = new Memory(memoryQueue, memorySize, statistics);
        cpu = new CPU(cpuQueue, maxCpuTime, statistics);
        io = new IO(ioQueue, avgIoTime, statistics);
        clock = 0;

    }

    /**
     * Starts the simulation. Contains the main loop, processing events.
     * This method is called when the "Start simulation" button in the
     * GUI is clicked.
     */
    public void simulate() {
        System.out.print("Simulating...\n");
        eventQueue.insertEvent(new Event(NEW_PROCESS, 0));

        // Agenda loop
        while (clock < simulationLength && !eventQueue.isEmpty()) {

            Event event = eventQueue.getNextEvent();

            // Update clock
            long timeDifference = event.getTime() - clock;
            clock = event.getTime();

            // Let the memory unit and the GUI know that time has passed
            memory.timePassed(timeDifference);
            gui.timePassed(timeDifference);
            cpu.timePassed(timeDifference);
            io.timePassed(timeDifference);

            // Deal with the event
            if (clock < simulationLength) {
                processEvent(event);
            }
        }
        System.out.println("..done.");

        memory.statisticizeRemaining();
        cpu.statisticizeRemaining();
        io.statisticizeRemaining();

        statistics.printSim(clock);
    }

    /**
     * Processes an event by inspecting its type and delegating
     * the work to the appropriate method.
     *
     * @param event The event to be processed.
     */
    private void processEvent(Event event) {
        switch (event.getType()) {
            case NEW_PROCESS:

                createProcess();
                break;

            case SWITCH_PROCESS:

                switchProcess();
                break;

            case END_PROCESS:

                endProcess();
                break;

            case IO_REQUEST:

                processIoRequest();
                break;

            case END_IO:

                endIoOperation();
                break;

        }
    }

    /**
     * Simulates a process arrival/creation.
     * When a process is submitted, a new process is added to event queue.
     */
    private void createProcess() {
        // Create a new process
        Process newProcess = new Process(memory.getMemorySize(), clock);
        memory.insertProcess(newProcess);
        flushMemoryQueue();

        // Add an event for the next process arrival
        long nextArrivalTime = clock + 1 + (long) (2 * Math.random() * avgArrivalInterval);
        eventQueue.insertEvent(new Event(NEW_PROCESS, nextArrivalTime));
    }

    private void flushMemoryQueue() {
        Process p = memory.checkMemory(clock);
        while (p != null) {

            /** Admits process to memory, wakes CPU if idle */
            Event cpuEvent = cpu.insertProcess(p, clock);
            if (cpuEvent != null) {
                eventQueue.insertEvent(cpuEvent);
            }

            p = memory.checkMemory(clock);
        }
    }

    private void switchProcess() {
        eventQueue.insertEvent(cpu.handleSwitch(clock));
        gui.setCpuActive(cpu.getCurrentProcess());
    }

    /**
     * Ends the active process, and deallocates any resources allocated to it.
     */
    private void endProcess() {
        memory.updateMemory(cpu.getCurrentProcess());
        eventQueue.insertEvent(cpu.handleEnd(clock));
        gui.setCpuActive(cpu.getCurrentProcess());
        flushMemoryQueue();
    }

    private void processIoRequest() {

        Event ioEvent = io.insertProcess(cpu.getCurrentProcess(), clock);
        if (ioEvent != null) {
            eventQueue.insertEvent(ioEvent);
        }
        eventQueue.insertEvent(cpu.handleIO(clock));
        gui.setIoActive(io.getCurrentProcess());
        gui.setCpuActive(cpu.getCurrentProcess());

    }

    private void endIoOperation() {

        Event nextCPU = cpu.insertProcess(io.getCurrentProcess(), clock);
        if (nextCPU != null) {
            eventQueue.insertEvent(nextCPU);
        }
        Event nextIO = io.handleEndIO(clock);
        if (nextIO != null) {
            eventQueue.insertEvent(nextIO);
        }
        gui.setIoActive(io.getCurrentProcess());
        gui.setCpuActive(cpu.getCurrentProcess());
    }

    public static void main(String args[]) {
        long memorySize = Long.valueOf(args[0]);
        long maxCpuTime = Long.valueOf(args[1]);
        long avgIoTime = Long.valueOf(args[2]);
        long simulationLength = Long.valueOf(args[3]);
        long avgArrivalInterval = Long.valueOf(args[4]);

        System.out.printf("Starting with input: \n mem size: \t\t\t\t%d kB\n CPU time: \t\t\t\t%d ms \n I/O time: \t\t\t\t%d ms \n Arrival interval: \t\t%d ms \n simulation lenght: \t%d ms \n", memorySize, maxCpuTime, avgIoTime, avgArrivalInterval, simulationLength);

        SimulationGui gui = new SimulationGui(memorySize, maxCpuTime, avgIoTime, simulationLength, avgArrivalInterval);
    }
}
