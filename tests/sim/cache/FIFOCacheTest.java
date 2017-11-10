package sim.cache;

import org.junit.jupiter.api.Test;
import sim.cache.FIFOCache;
import sim.event.RequestEvent;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FIFOCacheTest {

    private final RequestEvent e1 = new RequestEvent(1);
    private final RequestEvent e2 = new RequestEvent(2);
    private final RequestEvent e3 = new RequestEvent(3);
    private final RequestEvent e4 = new RequestEvent(4);

    @Test
    void cacheIsInitialised() {

        Collection<RequestEvent> events = Arrays.asList(e1, e2, e3);
        FIFOCache cache = new FIFOCache(events);

        assertTrue(cache.fetch(e1));
        assertTrue(cache.fetch(e2));
        assertTrue(cache.fetch(e3));
    }

    @Test
    void cacheIsInitialisedInOrder() {

        Collection<RequestEvent> events = Arrays.asList(e1, e2);
        FIFOCache cache = new FIFOCache(events);


        assertFalse(cache.fetch(e3));
        assertTrue(cache.fetch(e2));
        assertFalse(cache.fetch(e1));
    }

    @Test
    void cacheEvictsOnMiss() {

        Collection<RequestEvent> events = Arrays.asList(e1, e2);
        FIFOCache cache = new FIFOCache(events);

        assertFalse(cache.fetch(e3));
        assertFalse(cache.fetch(e1));
    }

    @Test
    void cacheEvictionIsFIFO() {

        Collection<RequestEvent> events = Arrays.asList(e1, e2, e3);
        FIFOCache cache = new FIFOCache(events);

        assertFalse(cache.fetch(e4));
        assertFalse(cache.fetch(e1));
        assertFalse(cache.fetch(e2));
        assertFalse(cache.fetch(e3));

        assertTrue(cache.fetch(e1));
        assertTrue(cache.fetch(e2));
        assertTrue(cache.fetch(e3));
    }


}