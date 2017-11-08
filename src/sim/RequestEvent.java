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
        return Math.log(1-rand.nextDouble())/(-itemNo);
    }

    public void scheduleNewRequest() {
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
        return Objects.hashCode(this);
    }
}
