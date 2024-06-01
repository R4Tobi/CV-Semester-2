package Uebung8;

import aud.BTree;
import aud.KTreeNode;
import aud.util.DotViewer;

public class MyBTree<T extends Comparable<T>> extends BTree<T> {
    /**
     * create an empty tree of order 2*m+1
     *
     * @param m order of the tree
     */
    public MyBTree(int m) {
        super(m);
    }

    public int getHeight() {
        return getHeight(root());
    }

    private int getHeight(KTreeNode<T> node) {
        if (node == null) {
            return 0;
        }

        int maxChildHeight = 0;
        for (int i = 0; i < node.getK(); i++) {
            if (node.getChild(i) != null) {
                int childHeight = getHeight(node.getChild(i));
                if (childHeight > maxChildHeight) {
                    maxChildHeight = childHeight;
                }
            }
        }

        return maxChildHeight + 1;
    }

    public static void main(String[] args) {
        MyBTree<Integer> btree = new MyBTree<Integer>(2);
        btree.insert(6);
        btree.insert(18);
        btree.insert(22);
        btree.insert(3);
        btree.insert(11);
        btree.insert(16);
        btree.insert(7);
        btree.insert(10);
        btree.insert(1);
        btree.insert(8);
        btree.insert(12);

        DotViewer.displayWindow(btree, "BTree");

        MyBTree<Integer> btree2 = new MyBTree<Integer>(2);
        for (int i = 0; i <= 1000000; i++){
            btree2.insert((int) (Math.random() * 100));
        }

        DotViewer.displayWindow(btree2, "BTree");

        System.out.println(btree2.getHeight());
    }
}
