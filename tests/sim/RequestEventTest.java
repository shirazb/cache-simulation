package sim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestEventTest {

    @Test
    void interArrivalTimeIsRealistic() {
        RequestEvent event = new RequestEvent(4);

        int numSamples = 100000;

        for (int i = 0; i < numSamples; i++) {
            event.scheduleNewRequest();
        }

//        System.out.println(event.getTime()/numSamples);
        assertTrue(Math.abs(event.getTime()/numSamples - 4) < 0.05);
    }

}