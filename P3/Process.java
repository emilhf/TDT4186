import java.awt.*;

/**
 * This class contains data associated with processes,
 * and methods for manipulating this data as well as
 * methods for displaying a process in the GUI.
 * <p/>
 * You will probably want to add more methods to this class.
 */
public class Process implements Constants {
    // It is generally considered poor coding practice to add redundant comments.
    private static long nextProcessId = 1;
    private static Font font = new Font("Arial", Font.PLAIN, 10);
    private long processId;
    private Color color;
    private long memoryNeeded;
    private long timeSpentInCpu = 0;

    private long cpuTimeNeeded;
    /**
     * The average time between the need for I/O operations for this process
     */
    private long avgIoInterval;
    private long nextIO = 0;

    private long timeToNextIoOperation = 0;
    private long timeSpentWaitingForMemory = 0;
    private long timeSpentInReadyQueue = 0;
    private long timeSpentWaitingForIo = 0;
    private long timeSpentInIo = 0;

    private long nofTimesInReadyQueue = 0;
    private long nofTimesInIoQueue = 0;

    /**
     * The global time of the last event involving this process
     */
    private long timeOfLastEvent;

    /**
     * Creates a new process with given parameters. Other parameters are randomly
     * determined.
     *
     * @param memorySize   The size of the memory unit.
     * @param creationTime The global time when this process is created.
     */
    public Process(long memorySize, long creationTime) {

        memoryNeeded = 100 + (long) (Math.random() * (memorySize / 4 - 100));
        cpuTimeNeeded = 100 + (long) (Math.random() * 9900);
        avgIoInterval = (1 + (long) (Math.random() * 25)) * cpuTimeNeeded / 100;
        timeOfLastEvent = creationTime;
        processId = nextProcessId++;
        nextIO = getNextIO();

        int red = 64 + (int) ((processId * 101) % 128);
        int green = 64 + (int) ((processId * 47) % 128);
        int blue = 64 + (int) ((processId * 53) % 128);
        color = new Color(red, green, blue);
    }

    /**
     * Whenever the process gets CPU time we check what the next event will be and update accordingly
     */
    public Event run(long quant, long clock) {

        // Termination
        if ((cpuTimeNeeded < nextIO) && (cpuTimeNeeded < quant)) {
            return new Event(END_PROCESS, clock + cpuTimeNeeded);
        }
        // IO interrupt
        else if (nextIO < quant) {
            cpuTimeNeeded -= nextIO;
            nextIO = getNextIO();
            return new Event(IO_REQUEST, clock + nextIO);
        }
        // Preemption
        // System.out.printf("Process running, time: %d, preempt time: %d", clock, clock + );
        cpuTimeNeeded -= quant;
        nextIO -= quant;
        return new Event(SWITCH_PROCESS, clock + quant);
    }

    /**
     * Draws this process as a colored box with a process ID inside.
     *
     * @param g The graphics context.
     * @param x The leftmost x-coordinate of the box.
     * @param y The topmost y-coordinate of the box.
     * @param w The width of the box.
     * @param h The height of the box.
     */
    public void draw(Graphics g, int x, int y, int w, int h) {
        g.setColor(color);
        g.fillRect(x, y, w, h);
        g.setColor(Color.black);
        g.drawRect(x, y, w, h);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        g.drawString("" + processId, x + w / 2 - fm.stringWidth("" + processId) / 2, y + h / 2 + fm.getHeight() / 2);
    }

    /**
     * This method is called when the process leaves the memory queue (and
     * enters the cpu queue).
     *
     * @param clock The time when the process leaves the memory queue.
     */
    public void leftMemoryQueue(long clock) {
        timeSpentWaitingForMemory += clock - timeOfLastEvent;
        timeOfLastEvent = clock;
    }

    /**
     * Returns the amount of memory needed by this process.
     *
     * @return The amount of memory needed by this process.
     */
    public long getMemoryNeeded() {
        return memoryNeeded;
    }

    /**
     * Updates the statistics collected by the given Statistic object, adding
     * data collected by this process. This method is called when the process
     * leaves the system.
     *
     * @param statistics The Statistics object to be updated.
     */
    public void updateStatistics(Statistics statistics) {
        statistics.totalTimeSpentWaitingForMemory += timeSpentWaitingForMemory;
        statistics.nofCompletedProcesses++;
    }

    /**
     * next interval is +- 25% of avg interval
     */
    public long getNextIO() {
        // return 100000;
        return (long) ((1 - (Math.random() * 0.5)) * avgIoInterval + 1);
    }

    // Add more methods as needed
}