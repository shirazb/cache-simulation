import sim.CacheSimulation;
import sim.cache.Cache;
import sim.cache.EvictionPolicy;
import sim.cache.FIFOCache;

public class Main {
    private static final double SIM_TIME = 50000;
    public static void main(String[] args) {
        performSimulationAndPrintResults(
                "policy = FIFO, m = 10, n = 1000",
                10,
                1000,
                new FIFOCache()
        );
        performSimulationAndPrintResults(
                "policy = FIFO, m = 50, n = 1000",
                50,
                1000,
                new FIFOCache()
        );
        performSimulationAndPrintResults(
                "policy = FIFO, m = 100, n = 1000",
                100,
                1000,
                new FIFOCache()
        );

        // TODO: RAND simulations.
    }

    // TODO: wtf why am I passing in the desc when it can be generated from the
    // args.
    private static void performSimulationAndPrintResults(
            String desc,
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

        System.out.println("-------- " + desc + " --------");
        System.out.println(sim.simulate());
        System.out.println();
    }
}
