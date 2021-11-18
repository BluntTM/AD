package greedy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class RoutingTrains {
    public static void main(String[] args) {
        assertEquals(
                read("/greedy/routing_trains/example.out").trim(),
                run(getResourceAsStream("/greedy/routing_trains/example.in")).trim()
        );
    }

    // Implement the solve method to return the answer to the problem posed by the inputstream.
    public static String run(InputStream in) {
        return new RoutingTrains().solve(in);
    }

    public String solve(InputStream in) {
        try (Scanner sc = new Scanner(in)) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int s = sc.nextInt();

            Node[] nodes = new Node[n + 1]; // just initialize one more for ez access
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
            }

            for (int i = 0; i < m; i++) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                sc.nextInt(); // discard

                nodes[a].outgoingEdges.add(nodes[b]);
            }

            return hasCycle(nodes[s], new HashSet<>(nodes.length)) ? "yes" : "no";
        }
    }

    public static boolean hasCycle(Node s, Set<Node> visited) {
        if (visited.contains(s)) return true;
        visited.add(s);

        for (Node child : s.outgoingEdges) {
            if (hasCycle(child, visited)) return true;
        }

        visited.remove(s);
        return false;
    }

    /**
     * Library
     */
    static class Node {

        List<Node> outgoingEdges;

        boolean marked;

        public Node() {
            this.outgoingEdges = new ArrayList<>();
            this.marked = false;
        }
    }
}
