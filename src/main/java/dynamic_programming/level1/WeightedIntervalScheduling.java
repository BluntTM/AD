package dynamic_programming.level1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class WeightedIntervalScheduling {

    public static class Job {
        private final int startTime;
        private final int finishTime;
        private final int weight;

        public Job(int startTime, int finishTime) {
            this(startTime, finishTime, 1);
        }

        public Job(int startTime, int finishTime, int weight) {
            this.startTime = startTime;
            this.finishTime = finishTime;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        Job[] jobs = new Job[] {
                new Job(2, 3, 8),
                new Job(2, 8, 5),
                new Job(1, 10, 4),
                new Job(7, 12, 6),
                new Job(9, 13, 3),
                new Job(8, 14, 3)
        };

        Arrays.sort(jobs, Comparator.comparingInt(o -> o.finishTime));
        int[] predecessors = computePredecessors(jobs);
        predecessors = new int[] {-1, -1, -1, 1, 2, 2};
        System.out.println("Predecessors = " + Arrays.toString(predecessors));
        System.out.println();

        Long[] mem = new Long[jobs.length];
        mem[0] = 0L;

        long maxJobWeight = optIterative(jobs, predecessors, mem);
        System.out.println("Max Job Weight = " + maxJobWeight);
        System.out.println("Memory = " + Arrays.toString(mem));
        System.out.println();
        System.out.println("Which jobs should we do?");
        findMaximumJobWeightSolutionIterative(jobs, predecessors, mem);
    }

    /**
     * Find's the jobs we need to do to achieve the maximum job weight, using the initialized memory.
     */
    public static void findMaximumJobWeightSolutionIterative(Job[] jobs, int[] predecessors, Long[] mem) {
        int j = jobs.length - 1;
        while (j > 0) {
            long v = 0;
            if (predecessors[j] != -1) {
                v = mem[predecessors[j]];
            }

            if (jobs[j].weight + v > mem[j - 1]) {
                System.out.println("Job " + j);
                j = predecessors[j];
            } else {
                j--;
            }
        }
    }

    /**
     * Poor man's solution using recursion.
     */
    public static void findMaximumJobWeightSolution(Job[] jobs, int j, int[] predecessors, Long[] mem) {
        if (j < 0) return;

        long v = 0;
        if (predecessors[j] != -1) {
            v = mem[predecessors[j]];
        }

        if (jobs[j].weight + v > mem[j - 1]) {
            findMaximumJobWeightSolution(jobs, predecessors[j], predecessors, mem);
            System.out.println("Job " + j);
        } else {
            findMaximumJobWeightSolution(jobs, j - 1, predecessors, mem);
        }
    }

    /**
     * Computes the maximum achievable job weight/values iteratively, filling up the memory.
     */
    private static long optIterative(Job[] jobs, int[] predecessors, Long[] mem) {
        for (int j = 1; j < jobs.length; j++) {
            long v = 0;
            if (predecessors[j] >= 0) {
                v = mem[predecessors[j]];
            }
            mem[j] = Math.max(jobs[j].weight + v, mem[j - 1]);
        }
        return mem[jobs.length - 1];
    }

    /**
     * Poor man's computation using recursion.
     */
    private static long opt(int j, Job[] jobs, int[] predecessors, Long[] mem) {
        if (j <= 0) return 0L;
        else if (mem[j] == null) {
            long optPredecessor = jobs[j].weight + opt(predecessors[j], jobs, predecessors, mem);
            long optPrevious = opt(j - 1, jobs, predecessors, mem);
            mem[j] = Math.max(optPredecessor, optPrevious);
        }
        return mem[j];
    }

    /**
     * Computes the predecessors of given Job array.
     * @return If job i has a predecessor, index i contains the index of the job with the maximum finish time,
     *         such that the predecessor is compatible with job i.
     *         If job i has no predecessor, -1 is returned.
     */
    public static int[] computePredecessors(Job[] jobs) {
        int[] t = sortedIndices(jobs, Comparator.comparingInt(o -> o.startTime));

        int k = jobs.length - 1;
        int[] predecessors = new int[jobs.length];
        for (int i = k; i >= 0; i--) {
            while (k >= 0 && jobs[k].finishTime > jobs[t[i]].startTime) {
                k--;
            }
            if (k < 0) predecessors[i] = -1;
            else predecessors[i] = t[k];
        }
        return predecessors;
    }

    /**
     * Sorts an (object) array using given comparator, returning the sorted indices, leaving original array intact.
     */
    private static <T> int[] sortedIndices(T[] arr, Comparator<T> comparator) {
        return IntStream.range(0, arr.length)
                .boxed()
                .sorted((o1, o2) -> comparator.compare(arr[o1], arr[o2]))
                .mapToInt(Integer::intValue)
                .toArray();
    }
}
