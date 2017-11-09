import sim.CacheSimulation;
import sim.CacheSimulation.Results;
import sim.cache.EvictionPolicy;

public class Main {
    public static void main(String[] args) {
        CacheSimulation fifoSim = new CacheSimulation(10, 1000,
                EvictionPolicy.FIFO, 50000);
        Results results = fifoSim.simulate();
        System.out.println(results);
    }
}
