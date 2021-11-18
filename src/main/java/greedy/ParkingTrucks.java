package greedy;

import static utils.Assertions.assertEquals;

public class ParkingTrucks {
    public static void main(String[] args) {
        int n = 4;
        int[] weights = { 0, 41, 29, 12, 26 };
        int maxWeight = 48;
        assertEquals(3, minAmountOfTrucks(n, weights, maxWeight));
    }


    /**
     * @param n the number of packages
     * @param weights the weights of all packages 1 through n. Note that weights[0] should be ignored!
     * @param maxWeight the maximum weight a truck can carry
     * @return the minimal number of trucks required to ship the packages _in the given order_.
     */
    public static int minAmountOfTrucks(int n, int[] weights, int maxWeight) {
        int trucks = 0;
        int lastTruckWeight = 0;
        for (int i = 1; i <= n; i++) {
            int weight = weights[i];
            if (lastTruckWeight + weight <= maxWeight) {
                lastTruckWeight += weight;
            } else {
                trucks++;
                lastTruckWeight = weight;
            }
        }
        return trucks + (lastTruckWeight == 0 ? 0 : 1);
    }
}
