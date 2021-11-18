package greedy;

import static utils.Assertions.assertEquals;
import static utils.Assertions.assertFalse;
import static utils.Assertions.assertTrue;

public class UnionRank {
    public static void main(String[] args) {
        UnionFind uf = new UnionFind(10);
        assertEquals(9, uf.find(9));
        assertTrue(uf.union(1, 2));
        assertTrue(uf.union(2, 3));
        assertTrue(uf.union(0, 1));
        assertTrue(uf.union(3, 4));
        // Test that joining any combination will have no effect, as they are already joined
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) assertFalse("union(" + i + "," + j + ")", uf.union(i, j));
        // Test whether all first five entries have the same root
        for (int i = 0; i < 4; i++) assertEquals("find(" + i + ")", uf.find(i), uf.find(i + 1));
        // Test whether all last five entries have themselves as root
        for (int i = 5; i < 10; i++) assertEquals("find(" + i + ")", i, uf.find(i));
    }

    static class UnionFind {

        private int[] parent;

        private int[] rank;

        // Union Find structure implemented with two arrays for Union by Rank
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
        }

        /**
         * Merge two subtrees if they have a different parent, input is array indices
         * @param i a node in the first subtree
         * @param j a node in the second subtree
         * @return true iff i and j had different parents.
         */
        boolean union(int i, int j) {
            int parentI = find(i);
            int parentJ = find(j);
            if (parentI == parentJ) return false;
            parent[parentI] = parentJ;
            return true;
        }

        /**
         * NB: this function should also do path compression
         * @param i index of a node
         * @return the root of the subtree containg i.
         */
        int find(int i) {
            if (parent[i] == i) return i;
            else {
                int parentI = find(parent[i]);
                parent[i] = parentI;
                return parentI;
            }
        }

        // Return the rank of the trees
        public int[] getRank() {
            return rank;
        }

        // Return the parent of the trees
        public int[] getParent() {
            return parent;
        }
    }
}
