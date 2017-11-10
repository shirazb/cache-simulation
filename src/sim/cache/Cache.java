package sim.cache;

import sim.event.RequestEvent;

import java.util.Collection;

public interface Cache {

    // TODO: Rename to lookUp
    boolean fetch(RequestEvent e);
    void setCache(Collection<RequestEvent> initialCache);

}
