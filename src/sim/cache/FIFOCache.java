package sim.cache;

import sim.event.RequestEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FIFOCache implements Cache {

    private int indexToEvict = 0;
    private List<RequestEvent> cache;

    public FIFOCache(Collection<RequestEvent> initialCache) {
        this.cache = new ArrayList<>(initialCache);
    }

    /**
     * Simulates the lookup of `e.itemNo` in the cache. If there is a cache
     * miss, evicts an element according to a FIFO policy and places `e` in
     * the cache.
     * @param e Request element to lookup in the cache.
     * @return `lookUp(e)` <-> `e` was in the cache.
     */
    @Override
    public boolean fetch(RequestEvent e) {
        boolean cacheHit = cache.contains(e);

        // Implements FIFO by keeping track of the index of the last added
        // element, and filling up the cache from index 0 upwards. Thus, to
        // remove an event from the cache and insert e, simply set the
        // event at that index to be `e`.
        if (!cacheHit) {
            cache.set(indexToEvict, e);
            indexToEvict = (indexToEvict + 1) % cache.size();
        }

        return cacheHit;
    }

}
