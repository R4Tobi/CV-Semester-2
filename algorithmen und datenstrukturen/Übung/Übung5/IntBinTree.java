package Übung5;

import aud.*; //to use BinaryTree<T>
import aud.util.*; //to use DotViewer, SingleStepper
import java.util.Iterator;

/**
 * Represents a binary tree that stores integer values.
 */
public class IntBinTree extends BinaryTree<Integer> {
    /**
     * Constructs a new IntBinTree object with the given data.
     *
     * @param data the data to be stored in the root node of the tree
     */
    public IntBinTree(int data) {
        super(data);
    }

    /**
     * Constructs a new IntBinTree object with the given data, left subtree, and right subtree.
     *
     * @param data  the data to be stored in the root node of the tree
     * @param left  the left subtree of the root node
     * @param right the right subtree of the root node
     */
    public IntBinTree(int data, IntBinTree left, IntBinTree right) {
        super(data, left, right);
    }

    /**
     * Calculates the height of the binary tree.
     *
     * @return the height of the binary tree
     */
    public int height() {
        return heightHelper(this);
    }

    /**
     * Helper method to calculate the height of a binary tree.
     *
     * @param node the current node in the binary tree
     * @return the height of the binary tree rooted at the given node
     */
    private int heightHelper(BinaryTree<Integer> node) {
        if (node == null)
            return 0;

        int leftHeight = heightHelper(node.getLeft());
        int rightHeight = heightHelper(node.getRight());

        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Calculates the maximum sum of a path in the binary tree.
     * Only includes positive sums.
     *
     * @return the maximum sum of a path in the binary tree
     */
    /**
     * Calculates the maximum sum of a path in the binary tree.
     *
     * @return the maximum sum of a path in the binary tree
     */
    public int maxSum() {
        int[] max = new int[] { Integer.MIN_VALUE };
        maxSumHelper(this, max);
        return max[0];
    }

    /**
     * Helper method to calculate the maximum sum of a path in the binary tree.
     *
     * @param node the current node in the binary tree
     * @param max  an array to store the maximum sum found so far
     * @return the maximum sum of a path starting from the given node
     */
    private int maxSumHelper(BinaryTree<Integer> node, int[] max) {
        
        if (node == null)
            return 0;

        int leftSum = maxSumHelper(node.getLeft(), max);
        int rightSum = maxSumHelper(node.getRight(), max);

        int currentSum = Math.max(node.getData(), Math.max(leftSum, rightSum) + node.getData());
        max[0] = Math.max(max[0], Math.max(currentSum, leftSum + rightSum + node.getData()));

        return Math.max(leftSum, rightSum) + node.getData();
    }

    /**
     * Calculates the maximum path sum in the binary tree.
     *
     * @return the maximum path sum in the binary tree
     */
    public int maxPath() {
        int[] max = new int[] { Integer.MIN_VALUE };
        maxPathHelper(this, max);
        return max[0];
    }

    /**
     * Helper method to calculate the maximum path sum in the binary tree.
     *
     * @param node the current node in the binary tree
     * @param max  an array to store the maximum path sum found so far
     * @return the maximum path sum starting from the given node
     */
    private int maxPathHelper(BinaryTree<Integer> node, int[] max) {
        if (node == null)
            return Integer.MIN_VALUE;

        int leftMaxPath = maxPathHelper(node.getLeft(), max);
        int rightMaxPath = maxPathHelper(node.getRight(), max);

        int maxChildPath = Math.max(leftMaxPath, rightMaxPath);
        int currentPath = node.getData() + Math.max(maxChildPath, 0);
        max[0] = Math.max(max[0], currentPath);

        return node.getData() + Math.max(leftMaxPath, rightMaxPath);
    }

    /**
     * Main method to test the IntBinTree class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create a simple binary tree
        IntBinTree tree = new IntBinTree(1, new IntBinTree(2, new IntBinTree(4), new IntBinTree(5)), new IntBinTree(3, new IntBinTree(6), new IntBinTree(7)));

        // Test the height method
        System.out.println("Height of the tree: " + tree.height()); // Expected output: 3

        // Test the maxSum method
        System.out.println("Maximum sum of the tree: " + tree.maxSum()); // Expected output: 18

        // Test the maxPath method
        System.out.println("Maximum path sum of the tree: " + tree.maxPath()); // Expected output: 16
    }
}
