package sim;

import java.util.Random;

public class RequestEvent implements Comparable<RequestEvent> {

    private int k;
    private Double time;
    private static Random rand = new Random();

    public RequestEvent(int k, double time) {
        this.k = k;
        this.time = time;
    }

    public double getInterArrivalTime() {
        return Math.log(1-rand.nextDouble())/(-k);
    }


    @Override
    public int compareTo(RequestEvent e) {
        return this.time.compareTo(e.time);
    }
}
