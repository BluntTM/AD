package divide_and_conquer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static utils.Assertions.assertEquals;

public class SnuggleInTight {
    public static void main(String[] args) {
        {
            List<Point> points = new ArrayList<>();
            points.add(new Point(1, 2));
            points.add(new Point(4, 6));
            assertEquals(5, closestPair(points), 5e-6);
        }
        {
            List<Point> points = new ArrayList<>();
            points.add(new Point(2, 3));
            points.add(new Point(12, 30));
            points.add(new Point(40, 50));
            points.add(new Point(5, 1));
            points.add(new Point(12, 10));
            points.add(new Point(3, 4));
            assertEquals(1.4142135623730951, closestPair(points), 1e-6);
        }
    }

    /**
     * Takes a list of points and returns the distance between the closest pair.
     * This is done with divide and conquer.
     *
     * @param points
     *     - list of points that need to be considered.
     * @return smallest pair-wise distance between points.
     */
    public static double closestPair(List<Point> points) {
        List<Point> px = new ArrayList<>(points);
        Util.sortByX(px);
        List<Point> py = new ArrayList<>(points);
        Util.sortByX(py);

        return closestPairRec(px, py);
    }

    public static double closestPairRec(List<Point> px, List<Point> py) {
        if (px.size() <= 3) return Util.bruteForce(px);

        int mid = px.size()/2;
        Point median = px.get(mid);

        int halfSize = mid + 1;
        List<Point> qx = new ArrayList<>(halfSize);
        List<Point> qy = new ArrayList<>(halfSize);
        List<Point> rx = new ArrayList<>(halfSize);
        List<Point> ry = new ArrayList<>(halfSize);

        for (Point p : px) {
            if (p.x < median.x) qx.add(p);
            else rx.add(p);
        }

        for (Point p : py) {
            if (p.x < median.x) qy.add(p);
            else ry.add(p);
        }

        double qMin = closestPairRec(qx, qy);
        double rMin = closestPairRec(rx, ry);

        double d = Math.min(qMin, rMin);
        double L = median.x;

        List<Point> sy = new ArrayList<>(halfSize);
        for (Point p : py) {
            if (Math.abs(L - p.x) <= d) {
                sy.add(p);
            }
        }

        double min = d;
        for (int i = 0; i < sy.size(); i++) {
            Point pi = sy.get(i);
            for (int j = 1; i + j < sy.size() && j <= 15; j++) {
                Point pj = sy.get(i + j);
                min = Math.min(min, Util.distance(pi, pj));
            }
        }
        return min;
    }

    /**
     * Library
     */
    /**
     * Class representing a 2D point.
     */
    static class Point {

        public double x;

        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Useful methods for this assignment.
     */
    static class Util {

        /**
         * Takes two points and computes the euclidean distance between the two points.
         *
         * @param point1 - first point.
         * @param point2 - second point.
         * @return euclidean distance between the two points.
         * @see <a href="https://en.wikipedia.org/wiki/Euclidean_distance">https://en.wikipedia.org/wiki/Euclidean_distance</a>
         */
        public static double distance(Point point1, Point point2) {
            return Math.sqrt(Math.pow(point1.x - point2.x, 2.0D) + Math.pow(point1.y - point2.y, 2.0D));
        }

        /**
         * Takes a list of points and sorts it on x (ascending).
         *
         * @param points - points that need to be sorted.
         */
        public static void sortByX(List<Point> points) {
            Collections.sort(points, Comparator.comparingDouble(point -> point.x));
        }

        /**
         * Takes a list of points and sorts it on y (ascending) .
         *
         * @param points - points that need to be sorted.
         */
        public static void sortByY(List<Point> points) {
            Collections.sort(points, Comparator.comparingDouble(point -> point.y));
        }

        /**
         * Takes a list of points and returns the distance between the closest pair.
         * This is done by brute forcing.
         *
         * @param points - list of points that need to be considered.
         * @return smallest pair-wise distance between points.
         */
        public static double bruteForce(List<Point> points) {
            int size = points.size();
            if (size <= 1)
                return Double.POSITIVE_INFINITY;
            double bestDist = Double.POSITIVE_INFINITY;
            for (int i = 0; i < size - 1; i++) {
                Point point1 = points.get(i);
                for (int j = i + 1; j < size; j++) {
                    Point point2 = points.get(j);
                    double distance = Util.distance(point1, point2);
                    if (distance < bestDist)
                        bestDist = distance;
                }
            }
            return bestDist;
        }
    }
}
