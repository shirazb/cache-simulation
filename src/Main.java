import sim.CacheSimulation;
import sim.EvictionPolicy;

public class Main {
    public static void main(String[] args) {
        CacheSimulation fifoSim = new CacheSimulation(10, 1000, EvictionPolicy.FIFO, 10000);
        fifoSim.simulate();
    }
}
