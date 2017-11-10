package sim;

import org.junit.jupiter.api.Test;
import sim.cache.Cache;
import sim.cache.FIFOCache;
import sim.cache.RandomCache;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheSimulationTest {
    private static final double SIM_TIME = 50000;
    private static final double TOLERANCE = 0.05;

    // TODO: How to test hit ratio, or that miss rate is actually correct?
    @Test
    void FIFOCacheMissRateIsAccurate() {
        assertMissRatesSimilar(new FIFOCache(), 100, 1000);
        assertMissRatesSimilar(new FIFOCache(), 10, 10000);
    }

    @Test
    void randomCacheMissRateIsAccurate() {
        assertMissRatesSimilar(new RandomCache(), 100, 1000);
        assertMissRatesSimilar(new RandomCache(), 10, 10000);
    }

    private void assertMissRatesSimilar(Cache cacheType, int m, int n) {
        CacheSimulation fifoSim = new CacheSimulation(m, n, cacheType, SIM_TIME);
        SimResults results = fifoSim.simulate();
        double missRate1 = results.getMissRateByMissThroughput();
        double missRate2 = results.getMissRateByHitRatioAndRates();

        boolean isSimilar = Math.abs(missRate1 - missRate2) < TOLERANCE;
        String failMsg = "Simulation for FIFO, m = " + m + ", n = " + n +
                " has unacceptably different miss rates. By miss throughput: " +
                missRate2 + ", by hit ratio times sum of rates: " + missRate2 +
                ".";

        assertTrue(isSimilar, failMsg);
    }
}