package sim;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class CacheSimulation {

    private double simTime;
    private Cache cache;
    private PriorityQueue<RequestEvent> eventQueue = new PriorityQueue<>();

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

        this.simTime = simTime;

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
    public void simulate() {
        double hits = 0;
        double total = 0;

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
            e.scheduleNewRequest();
            eventQueue.add(e);

            total++;
            e = eventQueue.poll();
        }

        System.out.println("hits = " + hits);
        System.out.println("total = " + total);
        System.out.println("hit ratio = " + (hits / total));
        System.out.println("miss rate = " + (total - hits) / now);

        double sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += 1 / (i + 1.0);
        }

        System.out.println("miss rate 2 = " + (1 - (hits / total)) * sum);


    }
}
