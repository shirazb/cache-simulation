package sim;

import java.util.Objects;
import java.util.Random;

public class RequestEvent implements Comparable<RequestEvent> {

    private int itemNo;
    private Double time;
    private static Random rand = new Random();

    public RequestEvent(int itemNo) {
        this.itemNo = itemNo;
        this.time = getInterArrivalTime();
    }

    private double getInterArrivalTime() {
        return Math.log(1-rand.nextDouble()) * (-itemNo);
    }

    /**
     * Updates the time of the request event to be of the next request for
     * item `itemNo`.
     */
    public void scheduleNewRequest() {
        /*
            Since possible events consist of requests for a resource at a
            given time, and there is only one request for one resource
            scheduled at any time, we can reuse a single RequestEvent object
            for all the request events for one item. All that needs to be
            done is to update the time and reinsert it into the queue.
         */
        time += getInterArrivalTime();
    }

    // itemNo is also the rate parameter in the distribution
    public int getItemNo() {
        return itemNo;
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
        return obj instanceof RequestEvent && ((RequestEvent) obj).itemNo == this.itemNo;
    }

    @Override
    public int hashCode() {
        return (int) (itemNo * 1021 + time * 331);
    }

}
