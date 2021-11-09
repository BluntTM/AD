package network_flow.tutorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ExtendLibrary {

    static class Graph {

        private List<Node> nodes;

        private Node source;

        private Node sink;

        public Graph(List<Node> nodes) {
            this.nodes = nodes;
            this.source = null;
            this.sink = null;
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

        /**
         *  You should implement this method.
         *  @param y Require a circulation of at this value
         */
        public boolean hasCirculationOfValue(int y) {
            this.removeLowerBounds();
            this.removeSupplyDemand();
            return MaxFlow.maximizeFlow(this) == y;
        }

        /**
         *  Should remove all lower bounds on edges.
         */
        private void removeLowerBounds() {
            for (Node n : nodes) {
                for (Edge e : n.getEdges()) {
                    if (e.lower <= 0) continue;
                    e.capacity -= e.lower;
                    e.backwards.capacity -= e.lower;
                    e.backwards.flow -= e.lower;
                    e.from.d += e.lower;
                    e.to.d -= e.lower;
                    e.lower = 0;
                    e.backwards.lower = 0;
                }
            }
        }

        /**
         *  Should remove all supply and demand from nodes.
         */
        private void removeSupplyDemand() {
            this.source = new Node(-1, 0);
            this.sink = new Node(-2, 0);
            for (Node n : nodes) {
                if (n.d > 0) {
                    n.addEdge(this.sink, 0, n.d);
                } else if (n.d < 0) {
                    this.source.addEdge(n, 0, -1 * n.d);
                }
                n.d = 0;
            }
        }
    }

    static class Node {

        protected int id;

        protected int d;

        protected Collection<Edge> edges;

        /**
         *  Create a new node
         *  @param id: Id for the node.
         *  @param d: demand for the node. Remember that supply is represented as a negative demand.
         */
        public Node(int id, int d) {
            this.id = id;
            this.d = d;
            this.edges = new ArrayList<Edge>();
        }

        public void addEdge(Node to, int lower, int upper) {
            Edge e = new Edge(lower, upper, this, to);
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

        protected int lower;

        protected int capacity;

        protected int flow;

        protected Node from;

        protected Node to;

        protected Edge backwards;

        private Edge(Edge e) {
            this.lower = 0;
            this.flow = e.getCapacity();
            this.capacity = e.getCapacity();
            this.from = e.getTo();
            this.to = e.getFrom();
            this.backwards = e;
        }

        protected Edge(int lower, int capacity, Node from, Node to) {
            this.lower = lower;
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

        public int getLower() {
            return lower;
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

        public static int maximizeFlow(Graph g) {
            int f = 0;
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
                f += r;
            }
            return f;
        }
    }
}
