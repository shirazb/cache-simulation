package sim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestEventTest {

    @Test
    void interArrivalTimeIsRealistic() {
        RequestEvent event = new RequestEvent(1, 0);

        double sum = 0;
        int numSamples = 100000;

        for (int i = 0; i < numSamples; i++) {
            sum += event.getInterArrivalTime();
        }

        assertTrue(Math.abs(sum/numSamples - 1) < 0.05);
    }

    @Test
    void compareTimes() {
        RequestEvent e1 = new RequestEvent(1, 0);
        RequestEvent e2 = new RequestEvent(1, 0);
        RequestEvent e3 = new RequestEvent(1, 1);

        assertTrue(e1.compareTo(e2) == 0);
        assertTrue(e1.compareTo(e3) < 0);
        assertTrue(e3.compareTo(e1) > 0);

    }
}