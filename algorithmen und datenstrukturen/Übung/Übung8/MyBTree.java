package Übung8;

import aud.BTree;
import aud.*;
import Übung6.SimpleTree;

public class MyBTree extends BTree {
    /**
     * create an empty tree of order 2*m+1
     *
     * @param m
     */
    public MyBTree(int m) {
        super(m);
    }

    public int getHeight() {
        return getHeight(this.root);
    }

    private int getHeight(SimpleTree.Node node) {
        if (node == null) {
            return -1;
        }

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static void main(String[] args) {
        MyBTree tree = new MyBTree(2);

        int n = 1000000;
        for (int i = 0; i < n; i++) {
            tree.insert((int)Math.random() * 100);
        }
        System.out.println(tree.getHeight());
    }
}
