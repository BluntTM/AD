package greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import static utils.Assertions.assertEquals;

public class OptimalWorkstation {
    public static void main(String[] args) {
        int n = 5;
        int m = 10;
        int[] start = { 0, 2, 1, 17, 3, 15 };
        int[] end = { 0, 6, 2, 7, 9, 6 };
        assertEquals(3, solve(n, m, start, end));
    }

    /**
     * @param n number of researchers
     * @param m amount of minutes after computer locks
     * @param start start times of jobs 1 through n. NB: you should ignore start[0]
     * @param duration duration of jobs 1 through n. NB: you should ignore duration[0]
     * @return the number of unlocks that can be saved.
     */
    public static int solve(int n, int m, int[] start, int[] duration) {
        List<Slot> slots = new ArrayList<>(n + 1);
        for (int i = 1; i <= n; i++) {
            slots.add(new Slot(start[i], duration[i]));
        }
        Collections.sort(slots);

        PriorityQueue<Integer> computers = new PriorityQueue<>();
        int wakes = 0;
        for (Slot slot : slots) {
            while (!computers.isEmpty()) {
                // if slot starts bf computer's available time, stop
                if (slot.start < computers.peek()) break;

                // if slot starts within computer's autolock, increment wakes
                if (slot.start <= computers.poll() + m) {
                    wakes++;
                    break;
                }
            }
            computers.add(slot.endsAt());
        }
        return wakes;
    }

    static class Slot implements Comparable<Slot> {
        public int start;
        public int duration;

        public Slot(int start, int duration) {
            this.start = start;
            this.duration = duration;
        }

        public int endsAt() {
            return start + duration;
        }

        @Override
        public int compareTo(Slot that) {
            return Integer.compare(this.start, that.start);
        }
    }
}
