package sim;

import java.util.ArrayList;
import java.util.List;

public class FIFOCache implements Cache {

    private int indexToEvict = 0;
    private List<RequestEvent> cache;

    public FIFOCache(int size) {
        this.cache = new ArrayList<>(size);
    }

    @Override
    public boolean checkCache(RequestEvent e) {
        boolean cacheHit = cache.contains(e);
        if (!cacheHit) {
            cache.set(indexToEvict, e);
            indexToEvict = (indexToEvict + 1) % cache.size();
        }
        return cacheHit;
    }
}
