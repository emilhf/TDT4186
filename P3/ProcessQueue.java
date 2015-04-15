public class ProcessQueue {
    private Queue<Process> queue;
    private Statistics statistics;

    public ProcessQueue(Queue<Process> queue, Statistics statistics) {
        this.queue = queue;
        this.statistics = statistics;
    }

    public Queue getQueue() {
        return queue;
    }

    public Process getNext() {
        return (Process) queue.getNext();
    }

    public Process removeNext() {
        return (Process) queue.removeNext();
    }

    public Process get(int i) {
        return (Process) queue.get(i);
    }

    public int getQueueLength() {
        return queue.getQueueLength();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void insertProcess(Process p) {
        queue.insert(p);
    }

    public void statisticizeRemaining() {
        for (int i = 0; i < getQueueLength(); i++) {
            statistics.logProcess(get(i));
        }
    }
}
