import sim.CacheSimulation;
import sim.cache.EvictionPolicy;

public class Main {
    private static final double SIM_TIME = 50000;
    public static void main(String[] args) {
        performSimulationAndPrintResults(
                "policy = FIFO, m = 10, n = 1000",
                10,
                1000,
                EvictionPolicy.FIFO
        );
        performSimulationAndPrintResults(
                "policy = FIFO, m = 50, n = 1000",
                50,
                1000,
                EvictionPolicy.FIFO
        );
        performSimulationAndPrintResults(
                "policy = FIFO, m = 100, n = 1000",
                100,
                1000,
                EvictionPolicy.FIFO
        );

        // TODO: RAND simulations.
    }

    private static void performSimulationAndPrintResults(
            String desc,
            int cacheSize,
            int storageSize,
            EvictionPolicy policy)
    {
        CacheSimulation sim = new CacheSimulation(
                cacheSize,
                storageSize,
                policy,
                SIM_TIME
        );

        System.out.println("-------- " + desc + " --------");
        System.out.println(sim.simulate());
        System.out.println();
    }
}
