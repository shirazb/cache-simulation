package sim.cache;

import org.junit.jupiter.api.Test;
import sim.event.RequestEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RandomCacheTest {

    private final RequestEvent e1 = new RequestEvent(1);
    private final RequestEvent e2 = new RequestEvent(2);
    private final RequestEvent e3 = new RequestEvent(3);
    private final RequestEvent e4 = new RequestEvent(4);
    private final RandomCache cache = new RandomCache();

    @Test
    void cacheIsInitialised() {

        Collection<RequestEvent> events = Arrays.asList(e1, e2, e3);
        cache.setCache(events);

        assertTrue(cache.fetch(e1));
        assertTrue(cache.fetch(e2));
        assertTrue(cache.fetch(e3));
    }


    @Test
    void cacheEvictsOnMiss() {

        Collection<RequestEvent> events = Collections.singletonList(e1);
        cache.setCache(events);

        assertFalse(cache.fetch(e2));
        assertFalse(cache.fetch(e1));
    }

    @Test
    void cacheEvictionIsRandomAndValid() {

        Collection<RequestEvent> events = Arrays.asList(e1, e2, e3);
        cache.setCache(events);

        for (int i = 0; i < 10000; i++) {
            int evictIndex = cache.evictIndex();
            assertTrue(evictIndex >= 0 && evictIndex < events.size());
        }
    }

}