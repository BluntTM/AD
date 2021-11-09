package network_flow.level4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import static utils.Assertions.assertEquals;

public class ImageSegmentation {

    public static void main(String[] args) {
        String str = "5 1\n" +
                "1 7 3\n" +
                "2 4 7\n" +
                "3 15 16\n" +
                "4 21 16\n" +
                "5 3 8\n" +
                "1 2 7\n" +
                "2 4 7\n" +
                "2 5 12\n" +
                "4 5 6\n";
        assertEquals(3, solve(str));
    }

    public static int solve(String str) {
        return new ImageSegmentation().solve(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
    }

    public int solve(InputStream in) {

        try (Scanner sc = new Scanner(in)) {
            // Parse scanner shit
            int n = sc.nextInt();
            int m = sc.nextInt();

            Node source = new Node(0);
            Node sink = new Node(n + 1);

            List<Node> nodes = new ArrayList<>();
            nodes.add(source);
            for (int i = 1; i <= n; i++) {
                Node node = new Node(sc.nextInt());
                source.addEdge(node, sc.nextInt());
                node.addEdge(sink, sc.nextInt());
                nodes.add(node);
            }
            nodes.add(sink);

            for (int i = 1; i <= m; i++) {
                Node n1 = nodes.get(sc.nextInt());
                Node n2 = nodes.get(sc.nextInt());
                int c = sc.nextInt();
                n1.addEdge(n2, c);
                n2.addEdge(n1, c);
            }

            // Build Graph
            Graph g = new Graph(nodes, source, sink);
            MaxFlow.maximizeFlow(g);
            System.out.println(g);

            // Find max flow
            int flow = 0;
            for (Edge e : g.getSource().getEdges()) {
                flow += e.getFlow();
            }
            return flow;
        }
    }


    /**
     * Library
     */
    static class MaxFlow {

        public static List<Edge> findPath(Graph g, Node start, Node end) {
            Map<Node, Edge> mapPath = new HashMap<Node, Edge>();
            Queue<Node> sQueue = new LinkedList<Node>();
            Node currentNode = start;
            sQueue.add(currentNode);
            while (!sQueue.isEmpty() && currentNode != end) {
                currentNode = sQueue.remove();
                for (Edge e : currentNode.getEdges()) {
                    Node to = e.getTo();
                    if (to != start && mapPath.get(to) == null && e.getResidual() > 0) {
                        sQueue.add(e.getTo());
                        mapPath.put(to, e);
                    }
                }
            }
            if (sQueue.isEmpty() && currentNode != end)
                return null;
            LinkedList<Edge> path = new LinkedList<Edge>();
            Node current = end;
            while (mapPath.get(current) != null) {
                Edge e = mapPath.get(current);
                path.addFirst(e);
                current = e.getFrom();
            }
            return path;
        }

        public static void maximizeFlow(Graph g) {
            Node sink = g.getSink();
            Node source = g.getSource();
            List<Edge> path;
            while ((path = findPath(g, source, sink)) != null) {
                int r = Integer.MAX_VALUE;
                for (Edge e : path) {
                    r = Math.min(r, e.getResidual());
                }
                for (Edge e : path) {
                    e.augmentFlow(r);
                }
            }
        }
    }

    static class Graph {

        private List<Node> nodes;

        private Node source;

        private Node sink;

        public Graph(List<Node> nodes, Node source, Node sink) {
            this.nodes = nodes;
            this.source = source;
            this.sink = sink;
        }

        public Node getSink() {
            return sink;
        }

        public Node getSource() {
            return source;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public boolean equals(Object other) {
            if (other instanceof Graph) {
                Graph that = (Graph) other;
                return this.nodes.equals(that.nodes);
            }
            return false;
        }
    }

    static class Node {

        protected int id;

        protected Collection<Edge> edges;

        public Node(int id) {
            this.id = id;
            this.edges = new ArrayList<Edge>();
        }

        public void addEdge(Node to, int capacity) {
            Edge e = new Edge(capacity, this, to);
            edges.add(e);
            to.getEdges().add(e.getBackwards());
        }

        public Collection<Edge> getEdges() {
            return edges;
        }

        public int getId() {
            return id;
        }

        public boolean equals(Object other) {
            if (other instanceof Node) {
                Node that = (Node) other;
                if (id == that.getId())
                    return edges.equals(that.getEdges());
            }
            return false;
        }
    }

    static class Edge {

        protected int capacity;

        protected int flow;

        protected Node from;

        protected Node to;

        protected Edge backwards;

        private Edge(Edge e) {
            this.flow = e.getCapacity();
            this.capacity = e.getCapacity();
            this.from = e.getTo();
            this.to = e.getFrom();
            this.backwards = e;
        }

        protected Edge(int capacity, Node from, Node to) {
            this.capacity = capacity;
            this.from = from;
            this.to = to;
            this.flow = 0;
            this.backwards = new Edge(this);
        }

        public void augmentFlow(int add) {
            assert (flow + add <= capacity);
            flow += add;
            backwards.setFlow(getResidual());
        }

        public Edge getBackwards() {
            return backwards;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getFlow() {
            return flow;
        }

        public Node getFrom() {
            return from;
        }

        public int getResidual() {
            return capacity - flow;
        }

        public Node getTo() {
            return to;
        }

        private void setFlow(int f) {
            assert (f <= capacity);
            this.flow = f;
        }

        public boolean equals(Object other) {
            if (other instanceof Edge) {
                Edge that = (Edge) other;
                return this.capacity == that.capacity && this.flow == that.flow && this.from.getId() == that.getFrom().getId() && this.to.getId() == that.getTo().getId();
            }
            return false;
        }
    }
}
