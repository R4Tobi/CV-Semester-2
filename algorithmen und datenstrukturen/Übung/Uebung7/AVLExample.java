package Uebung7;

import aud.AVLTree;
import aud.util.DotViewer;
import aud.util.SingleStepper;

public class AVLExample {

    public static void main(String[] args) {

        AVLTree<Integer, String> avlTree = new AVLTree<Integer, String>();
        SingleStepper stepper = new SingleStepper("Stepper");
        DotViewer viewer = DotViewer.displayWindow(avlTree, "AVL Tree");

        stepper.halt();
        avlTree.insert(14, "");
        viewer.display(avlTree);

        stepper.halt();
        avlTree.insert(15, "");
        viewer.display(avlTree);

        stepper.halt();
        avlTree.insert(17, "");
        viewer.display(avlTree);

        stepper.halt();
        avlTree.insert(7, "");
        viewer.display(avlTree);

        stepper.halt();
        avlTree.insert(5, "");
        viewer.display(avlTree);

        stepper.halt();
        avlTree.insert(10, "");
        viewer.display(avlTree);

        stepper.halt();
        avlTree.insert(16, "");
        viewer.display(avlTree);

    }
}