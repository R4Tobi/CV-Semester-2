package Uebung7;

import aud.RedBlackTree;
import aud.util.SingleStepper;
import aud.util.DotViewer;

class RedBlackExample{
    public static void main(String[] args) {
        RedBlackTree<Integer, Integer> tree = new RedBlackTree<Integer, Integer>();
        SingleStepper stepper = new SingleStepper(String.valueOf(tree));
        DotViewer viewer = DotViewer.displayWindow(tree, "RedBlack Tree");
        
        // Insertion of nodes into the RedBlackTree
        tree.insert(6, 6);
        stepper.halt();
        viewer.display(tree);

        tree.insert(7, 7);
        stepper.halt();
        viewer.display(tree);

        tree.insert(3, 3);
        stepper.halt();
        viewer.display(tree);

        tree.insert(4, 4);
        stepper.halt();
        viewer.display(tree);

        tree.insert(2, 2);
        stepper.halt();
        viewer.display(tree);

        tree.insert(1, 1);
        stepper.halt();
        viewer.display(tree);

        stepper.halt("stop");
    }
}