package greedy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class PlanningBikeRepair {
    public static void main(String[] args) {
        assertEquals(
                read("/greedy/planning_bike_repair/example.out").trim(),
                run(getResourceAsStream("/greedy/planning_bike_repair/example.in")).trim()
        );
    }

    // Implement the solve method to return the answer to the problem posed by the inputstream.
    public static String run(InputStream in) {
        return new PlanningBikeRepair().solve(in);
    }

    public String solve(InputStream in) {
        List<Repair> repairs;

        try (Scanner sc = new Scanner(in)) {
            int n = sc.nextInt();
            repairs = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                repairs.add(new Repair(sc.nextInt(), sc.nextInt()));
            }
        }

        Collections.sort(repairs);
        return String.valueOf(amountOfPeopleNeeded(repairs));
    }

    public int amountOfPeopleNeeded(List<Repair> repairs) {
        List<Integer> employees = new ArrayList<>();

        for (Repair repair : repairs) {
            int done = repair.start + repair.length;
            int i;
            for (i = 0; i < employees.size(); i++) {
                if (employees.get(i) <= repair.start) {
                    employees.set(i, done);
                    break;
                }
            }

            if (i == employees.size()) {
                employees.add(done);
            }
        }

        return employees.size();
    }

    class Repair implements Comparable<Repair> {
        int start;
        int length;

        public Repair(int start, int length) {
            this.start = start;
            this.length = length;
        }

        @Override
        public int compareTo(Repair that) {
            return Integer.compare(this.start, that.start);
        }
    }
}
