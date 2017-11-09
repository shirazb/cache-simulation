package sim.event;

import java.util.Random;

public class RequestEvent implements Comparable<RequestEvent> {

    private static Random rand = new Random();

    private int itemNo;
    private Double time;

    /**
     * Constructs a new RequestEvent. These events represent requests for
     * resource `itemNo` at a given time, whose inter-arrival time is
     * exponentially distributed with rate parameter `1 / itemNo`.
     * @param itemNo The number of the item being requested and the inverse
     *               of its rate parameter. Denoted `k` in the spec.
     */
    public RequestEvent(int itemNo) {
        this.itemNo = itemNo;
        this.time = getInterArrivalTime();
    }

    /**
     * Updates the time of the request event to be of the next request for
     * item `itemNo`.
     */
    public void scheduleNextRequest() {
        /*
            Since possible events consist of requests for a resource at a
            given time, and there is only one request for one resource
            scheduled at any time, we can reuse a single RequestEvent object
            for all the request events for one item. All that needs to be
            done is to update the time and reinsert it into the queue.
         */
        time += getInterArrivalTime();
    }

    public Double getTime() {
        return time;
    }

    @Override
    public int compareTo(RequestEvent e) {
        return this.time.compareTo(e.time);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RequestEvent && ((RequestEvent) obj).itemNo ==
                this.itemNo;
    }

    @Override
    public int hashCode() {
        return (int) (itemNo * 1021 + time * 331);
    }

    /**
     * Samples an exponential distribution with rate parameter `1 / itemNo`.
     */
    private double getInterArrivalTime() {
        return Math.log(1 - rand.nextDouble()) * -itemNo;
    }

}
