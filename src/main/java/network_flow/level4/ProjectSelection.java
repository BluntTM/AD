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

public class ProjectSelection {

    public static void main(String[] args) {
        String str = "2 3\n" +
                "Oliver 5 2 investigation interviewing\n" +
                "Caleb 8 2 interviewing lit\n" +
                "hire_member 1 1 interviewing\n" +
                "interview_author 3 2 interviewing lit\n" +
                "solve_crime 4 1 investigation";
        assertEquals(true, solve(str));

        String str2 = "1 1\n" +
                "a 71 3 b c d\n" +
                "e 2 3 f c g";
        assertEquals(false, solve(str2));
    }

    public static boolean solve(String str) {
        return new ProjectSelection().solve(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
    }

    public boolean solve(InputStream in) {
        try (Scanner sc = new Scanner(in)) {
            int n = sc.nextInt();
            int m = sc.nextInt();

            Node s = new Node("Source");
            Node t = new Node("Sink");

            List<Node> nodes = new ArrayList<>();
            nodes.add(s);

            Map<String, Node> qualities = new HashMap<>();
            for (int i = 0; i < n; i++) {
                Node node = new Node(sc.next());
                nodes.add(node);
                s.addEdge(node, sc.nextInt());

                int si = sc.nextInt();
                for (int j = 0; j < si; j++) {
                    String quality = sc.next();
                    Node req = qualities.computeIfAbsent(quality, k -> {
                        Node q = new Node(k);
                        nodes.add(q);
                        return q;
                    });
                    node.addEdge(req, Integer.MAX_VALUE / 2);
                }
            }

            int totalJobTime = 0;
            for (int i = 0; i < m; i++) {
                Node node = new Node(sc.next());
                nodes.add(node);

                int jobTime = sc.nextInt();
                node.addEdge(t, jobTime);
                totalJobTime += jobTime;

                int qj = sc.nextInt();
                int cap = Math.max(jobTime - (qj - 1), 0);
                for (int j = 0; j < qj; j++) {
                    Node quality = qualities.get(sc.next());
                    if (quality == null) continue;
                    quality.addEdge(node, cap);
                }
            }
            nodes.add(t);

            Graph g = new Graph(nodes, s, t);
            MaxFlow.maximizeFlow(g);

            int flow = 0;
            for (Edge e : s.getEdges()) {
                flow += e.getFlow();
            }
            return totalJobTime == flow;
        }
    }


    /**
     * Library
     */
    static class MaxFlow {

        private static List<Edge> findPath(Graph g, Node start, Node end) {
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

        static void maximizeFlow(Graph g) {
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

        public Collection<Node> getNodes() {
            return nodes;
        }

        public void maximizeFlow() {
            MaxFlow.maximizeFlow(this);
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

        protected String id;

        protected Collection<Edge> edges;

        public Node(String id) {
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

        public String getId() {
            return id;
        }

        public boolean equals(Object other) {
            if (other instanceof Node) {
                Node that = (Node) other;
                if (id.equals(that.getId()))
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
                return this.capacity == that.capacity && this.flow == that.flow && this.from.getId().equals(that.getFrom().getId()) && this.to.getId().equals(that.getTo().getId());
            }
            return false;
        }
    }
}
