/**
 * This class demonstrates the usage of the A234Tree data structure.
 */
package Übung7;

import aud.A234Tree;
import aud.util.DotViewer;
import aud.util.SingleStepper;

public class A234Example {
    public static void main(String[] args) {
        A234Tree<Integer> tree = new A234Tree<Integer>();
        SingleStepper stepper = new SingleStepper("Stepper");
        DotViewer viewer = DotViewer.displayWindow(tree, "A234 Tree");
        
        System.out.println(tree);

        Integer[] num1 = {3, 7, 5, 15, 17, 9, 13, 21, 11, 19};
        Integer[] num2 = {3, 5, 7, 9, 11, 13, 15, 17, 19, 21};

        for (Integer key : num2) {
            tree.insert(key);
            System.out.println(tree);
            stepper.halt();
            tree.checkConsistency();
            viewer.display(tree);
        }
    }
}