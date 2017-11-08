package sim;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FIFOCacheTest {
    @Test
    void cacheIsInitialised() {
        RequestEvent e1 = new RequestEvent(1);
        RequestEvent e2 = new RequestEvent(2);
        RequestEvent e3 = new RequestEvent(3);

        Collection<RequestEvent> events = Arrays.asList(e1, e2, e3);
        FIFOCache cache = new FIFOCache(events);

        assertTrue(cache.checkCache(e1));
        assertTrue(cache.checkCache(e2));
        assertTrue(cache.checkCache(e3));
    }

    @Test
    void cacheIsInitialisedInOrder() {
        RequestEvent e1 = new RequestEvent(1);
        RequestEvent e2 = new RequestEvent(2);
        RequestEvent e3 = new RequestEvent(3);

        Collection<RequestEvent> events = Arrays.asList(e1, e2);
        FIFOCache cache = new FIFOCache(events);


        assertFalse(cache.checkCache(e3));
        assertTrue(cache.checkCache(e2));
        assertFalse(cache.checkCache(e1));
    }

    @Test
    void cacheEvictsOnMiss() {
        RequestEvent e1 = new RequestEvent(1);
        RequestEvent e2 = new RequestEvent(2);
        RequestEvent e3 = new RequestEvent(3);

        Collection<RequestEvent> events = Arrays.asList(e1, e2);
        FIFOCache cache = new FIFOCache(events);

        assertFalse(cache.checkCache(e3));
        assertFalse(cache.checkCache(e1));
    }

    @Test
    void cacheEvictionIsFIFO() {
        RequestEvent e1 = new RequestEvent(1);
        RequestEvent e2 = new RequestEvent(2);
        RequestEvent e3 = new RequestEvent(3);
        RequestEvent e4 = new RequestEvent(4);

        Collection<RequestEvent> events = Arrays.asList(e1, e2, e3);
        FIFOCache cache = new FIFOCache(events);

        assertFalse(cache.checkCache(e4));
        assertFalse(cache.checkCache(e1));
        assertFalse(cache.checkCache(e2));
        assertFalse(cache.checkCache(e3));

        assertTrue(cache.checkCache(e1));
        assertTrue(cache.checkCache(e2));
        assertTrue(cache.checkCache(e3));
    }


}