package sim;

import sim.cache.Cache;
import sim.cache.EvictionPolicy;
import sim.event.RequestEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class CacheSimulation {

    private double simTime;
    private Cache cache;
    private PriorityQueue<RequestEvent> eventQueue = new PriorityQueue<>();
    private Results results;

    /**
     * Constructs a simulation of a cache that measures the hit ratio and the
     * miss rate.
     * @param cacheSize Number items in the cache, denoted as 'm' in the spec.
     * @param storageSize Total number items stored, denoted as 'n' in the spec.
     * @param policy The eviction policy, FIFO or RAND.
     * @param simTime The virtual time limit of the simulation. This should
     *                be large enough to allow all measurements to stabilise.
     */
    // TODO: How to cope with initialisation period?
    public CacheSimulation(final int cacheSize, final int storageSize,
                           EvictionPolicy policy, final double simTime) {

        if (cacheSize > storageSize || simTime < 0) {
            throw new IllegalArgumentException("Invalid parameters for " +
                    "simulation");
        }

        this.simTime = simTime;
        this.results = new Results(storageSize);

        // List of events to be cached. These are the first `cacheSize` events.
        List<RequestEvent> toCache = new LinkedList<>();

        // First `cacheSize` events are added to queue and inserted into
        // cache, in order 1..`cacheSize`.
        for (int i = 0; i < cacheSize; i++) {
            RequestEvent e = new RequestEvent(i + 1);
            eventQueue.add(e);

            // Adds to end of linked list in O(1) time.
            toCache.add(e);
        }

        // Remaining events are added to event queue, but not the cache.
        for (int i = cacheSize; i < storageSize; i++) {
            RequestEvent e = new RequestEvent(i + 1);
            eventQueue.add(e);
        }


        // Construct cache with eviction policy described by `policy`.
        this.cache = Cache.withEvictionPolicy(policy, toCache);
    }

    /**
     * Performs the simulation, measuring hit ratio and miss rate.
     */
    public Results simulate() {
        int hits = 0;
        int total = 0;

        RequestEvent e = eventQueue.poll();
        double now = 0;

        while (e != null && e.getTime() <= simTime) {
            now = e.getTime();

            // Check if e in cache, evicting an event and placing e into the
            // cache if necessary.
            if (cache.fetch(e)) {
                hits++;
            }

            // Update e's time to be of the next request for it, then re-insert
            // into queue.
            e.scheduleNextRequest();
            eventQueue.add(e);

            total++;
            e = eventQueue.poll();
        }

        return results.calculate(hits, total, now);
    }

    // Literally made this an inner class to see if I can still remember how
    // to use inner classes...
    public class Results {
        private int numItems;

        private double hitRatio;
        private double missRateByMissThroughput;
        private double missRateByHitRatioAndRates;

        public double getHitRatio() {
            return hitRatio;
        }

        public double getMissRateByMissThroughput() {
            return missRateByMissThroughput;
        }

        public double getMissRateByHitRatioAndRates() {
            return missRateByHitRatioAndRates;
        }

        @Override
        public String toString() {
            return this.hitRate < 0 ?
                    "{\n" +
                    "    No events were simulated in the allowed time.\n" +
                    "}"
                :
                    "{\n" +
                    "    Hit ratio: " + this.hitRatio + ",\n" +
                    "    Miss rate by miss throughput: " + this.missRateByMissThroughput + ",\n" +
                    "    Miss rate by hit ratio times sum of all rate parameters: " + this.missRateByHitRatioAndRates + ",\n" +
                    "}";
        }

        /**
         * Constructs an object containing the results of the cache
         * simulation for `numItems` items. These results are:
         * `hitRatio`: The hit ratio, H / C.
         * `missRateByMissThroughput`: The miss rate calculated by M / T.
         * `missRateByHitRatioAndRates`: The miss rate calculate by (1 -
         * hitRatio) * (sum of rate parameter of each stored item).
         *
         * The fields are not safe to access until after `calculate()` has
         * been called.
         * @param numItems The number of items in the simulation. This is
         *                 used to calculate `missRateByHitRatioAndRates`.
         */
        private Results(int numItems) {
            this.numItems = numItems;
        }

        /**
         * Calculates the results and sets their fields. Only after this call
         * can the fields by safely accessed.
         * @param hits number of hits observed.
         * @param totalReqs number of requests observed.
         * @return The results of the simulation.
         */
        private Results calculate(int hits, int totalReqs, double totalTime) {
            // If no events were simulated, write invalid results. toString()
            // will detect this.
            if (Double.compare(totalTime, 0) == 0) {
                this.hitRatio = -1;
                this.missRateByMissThroughput = -1;
                this.missRateByHitRatioAndRates = -1345435443;
                return this;
            }

            this.hitRatio = (double) hits / (double) totalReqs;
            this.missRateByMissThroughput = ((double) (totalReqs - hits)) / totalTime;

            double sumOfRates = 0;

            // Since for item k, 1 <= k <= numItems, their rate parameters for
            // inter-request time are 1 / k.
            for (int i = 0; i < numItems; i++) {
                sumOfRates += 1 / ((double) i + 1);
            }

            this.missRateByHitRatioAndRates = (1 - this.hitRatio) * sumOfRates;

            return this;
        }
    }

}
