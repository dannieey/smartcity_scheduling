package common;

public class Metrics {
    public long dfsVisits = 0;
    public long dfsEdges = 0;
    public long kahnPushes = 0;
    public long kahnPops = 0;
    public long relaxations = 0;
    public long startTime;
    public long endTime;

    public void startTimer() { startTime = System.nanoTime(); }
    public void endTimer() { endTime = System.nanoTime(); }
    public long getElapsedMillis() { return (endTime - startTime) / 1_000_000; }

    @Override
    public String toString() {
        return String.format("dfsVisits=%d, dfsEdges=%d, kahnPushes=%d, kahnPops=%d, relaxations=%d, timeMs=%d",
                dfsVisits, dfsEdges, kahnPushes, kahnPops, relaxations, getElapsedMillis());
    }
}
