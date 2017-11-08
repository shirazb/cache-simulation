package sim;

import java.util.Objects;
import java.util.Random;

public class RequestEvent implements Comparable<RequestEvent> {

    private int itemNo;
    private Double time;
    private static Random rand = new Random();

    public RequestEvent(int k, double time) {
        this.itemNo = k;
        this.time = time;
    }

    public double getInterArrivalTime() {
        return Math.log(1-rand.nextDouble())/(-itemNo);
    }

    // itemNo is also the rate parameter in the distribution
    public int getItemNo() {
        return itemNo;
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
