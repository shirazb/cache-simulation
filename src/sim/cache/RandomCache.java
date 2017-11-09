package sim.cache;

import sim.event.RequestEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomCache implements Cache {

    /*
    Should cache be array or some kind of tree? We need fast random lookup,
    insertion, and deletion.
     */
    private List<RequestEvent> cache;
    private Random generator = new Random();

    public RandomCache(Collection<RequestEvent> initialCache) {
        this.cache = new ArrayList<>(initialCache);
    }

    @Override
    public boolean fetch(RequestEvent e) {
        return false;
    }

}
