package greedy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class Maze {
    public static void main(String[] args) {
        assertEquals(
                read("/greedy/maze/example.out").trim(),
                run(getResourceAsStream("/greedy/maze/example.in")).trim()
        );
    }

    // Implement the solve method to return the answer to the problem posed by the inputstream.
    public static String run(InputStream in) {
        return new Maze().solve(in);
    }

    public String solve(InputStream in) {
        try (Scanner sc = new Scanner(in)) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int s = sc.nextInt();
            int t = sc.nextInt();

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

            return canReach(nodes[s], nodes[t]) ? "yes" : "no";
        }
    }

    public static boolean canReach(Node s, Node t) {
        s.marked = true;
        if (s.equals(t)) return true;

        for (Node child : s.outgoingEdges) {
            if (child.marked) continue;
            if (canReach(child, t)) return true;
        }
        return false;
    }

    static class Node {

        List<Node> outgoingEdges;

        boolean marked;

        public Node() {
            this.outgoingEdges = new ArrayList<>();
            this.marked = false;
        }
    }
}
