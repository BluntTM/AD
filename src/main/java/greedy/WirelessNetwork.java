package greedy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class WirelessNetwork {
    public static void main(String[] args) {
        assertEquals(
                read("/greedy/wireless_network/example.out").trim(),
                run(getResourceAsStream("/greedy/wireless_network/example.in")).trim()
        );
    }

    // Implement the solve method to return the answer to the problem posed by the inputstream.
    public static String run(InputStream in) {
        return new WirelessNetwork().solve(in);
    }

    public String solve(InputStream in) {
        int n, m;
        long B;
        List<Edge> edges;

        try (Scanner sc = new Scanner(in)) {
            n = sc.nextInt();
            m = sc.nextInt();
            B = sc.nextLong();
            edges = new ArrayList<>(m);
            for (int i = 0; i < m; i++) {
                edges.add(new Edge(sc));
            }
        }

        return solve(edges, n, m, B);
    }

    public static String solve(List<Edge> edges, int n, int m, long B) {
        Collections.sort(edges);

        UnionFind uf = new UnionFind(n);
        int affordableEdges = 0;
        int totalCost = 0;
        for (Edge e : edges) {
            if (!uf.union(e)) continue;

            B -= e.c;
            if (B >= 0) affordableEdges++;

            totalCost += e.c;
        }
        return totalCost + " " + affordableEdges;
    }

    /**
     * Library
     */
    interface Weight {
        int getWeight();
    }

    static class Edge implements Weight, Comparable<Edge> {
        int u;
        int v;
        int c;

        public Edge(Scanner sc) {
            this.u = sc.nextInt();
            this.v = sc.nextInt();
            this.c = sc.nextInt();
        }

        @Override
        public int getWeight() {
            return c;
        }

        @Override
        public int compareTo(Edge that) {
            return Integer.compare(c, that.c);
        }
    }

    static class UnionFind {

        private int[] parent;
        private int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public boolean union(Edge e) {
            int uRoot = find(e.u);
            int vRoot = find(e.v);
            if (rank[uRoot] > rank[vRoot]) {
                parent[vRoot] = uRoot;
            } else if (rank[uRoot] < rank[vRoot]) {
                parent[uRoot] = vRoot;
            } else if (uRoot != vRoot) {
                rank[parent[vRoot] = uRoot]++;
            }
            return uRoot != vRoot;
        }

        public int find(int x) {
            return parent[x] == x ? x : (parent[x] = find(parent[x]));
        }
    }
}
