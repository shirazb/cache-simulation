package sim.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestEventTest {

    /**
     * Test soundness of inter arrival distribution by testing the mean of
     * many results is close to the expected mean.
     */
    @Test
    void interArrivalTimeIsRealistic() {
        int lambda = 4;
        int numSamples = 100000;

        RequestEvent event = new RequestEvent(lambda);

        for (int i = 0; i < numSamples; i++) {
            event.scheduleNextRequest();
        }

        assertTrue(Math.abs(event.getTime() / numSamples - 4) < 0.05);
    }

}