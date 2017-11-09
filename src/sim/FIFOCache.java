package sim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FIFOCache implements Cache {

    private int indexToEvict = 0;
    private List<RequestEvent> cache;

    public FIFOCache(Collection<RequestEvent> initialCache) {
        this.cache = new ArrayList<>(initialCache);
    }

    @Override
    public boolean fetch(RequestEvent e) {
        boolean cacheHit = cache.contains(e);
        if (!cacheHit) {
            cache.set(indexToEvict, e);
            indexToEvict = (indexToEvict + 1) % cache.size();
        }
        return cacheHit;
    }
}
