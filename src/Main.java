import sim.CacheSimulation;
import sim.cache.Cache;
import sim.cache.FIFOCache;
import sim.cache.RandomCache;

public class Main {
    private static final double SIM_TIME = 50000;
    public static void main(String[] args) {

        // FIFO
        performSimulationAndPrintResults(10, 1000, new FIFOCache());
        performSimulationAndPrintResults(50, 1000, new FIFOCache());
        performSimulationAndPrintResults(100, 1000, new FIFOCache());

        // Random
        performSimulationAndPrintResults(10, 1000, new RandomCache());
        performSimulationAndPrintResults(50, 1000, new RandomCache());
        performSimulationAndPrintResults(100, 1000, new RandomCache());

    }

    private static void performSimulationAndPrintResults(
            int cacheSize,
            int storageSize,
            Cache cacheType)
    {
        CacheSimulation sim = new CacheSimulation(
                cacheSize,
                storageSize,
                cacheType,
                SIM_TIME
        );

        System.out.println(
                "-------- policy = " +
                cacheType.policyType() +
                " m = " +
                cacheSize +
                " n = " +
                storageSize +
                " --------"
        );
        System.out.println(sim.simulate());
        System.out.println();
    }
}
