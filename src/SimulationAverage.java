import sim.CacheSimulation;
import sim.SimResults;
import sim.cache.Cache;
import sim.cache.FIFOCache;
import sim.cache.RandomCache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class SimulationAverage {

    private static final double CRITICAL_VALUE = 1.96;
    private static final int NUM_SAMPLES = 200;
    private static final int SIM_TIME = 20000;

    public static void main(String[] args) throws Exception {
        final int N = 1000;

        System.out.println("Virtual simulation time: " + SIM_TIME);
        System.out.println("Sample size: " + NUM_SAMPLES);
        System.out.println("Critical Value: z = " + CRITICAL_VALUE);

        simulate(10, N, FIFOCache::makeCache);
        simulate(50, N, FIFOCache::makeCache);
        simulate(100, N, FIFOCache::makeCache);
        simulate(10, N, RandomCache::makeCache);
        simulate(50, N, RandomCache::makeCache);
        simulate(100, N, RandomCache::makeCache);
    }

    private static void simulate(int m, int n, Supplier<? extends Cache> cacheSupplier) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        List<Future<SimResults>> futureResults = new LinkedList<>();

        for (int i = 0; i < NUM_SAMPLES; i++) {
            CacheSimulation sim = new CacheSimulation(m, n, cacheSupplier.get(), SIM_TIME);
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

        double hitMean = results.stream().mapToDouble(SimResults::getHitRatio).sum() / NUM_SAMPLES;
        double missThroughputMean = results.stream().mapToDouble(SimResults::getMissRateByMissThroughput).sum() / NUM_SAMPLES;

        double hitSD = calculateStandardDeviation(results, hitMean, SimResults::getHitRatio);

        double missThroughputSD = calculateStandardDeviation(
                results,
                missThroughputMean,
                SimResults::getMissRateByMissThroughput
        );

        final double hitIntervalOffset = calculateIntervalOffset(hitSD);
        final double missIntervalOffset = calculateIntervalOffset(missThroughputSD);
        String outputFIFO = new StringBuilder()
                .append("----------------------------------------------------------------")
                .append(System.lineSeparator())

                .append("M = " + m)
                .append(", N = " + n)
                .append(", Eviction Policy: " + cacheSupplier.get().policyType())
                .append(System.lineSeparator())

                .append("Hit ratio mean: " + hitMean)
                .append(System.lineSeparator())

                .append("Hit ratio 95% CI: [" +
                        (hitMean - hitIntervalOffset) + ", " +
                        (hitMean + hitIntervalOffset) + "]"
                )
                .append(System.lineSeparator())

                .append("Miss throughput mean: " + missThroughputMean)
                .append(System.lineSeparator())

                .append("Miss throughput 95% CI: [" +
                        (missThroughputMean - missIntervalOffset) + ", " +
                        (missThroughputMean + missIntervalOffset) + "]"
                )
                .append(System.lineSeparator())
                .append("----------------------------------------------------------------")

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

    private static double calculateIntervalOffset(double sd) {
        return CRITICAL_VALUE * sd / Math.sqrt(NUM_SAMPLES);
    }
}
