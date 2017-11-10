package sim;


public class SimResults {
    private int numItems;

    private double hitRatio;
    private double missRateByMissThroughput;
    private double missRateByHitRatioAndRates;

    public double getHitRatio() {
        return hitRatio;
    }

    public double getMissRateByMissThroughput() {
        return missRateByMissThroughput;
    }

    public double getMissRateByHitRatioAndRates() {
        return missRateByHitRatioAndRates;
    }

    @Override
    public String toString() {
        return this.hitRatio < 0 ?
                "{\n" +
                "    No events were simulated in the allowed time.\n" +
                "}"
            :
                "{\n" +
                "    Hit ratio: " + this.hitRatio + ",\n" +
                "    Miss rate by miss throughput: " + this.missRateByMissThroughput + ",\n" +
                "    Miss rate by hit ratio times sum of all rate parameters: " + this.missRateByHitRatioAndRates + ",\n" +
                "}";
    }

    /**
     * Constructs an object containing the results of the cache
     * simulation for `numItems` items. These results are:
     * `hitRatio`: The hit ratio, H / C.
     * `missRateByMissThroughput`: The miss rate calculated by M / T.
     * `missRateByHitRatioAndRates`: The miss rate calculate by (1 -
     * hitRatio) * (sum of rate parameter of each stored item).
     *
     * The fields are not safe to access until after `calculate()` has
     * been called.
     * @param numItems The number of items in the simulation. This is
     *                 used to calculate `missRateByHitRatioAndRates`.
     */
    SimResults(int numItems) {
        this.numItems = numItems;
    }

    /**
     * Calculates the results and sets their fields. Only after this call
     * can the fields by safely accessed.
     * @param hits number of hits observed.
     * @param totalReqs number of requests observed.
     * @return The results of the simulation.
     */
    public SimResults calculate(int hits, int totalReqs, double totalTime) {
        // If no events were simulated, write invalid results. toString()
        // will detect this.
        if (Double.compare(totalTime, 0) == 0) {
            this.hitRatio = -1;
            this.missRateByMissThroughput = -1;
            this.missRateByHitRatioAndRates = -1345435443;
            return this;
        }

        this.hitRatio = (double) hits / (double) totalReqs;
        this.missRateByMissThroughput = ((double) (totalReqs - hits)) / totalTime;

        double sumOfRates = 0;

        // Since for item k, 1 <= k <= numItems, their rate parameters for
        // inter-request time are 1 / k.
        for (int i = 0; i < numItems; i++) {
            sumOfRates += 1 / ((double) i + 1);
        }

        this.missRateByHitRatioAndRates = (1 - this.hitRatio) * sumOfRates;

        return this;
    }
}
