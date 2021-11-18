package divide_and_conquer;

import static utils.Assertions.assertFalse;
import static utils.Assertions.assertTrue;

public class ExploringMaze {
    public static void main(String[] args) {
        ExploringMaze maze = new ExploringMaze();
        BinaryTree tree = new BinaryTree(42, new BinaryTree(1337), new BinaryTree(39));
        assertTrue(maze.search(tree, 42));
        assertFalse(maze.search(tree, 100));
    }

    /**
     * Recursively searches for the element.
     * Returns true if element can be found, else false.
     *
     * @param tree
     *     - tree that you need to look in.
     * @param element
     *     - the element that you are looking for.
     * @return true if found, else false.
     */
    public boolean search(BinaryTree tree, int element) {
        if (tree == null) return false;
        if (tree.getKey() == element) return true;
        boolean found = false;
        if (tree.hasLeft()) found |= search(tree.getLeft(), element);
        if (!found && tree.hasRight()) found |= search(tree.getRight(), element);
        return found;
    }


    /**
     * Library
     */
    static class BinaryTree {

        private int key;

        private BinaryTree left, right;

        /**
         * Simple constructor.
         *
         * @param key
         *     to set as key.
         */
        public BinaryTree(int key) {
            this.key = key;
        }

        /**
         * Extended constructor.
         *
         * @param key
         *     to set as key.
         * @param left
         *     to set as left child.
         * @param right
         *     to set as right child.
         */
        public BinaryTree(int key, BinaryTree left, BinaryTree right) {
            this.key = key;
            setLeft(left);
            setRight(right);
        }

        public int getKey() {
            return key;
        }

        /**
         * @return the left child.
         */
        public BinaryTree getLeft() {
            return left;
        }

        /**
         * @return the right child.
         */
        public BinaryTree getRight() {
            return right;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public boolean hasRight() {
            return right != null;
        }

        /**
         * @param left
         *     to set
         */
        public void setLeft(BinaryTree left) {
            this.left = left;
        }

        /**
         * @param right
         *     to set
         */
        public void setRight(BinaryTree right) {
            this.right = right;
        }
    }
}
