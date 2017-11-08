package sim;

import org.junit.jupiter.api.Test;

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
}