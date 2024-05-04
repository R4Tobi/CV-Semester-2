package Übung4;

import aud.util.DotViewer;
import aud.BinaryTree;

public class BuildBinTree {
    public static void main(String[] args) {
        // Baum erstellen
        BinaryTree<String> a = new BinaryTree<String>("A");
        BinaryTree<String> b = new BinaryTree<String>("B");
        BinaryTree<String> c = new BinaryTree<String>("C");
        BinaryTree<String> d = new BinaryTree<String>("D");

        a.setLeft(b);
        a.setRight(c);
        c.setLeft(d);

        // Traversierungen durchführen und ausgeben
        System.out.println("preorder: " + a.preorder().toString());
        System.out.println("inorder: " + a.inorder().toString());
        System.out.println("postorder: " + a.postorder().toString());
        System.out.println("level-order: " + a.levelorder().toString());

        // Baum visualisieren
        DotViewer.displayWindow(a, "Binary Tree Visualization");
    }
}