package greedy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class GettingOutFastest {
    public static void main(String[] args) {
        assertEquals(
                read("/greedy/getting_out_fastest/example.out").trim(),
                run(getResourceAsStream("/greedy/getting_out_fastest/example.in")).trim()
        );
    }

    // Implement the solve method to return the answer to the problem posed by the inputstream.
    public static String run(InputStream in) {
        return new GettingOutFastest().solve(in);
    }

    public String solve(InputStream in) {
        List<Map<Integer, Integer>> nodes;
        int n,m,s,t;

        try (Scanner sc = new Scanner(in)) {
            /*
             * We already parse the input for you and should not need to make changes to this part of the code.
             * You are free to change this input format however.
             */
            n = sc.nextInt();
            m = sc.nextInt();
            s = sc.nextInt();
            t = sc.nextInt();

            nodes = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) {
                nodes.add(new HashMap<>());
            }
            for (int i = 0; i < m; i++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                int cost = sc.nextInt();
                nodes.get(u).put(v, cost);
            }
        }

        return shortestDistance(nodes, s, t);
    }

    public static String shortestDistance(List<Map<Integer, Integer>> nodes, int s, int t) {
        int[] distances = new int[nodes.size()];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[s] = 0;

        // ghetto adaptable priorityqueue
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(s, 0));

        boolean[] marked = new boolean[nodes.size()];
        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (marked[node.i]) continue;
            marked[node.i] = true;

            if (node.i == t) break;

            Map<Integer, Integer> outgoingEdges = nodes.get(node.i);
            for (Map.Entry<Integer, Integer> edge : outgoingEdges.entrySet()) {
                int childIndex = edge.getKey();
                int weight = edge.getValue();
                if (marked[childIndex]) continue;

                // edge relaxation
                int d = distances[node.i] + weight + outgoingEdges.size();
                if (distances[childIndex] > d) {
                    distances[childIndex] = d;
                    queue.add(new Node(childIndex, d));
                }
            }
        }

        return String.valueOf(marked[t] ? distances[t] : -1);
    }

    static class Node implements Comparable<Node> {
        public int i;
        public int key;

        public Node(int i, int key) {
            this.i = i;
            this.key = key;
        }

        @Override
        public int compareTo(Node that) {
            if (this.key == that.key) return Integer.compare(this.i, that.i);
            else return Integer.compare(this.key, that.key);
        }
    }
}
