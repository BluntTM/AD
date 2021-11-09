package dynamic_programming.level2;

import java.util.Arrays;

public class SegmentedLeastSquares {

    public static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Line {
        private final double a;
        private final double b;

        public Line(double a, double b) {
            this.a = a;
            this.b = b;
        }

        public double calc(double x) {
            return a * x + b;
        }

        @Override
        public String toString() {
            return "Line{" + a + "x + " + b + "}";
        }
    }

    public static void main(String[] args) {
        Point[] points = new Point[]{
                // Segment 1
                new Point(1, 1.5),
                new Point(1.5, 1.6),
                new Point(2, 1.7),
                new Point(2.5, 1.8),
                new Point(3, 1.9),
                new Point(3.5, 2),

                // Segment 2
                new Point(3.8, 2.5),
                new Point(3.9, 3),
                new Point(3.95, 3.45),
                new Point(4.1, 4),
                new Point(4.2, 4.5),
                new Point(4.3, 5),
                new Point(4.4, 5.5),
                new Point(4.55, 5.95),

                // Segment 3
                new Point(5, 6.1),
                new Point(5.5, 6),
                new Point(6, 6.2),
                new Point(6.5, 6.1),
                new Point(7, 6.2),
                new Point(7.5, 6.3)
        };

        double[] mem = new double[points.length];
        double segmentCost = 0.4;

        System.out.println("Max Error = " + computeSegmentedLeastSquaresMaxError(points, segmentCost, mem));
        System.out.println("Memory = " + Arrays.toString(mem));
        System.out.println();
        System.out.println("Segments:");
        printSegmentedLeastSquares(points, segmentCost, mem);
    }

    /**
     * Prints the finalized segments as lines given the points, segmentCost and initialized memory.
     */
    public static void printSegmentedLeastSquares(Point[] points, double segmentCost, double[] mem) {
        for (int j = 0; j < mem.length; j++) {
            int i = j;

            double max = mem[j] + segmentCost;
            while (j < (mem.length - 1) && mem[j + 1] < max) {
                j++;
            }

            System.out.println(bestLinearLine(points, i, j));
        }
    }

    /**
     * Computes the Segmented Least Squares max error while initializing the memory, iteratively.
     */
    public static double computeSegmentedLeastSquaresMaxError(Point[] points, double segmentCost, double[] mem) {
        double[][] errors = new double[points.length][];
        for (int j = 0; j < points.length; j++) {
            errors[j] = new double[points.length];
            for (int i = 0; i <= j; i++) {
                errors[j][i] = computeError(points, i, j + 1);
            }
        }

        for (int j = 0; j < points.length; j++) {
            mem[j] = errors[j][0] + segmentCost;
            for (int i = 0; i < j; i++) {
                double c = errors[j][i] + segmentCost + mem[i];
                mem[j] = Math.min(mem[j], c);
            }
        }

        return mem[points.length - 1];
    }

    /**
     * Simple Sum of Squared Error function, for any subset defined by lower index (including) up to upper index (excluding).
     */
    public static double computeError(Point[] points, int lower, int upper) {
        Line line = bestLinearLine(points, lower, upper);

        double error = 0;
        for (int i = lower; i < upper; i++) {
            Point p = points[i];
            double d = p.y - line.calc(p.x);
            error += d * d;
        }
        return Double.isNaN(error) ? 0 : error;
    }

    /**
     * Fits the best linear line through a set of points, defined by lower index (including) up to upper index (excluding).
     */
    public static Line bestLinearLine(Point[] points, int lower, int upper) {
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;
        for (int i = lower; i < upper; i++) {
            Point p = points[i];
            sumX += p.x;
            sumY += p.y;
            sumXY += p.x * p.y;
            sumXX += p.x * p.x;
        }

        int n = upper - lower;
        double a = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double b = (sumY - a * sumX) / n;
        return new Line(a, b);
    }
}
