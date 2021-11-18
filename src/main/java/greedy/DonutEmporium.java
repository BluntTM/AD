package greedy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class DonutEmporium {
    public static void main(String[] args) {
        {
            assertApproximatelyEquals(
                    read("/greedy/donut_emporium/example.out").trim(),
                    run(getResourceAsStream("/greedy/donut_emporium/example.in")).trim()
            );
        }
        {
            assertApproximatelyEquals("6 8", run(new ByteArrayInputStream("3 1\n4 5\n5 10\n9 9".getBytes(StandardCharsets.UTF_8))).trim());
        }
    }

    // Implement the solve method to return the answer to the problem posed by the inputstream.
    public static String run(InputStream in) {
        return new DonutEmporium().solve(in);
    }

    public String solve(InputStream in) {
        int n, k;
        List<House> houses;
        List<Distance> distances;
        try (Scanner sc = new Scanner(in)) {
            n = sc.nextInt();
            k = sc.nextInt();
            houses = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                houses.add(new House(i, sc.nextInt(), sc.nextInt()));
            }

            int m = n * (n - 1) / 2;
            distances = new ArrayList<>(m);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    distances.add(new Distance(houses.get(i), houses.get(j)));
                }
            }
        }
        return applyKruskal(houses, distances, n, k);
    }

    public static String applyKruskal(List<House> houses, List<Distance> distances, int n, int k) {
        Collections.sort(distances, new Comparator<Distance>() {
            @Override
            public int compare(Distance d1, Distance d2) {
                return Long.compare(d1.distance, d2.distance);
            }
        });

        UnionFind unionFind = new UnionFind(houses);

        int edges = 0;
        int stop = n-k;
        for (Distance d : distances) {
            if (edges == stop) break;
            if (unionFind.join(d.a, d.b)) edges++;
        }

        StringBuilder sb = new StringBuilder();
        for (List<House> cluster : unionFind.clusters()) {
            sb.append(getCenter(cluster)).append("\n");
        }
        return sb.toString();
    }

    public static String getCenter(List<House> houses) {
        double x = 0;
        double y = 0;
        for (House h : houses) {
            x += h.x;
            y += h.y;
        }
        return x/houses.size() + " " + y/houses.size();
    }

    /**
     * Library
     */
    // Sorts the resulting stores by coordinate (so order doesn't matter) and checks whether it's within 1e-6 error
    private static void assertApproximatelyEquals(String expected, String actual) {
        String[] expectedStrings = expected.split("\n");
        String[] actualStrings = actual.split("\n");
        if (expectedStrings.length != actualStrings.length)
            throw new RuntimeException("Lengths of lists different! Expected=" + expectedStrings.length + ", Actual=" + actualStrings.length);
        Store[] expectedStores = Arrays.stream(expectedStrings).map(Store::new).sorted().toArray(Store[]::new);
        Store[] actualStores = Arrays.stream(actualStrings).map(Store::new).sorted().toArray(Store[]::new);
        for (int i = 0; i < actualStores.length; i++) {
            Store s1 = expectedStores[i], s2 = actualStores[i];
            assertEquals("x-coordinate of store " + i, s1.x, s2.x, s1.x * 1e-6);
            assertEquals("y-coordinate of store " + i, s1.y, s2.y, s1.y * 1e-6);
        }
    }

    static class House {

        int id, x, y;

        public House(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    static class Distance {

        House a, b;

        long distance;

        public Distance(House a, House b) {
            this.a = a;
            this.b = b;
            // Square Euclidean distance, to avoid floating-point errors
            this.distance = (long) (a.x - b.x) * (a.x - b.x) + (long) (a.y - b.y) * (a.y - b.y);
        }
    }

    static class UnionFind {

        private List<House> houses;

        private int[] parent;

        private int[] rank;

        UnionFind(List<House> houses) {
            this.houses = houses;
            int n = houses.size();
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        /**
         * Joins two disjoint sets together, if they are not already joined.
         * @return false if x and y are in same set, true if the sets of x and y are now joined.
         */
        boolean join(House x, House y) {
            int xrt = find(x.id);
            int yrt = find(y.id);
            if (rank[xrt] > rank[yrt])
                parent[yrt] = xrt;
            else if (rank[xrt] < rank[yrt])
                parent[xrt] = yrt;
            else if (xrt != yrt)
                rank[parent[yrt] = xrt]++;
            return xrt != yrt;
        }

        /**
         * @return The house that is indicated as the "root" of the set of house h.
         */
        House find(House h) {
            return houses.get(find(h.id));
        }

        private int find(int x) {
            return parent[x] == x ? x : (parent[x] = find(parent[x]));
        }

        /**
         * @return All clusters of houses
         */
        Collection<List<House>> clusters() {
            Map<Integer, List<House>> map = new HashMap<>();
            for (int i = 0; i < parent.length; i++) {
                int root = find(i);
                if (!map.containsKey(root))
                    map.put(root, new ArrayList<>());
                map.get(root).add(houses.get(i));
            }
            return map.values();
        }
    }

    static class Store implements Comparable<Store> {

        double x, y;

        public Store(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Store(String s) {
            String[] split = s.split(" ");
            x = Double.parseDouble(split[0]);
            y = Double.parseDouble(split[1]);
        }

        @Override
        public int compareTo(Store store) {
            return Comparator.<Store>comparingDouble(s -> s.x).thenComparingDouble(s -> s.y).compare(this, store);
        }
    }
}
