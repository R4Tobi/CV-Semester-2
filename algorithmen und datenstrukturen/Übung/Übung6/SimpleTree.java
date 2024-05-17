package Übung6;
//-----------------------------------------------------------------//
public class SimpleTree<T extends Comparable<T>> {
    // ---------------------------------------------------------------//
    public class Node {
        private T data;
        private Node left;
        private Node right;

        public Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        public T getData() {
            return data;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    // ---------------------------------------------------------------//
    private Node root_;

    // ---------------------------------------------------------------//
    public SimpleTree() {
        root_ = null;
    }

    // ---------------------------------------------------------------//
    public void add(T obj) {
        if (root_ == null) {
            root_ = new Node(obj);
        } else {
            addRecursive(root_, obj);
        }
    }

    private void addRecursive(Node node, T obj) {
        if (obj.compareTo(node.getData()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new Node(obj));
            } else {
                addRecursive(node.getLeft(), obj);
            }
        } else if (obj.compareTo(node.getData()) > 0) {
            if (node.getRight() == null) {
                node.setRight(new Node(obj));
            } else {
                addRecursive(node.getRight(), obj);
            }
        }
    }

    // ---------------------------------------------------------------//
    public boolean contains(T obj) {
        return containsRecursive(root_, obj);
    }

    private boolean containsRecursive(Node node, T obj) {
        if (node == null) {
            return false;
        }

        if (obj.compareTo(node.getData()) == 0) {
            return true;
        } else if (obj.compareTo(node.getData()) < 0) {
            return containsRecursive(node.getLeft(), obj);
        } else {
            return containsRecursive(node.getRight(), obj);
        }
    }

    // ---------------------------------------------------------------//
    public String toString() {
        return toStringRecursive(root_);
    }

    private String toStringRecursive(Node node) {
        if (node == null) {
            return "";
        }

        String result = "";
        result += toStringRecursive(node.getLeft());
        result += node.getData() + " ";
        result += toStringRecursive(node.getRight());

        return result;
    }

    public static void main(String[] args) {
        SimpleTree<Integer> tree = new SimpleTree<Integer>();

        // Test adding elements to the tree
        tree.add(5);
        tree.add(3);
        tree.add(7);
        tree.add(2);
        tree.add(4);
        tree.add(6);
        tree.add(8);

        // Test contains method
        System.out.println(tree.contains(5)); // Output: true
        System.out.println(tree.contains(10)); // Output: false

        // Test toString method
        System.out.println(tree.toString()); // Output: 2 3 4 5 6 7 8
    }
}