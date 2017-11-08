package sim;

import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class CacheSimulation {

    private double simTime;
    private Cache cache;
    private PriorityQueue<RequestEvent> eventQueue = new PriorityQueue<>();

    public CacheSimulation(int cacheSize, int storageSize, EvictionPolicy policy, double simTime) {

        this.simTime = simTime;

        Collection<RequestEvent> toCache = new LinkedList<>();

        for (int i = 0; i < storageSize; i++) {
            RequestEvent e = new RequestEvent(i + 1);
            eventQueue.add(e);
            if (i < cacheSize) {
                toCache.add(e);
            }
        }


        switch (policy) {

            case FIFO:
                this.cache = new FIFOCache(toCache);
                break;

            case RAND:
                assert false;
                break;
        }

    }

    public void simulate() {

        int hits = 0;
        int total = 0;

        RequestEvent e = eventQueue.poll();
        while (e != null && e.getTime() < simTime) {
            if(cache.checkCache(e)) {
                hits++;
            }
            e.scheduleNewRequest();
            e = eventQueue.poll();
            total++;
        }

        System.out.println("hits = " + hits);
        System.out.println("total = " + total);
        System.out.println("hit ratio = " + hits/total);
        System.out.println("miss rate = " + (total - hits)/simTime);

    }
}
