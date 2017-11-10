package sim.cache;

import sim.event.RequestEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomCache implements Cache {

    public static RandomCache makeCache() {
        return new RandomCache();
    }


    private List<RequestEvent> cache;
    private Random generator = new Random();


    @Override
    public boolean fetch(RequestEvent e) {
        boolean cacheHit = cache.contains(e);
        if (!cacheHit) {
            cache.set(evictIndex(), e);
        }
        return cacheHit;
    }

    @Override
    public String policyType() {
        return "Random";
    }

    @Override
    public void setCache(Collection<RequestEvent> initialCache) {
        this.cache = new ArrayList<>(initialCache);
    }

    int evictIndex() {
        return generator.nextInt(cache.size());
    }

}
