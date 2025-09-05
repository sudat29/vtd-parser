public class PerformanceMetrics {
    private long startTime;
    private long endTime;
    private long totalParsingTime;
    private int totalElementsParsed;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
        totalParsingTime += (endTime - startTime);
    }

    public void incrementElementCount() {
        totalElementsParsed++;
    }

    public long getTotalParsingTime() {
        return totalParsingTime;
    }

    public int getTotalElementsParsed() {
        return totalElementsParsed;
    }

    public void reset() {
        totalParsingTime = 0;
        totalElementsParsed = 0;
    }

    @Override
    public String toString() {
        return "PerformanceMetrics{" +
                "totalParsingTime=" + totalParsingTime +
                " ns, totalElementsParsed=" + totalElementsParsed +
                '}';
    }
}