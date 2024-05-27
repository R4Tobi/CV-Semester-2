package Übung6;

/**
 * Eine einfache Implementierung eines binären Suchbaums.
 * 
 * @param <T> der Typ der Elemente, die im Baum gespeichert sind, muss das
 *            Comparable-Interface implementieren
 */
public class SimpleTree<T extends Comparable<T>> {
    /**
     * Stellt einen Knoten im binären Suchbaum dar.
     */
    public class Node {
        // Die Daten für den Knoten
        private T data;
        // Der linke Kindknoten
        private Node left;
        // Der rechte Kindknoten
        private Node right;

        /**
         * Erstellt einen neuen Knoten mit den gegebenen Daten.
         * 
         * @param data die Daten, die im Knoten gespeichert werden sollen
         */
        public Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        // Getter für die Daten des Knotens
        public T getData() {
            return data;
        }

        // Getter für das linke Kind des Knotens
        public Node getLeft() {
            return left;
        }

        // Getter für das rechte Kind des Knotens
        public Node getRight() {
            return right;
        }

        // Setter für das linke Kind des Knotens
        public void setLeft(Node left) {
            this.left = left;
        }

        // Setter für das rechte Kind des Knotens
        public void setRight(Node right) {
            this.right = right;
        }
    }

    // Die Wurzel des Baums
    private Node root_;

    /**
     * Konstruiert einen leeren binären Suchbaum.
     */
    public SimpleTree() {
        root_ = null;
    }

    /**
     * Fügt ein Element zum binären Suchbaum hinzu.
     * 
     * @param obj das hinzuzufügende Element
     */
    public void add(T obj) {
        if (root_ == null) {
            root_ = new Node(obj);
        } else {
            addRecursive(root_, obj);
        }
    }

    /**
     * Hilfsmethode zum rekursiven Hinzufügen eines Elements.
     */
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

    /**
     * Überprüft, ob der binäre Suchbaum ein gegebenes Element enthält.
     * 
     * @param obj das zu überprüfende Element
     * @return true, wenn das Element gefunden wird, sonst false
     */
    public boolean contains(T obj) {
        return containsRecursive(root_, obj);
    }

    /**
     * Hilfsmethode zum rekursiven Durchsuchen auf ein Element.
     */
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

    /**
     * Gibt eine String-Repräsentation des binären Suchbaums zurück.
     * 
     * @return eine String-Repräsentation des Baums
     */
    public String toString() {
        return toStringRecursive(root_);
    }

    /**
     * Hilfsmethode zur Erstellung einer Inorder-String-Repräsentation des Baumes.
     */
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

        // Test: Elemente zum Baum hinzufügen
        tree.add(5);
        tree.add(3);
        tree.add(7);
        tree.add(2);
        tree.add(4);
        tree.add(6);
        tree.add(8);

        // Test: contains-Methode
        System.out.println(tree.contains(5)); // Ausgabe: true
        System.out.println(tree.contains(10)); // Ausgabe: false

        // Test: toString-Methode
        System.out.println(tree.toString()); // Ausgabe: 2
    }
}