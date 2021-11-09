package network_flow.level3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static utils.Assertions.assertEquals;

public class FordFulkerson {

    public static void main(String[] args) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int x = 0; x < 4; x++) nodes.add(new Node(x));
        nodes.get(0).addEdge(nodes.get(1), 10);
        nodes.get(0).addEdge(nodes.get(2), 10);
        nodes.get(1).addEdge(nodes.get(3), 10);
        nodes.get(2).addEdge(nodes.get(3), 10);
        nodes.get(1).addEdge(nodes.get(2), 2);
        Graph g = new Graph(nodes, nodes.get(0), nodes.get(3));
        assertEquals(20, maxFlow(g));
    }

    /**
     * Find the maximum flow in the given network.
     *
     * @param g Graph representing the network.
     * @return The maximum flow of the network.
     */
    static int maxFlow(Graph g) {
        Node s = g.getSource();
        Node t = g.getSink();

        List<Edge> path;
        while (!(path = getPath(s, t)).isEmpty()) {
            augment(path);
        }

        int flow = 0;
        for (Edge e : s.getEdges()) {
            flow += e.getFlow();
        }
        return flow;
    }

    static void augment(List<Edge> path) {
        int b = Integer.MAX_VALUE;
        for (Edge e : path) {
            b = Math.min(b, e.getCapacity() - e.getFlow());
        }

        if (b <= 0) return;

        for (Edge e : path) {
            e.setFlow(e.getFlow() + b);
            e.getBackwards().setFlow(e.getCapacity() - e.getFlow());
        }
    }

    static List<Edge> getPath(Node s, Node t) {
        Set<Integer> known = new HashSet<>();
        List<Edge> path = new ArrayList<>();
        return getPath(s, t, known, path);
    }

    static List<Edge> getPath(Node s, Node t, Set<Integer> known, List<Edge> path) {
        if (s.equals(t)) return path;
        known.add(s.getId());

        for (Edge e : s.getEdges()) {
            Node to = e.getTo();
            if (known.contains(to.getId()) || e.getFlow() >= e.getCapacity()) continue;

            path.add(e);
            getPath(to, t, known, path);

            if (path.get(path.size() - 1).getTo().equals(t)) return path;
            path.remove(path.size() - 1);
        }
        return path;
    }


    /**
     * Library
     */
    static class Graph {

        private Collection<Node> nodes;

        private Node source;

        private Node sink;

        public Graph(Collection<Node> nodes, Node source, Node sink) {
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
            this.edges = new ArrayList<>();
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

        public Edge(int capacity, Node from, Node to) {
            this.capacity = capacity;
            this.from = from;
            this.to = to;
            this.flow = 0;
            this.backwards = new Edge(this);
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

        public Node getTo() {
            return to;
        }

        public void setFlow(int f) {
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
