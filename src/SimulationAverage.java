import sim.CacheSimulation;
import sim.SimResults;
import sim.cache.FIFOCache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.ToDoubleFunction;

public class SimulationAverage {


    private static final int NUM_SAMPLES = 12;
    private static final int SIM_TIME = 10000;

    public static void main(String[] args) throws Exception {

        final int M = 100;
        final int N = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        List<Future<SimResults>> futureResults = new LinkedList<>();

        for (int i = 0; i < NUM_SAMPLES; i++) {
            CacheSimulation sim = new CacheSimulation(M, N, new FIFOCache(), SIM_TIME);
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

        double hitAverage = results.stream().mapToDouble(SimResults::getHitRatio).sum() / NUM_SAMPLES;
        double missThroughputAverage = results.stream().mapToDouble(SimResults::getMissRateByMissThroughput).sum() / NUM_SAMPLES;

        double hitSD = calculateStandardDeviation(results, hitAverage, SimResults::getHitRatio);

        double missThroughputSD = calculateStandardDeviation(
                results,
                missThroughputAverage,
                SimResults::getMissRateByMissThroughput
        );

        String outputFIFO = new StringBuilder()
                .append("Virtual simulation time: " + SIM_TIME)
                .append(System.lineSeparator())
                .append("Sample size: " + NUM_SAMPLES)
                .append(System.lineSeparator())
                .append("N: " + N)
                .append(System.lineSeparator())
                .append("M: " + M)
                .append(System.lineSeparator())
                .append("Hit average: " + hitAverage)
                .append(System.lineSeparator())
                .append("Hit SD: " + hitSD)
                .append(System.lineSeparator())
                .append("Miss throughput average: " + missThroughputAverage)
                .append(System.lineSeparator())
                .append("Miss throughput SD: " + missThroughputSD)
                .toString();
        System.out.println(outputFIFO);

    }

    private static double calculateStandardDeviation(List<SimResults> results, double missThroughputAverage, ToDoubleFunction<SimResults> resultFunc) {
        final double squareMean = results.stream()
                .mapToDouble(resultFunc)
                .map(miss -> Math.pow(miss, 2))
                .sum()
                / NUM_SAMPLES;
        final double variance = (squareMean - Math.pow(missThroughputAverage, 2)) * (NUM_SAMPLES / (NUM_SAMPLES - 1));
        return Math.sqrt(variance);
    }
}
