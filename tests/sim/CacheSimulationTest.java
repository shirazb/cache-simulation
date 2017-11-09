package sim;

import org.junit.jupiter.api.Test;
import sim.cache.EvictionPolicy;

import static org.junit.jupiter.api.Assertions.*;

class CacheSimulationTest {
    private static final double SIM_TIME = 50000;
    private static final double TOLERANCE = 0.05;

    // TODO: How to test hit ratio, or that miss rate is actually correct?
    @Test
    void testSimulate() {
        // Test FIFO, m = 10, n = 1000
        assertMissRatesSimilar(EvictionPolicy.FIFO, 10, 1000);

        // Test FIFO, m = 50, n = 1000
        assertMissRatesSimilar(EvictionPolicy.FIFO, 50, 1000);

        // Test FIFO, m = 100, n = 1000
        assertMissRatesSimilar(EvictionPolicy.FIFO, 100, 1000);

        // TODO: Test RAND, m = 10, n = 1000

        // TODO: Test RAND, m = 50, n = 1000

        // TODO: Test RAND, m = 100, n = 1000
    }

    private void assertMissRatesSimilar(EvictionPolicy policy, int m, int n) {
        CacheSimulation fifoSim = new CacheSimulation(m, n, policy, SIM_TIME);
        CacheSimulation.Results results = fifoSim.simulate();
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