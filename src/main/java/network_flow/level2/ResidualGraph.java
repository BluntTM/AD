package network_flow.level2;

import utils.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.Assertions.assertEquals;

public class ResidualGraph {

    public static void main(String[] args) {
        Node node0 = new Node(0);
        Node source = new Node(1);
        Node sink = new Node(2);
        Node node3 = new Node(3);

        // Add edge between the source and node 3 with capacity 1
        source.addEdge(node3, 1);
        node3.addEdge(source, 0);
        // Add edge between node 3 and node 0 with capacity 1
        node3.addEdge(node0, 1);
        node0.addEdge(node3, 0);
        // Add edge between node 0 and the sink with capacity 1
        node0.addEdge(sink, 1);
        sink.addEdge(node0, 0);

        List<Node> nodes = Arrays.asList(node0, source, sink, node3);

        Graph g = new Graph(nodes, source, sink);
        augmentPath(g, Arrays.asList(1, 3, 0, 2));

        Node ansNode0 = new Node(0);
        Node ansSource = new Node(1);
        Node ansSink = new Node(2);
        Node ansNode3 = new Node(3);

        // Add edge between the source and node 3 with flow 1
        ansSource.addEdge(ansNode3, 0);
        ansNode3.addEdge(ansSource, 1);
        // Add edge between node 3 and node 0 with flow 1
        ansNode3.addEdge(ansNode0, 0);
        ansNode0.addEdge(ansNode3, 1);
        // Add edge between node 0 and the sink with flow 1
        ansNode0.addEdge(ansSink, 0);
        ansSink.addEdge(ansNode0, 1);

        List<Node> ansNodes = Arrays.asList(ansNode0, ansSource, ansSink, ansNode3);

        Graph ansGraph = new Graph(ansNodes, ansSource, ansSink);

        assertEquals(ansGraph, g);
    }

    /**
     * Parses inputstream to graph.
     */
    static Graph parse(InputStream in) {
        try (Scanner sc = new Scanner(in)) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int s = sc.nextInt();
            int t = sc.nextInt();

            List<Node> nodes = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                nodes.add(new Node(i));
            }

            for (int i = 0; i < m; i++) {
                Node from = nodes.get(sc.nextInt());
                Node to = nodes.get(sc.nextInt());
                int cap = sc.nextInt();
                int flow = sc.nextInt();

                from.addEdge(to, cap - flow); //forward
                to.addEdge(from, flow); //backward
            }
            return new Graph(nodes, nodes.get(s), nodes.get(t));
        }
    }

    static Edge getEdge(Graph g, int from, int to) {
        for (Edge e : g.getNodes().get(from).getEdges()) {
            if (e.getTo().getId() == to) {
                return e;
            }
        }
        return null;
    }

    /**
     * Augments the flow over the given path by 1 if possible.
     *
     * @param g    Graph to operate on.
     * @param path List of nodes to represent the path.
     * @throws IllegalArgumentException if augmenting the flow in the given path is impossible.
     */
    static void augmentPath(Graph g, List<Integer> path) throws IllegalArgumentException {
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            Edge forward = getEdge(g, from, to);
            if (forward == null || forward.getCapacity() <= 0) throw new IllegalArgumentException();
            forward.setCapacity(forward.getCapacity() - 1);

            Edge backward = getEdge(g, to, from);
            backward.setCapacity(backward.getCapacity() + 1);
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

        List<Node> getNodes() {
            return nodes;
        }

        public boolean equals(Object other) {
            if (other instanceof Graph) {
                Graph that = (Graph) other;
                return this.nodes.equals(that.nodes);
            }
            return false;
        }

        @Override
        public String toString() {
            return "Graph{" +
                    "source=" + source +
                    ", sink=" + sink +
                    ", nodes=" + StringUtils.listToString(nodes, 2) +
                    '}';
        }
    }

    static class Node {

        private int id;

        private Collection<Edge> edges;

        public Node(int id) {
            this.id = id;
            this.edges = new ArrayList<Edge>();
        }

        void addEdge(Node to, int capacity) {
            Edge e = new Edge(capacity, this, to);
            edges.add(e);
        }

        Collection<Edge> getEdges() {
            return edges;
        }

        int getId() {
            return id;
        }

        public boolean equals(Object other) {
            if (other instanceof Node) {
                Node that = (Node) other;
                if (id == that.getId()) {
                    // Edges with capacity 0 should be ignored (you can choose to remove or keep these in the graph)
                    Set<Edge> thisEdges = this.getEdges().stream().filter(e -> e.getCapacity() > 0).collect(Collectors.toSet());
                    Set<Edge> thatEdges = that.getEdges().stream().filter(e -> e.getCapacity() > 0).collect(Collectors.toSet());
                    return thisEdges.equals(thatEdges);
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, edges);
        }

        @Override
        public String toString() {
            return "Node{" + "id=" + id + ", edges=" + StringUtils.listToString(edges, 4) + '}';
        }
    }

    static class Edge {

        private int capacity;

        private Node from;

        private Node to;

        protected Edge(int capacity, Node from, Node to) {
            this.capacity = capacity;
            this.from = from;
            this.to = to;
        }

        int getCapacity() {
            return capacity;
        }

        Node getFrom() {
            return from;
        }

        Node getTo() {
            return to;
        }

        void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public boolean equals(Object other) {
            if (other instanceof Edge) {
                Edge that = (Edge) other;
                return this.capacity == that.capacity && this.from.getId() == that.getFrom().getId() && this.to.getId() == that.getTo().getId();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(capacity, from.getId(), to.getId());
        }

        @Override
        public String toString() {
            return from.id + " -> " + to.id + " (" + capacity + ")";
        }
    }
}
