package sim.cache;

import sim.event.RequestEvent;

import java.util.Collection;

public interface Cache {

    // TODO: Rename to lookUp
    boolean fetch(RequestEvent e);

    static Cache withEvictionPolicy(EvictionPolicy policy,
                                    Collection<RequestEvent> events) {
        switch (policy) {
        case FIFO:
            return new FIFOCache(events);
        default:
            throw new UnsupportedOperationException("Not yet implemented " +
                    "Cache for policy: " + policy);
        }
    }

}
