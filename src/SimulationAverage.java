import sim.CacheSimulation;
import sim.SimResults;
import sim.cache.FIFOCache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class SimulationAverage {
    public static void main(String[] args) throws Exception {

        final int simTime = 10000;
        final int numSamples = 50;
        final int M = 100;
        final int N = 1000;


        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        List<Future<SimResults>> futureResults = new LinkedList<>();

        for (int i = 0; i < numSamples; i++) {
            CacheSimulation sim = new CacheSimulation(M, N, new FIFOCache(), simTime);
            futureResults.add(executor.submit(sim::simulate));
        }

        executor.shutdown();

        List<SimResults> results = new LinkedList<>();
        futureResults.forEach(future -> {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        double hitAverage = results.stream().mapToDouble(SimResults::getHitRatio).sum() / numSamples;
        double missThroughputAverage = results.stream().mapToDouble(SimResults::getMissRateByMissThroughput).sum() / numSamples;

        String outputFIFO = new StringBuilder()
                .append("Virtual simulation time: " + simTime)
                .append(System.lineSeparator())
                .append("Sample size: " + numSamples)
                .append(System.lineSeparator())
                .append("N: " + N)
                .append(System.lineSeparator())
                .append("M: " + M)
                .append(System.lineSeparator())
                .append("Hit average: " + hitAverage)
                .append(System.lineSeparator())
                .append("Miss throughput average: " + missThroughputAverage)
                .toString();
        System.out.println(outputFIFO);
    }
}
