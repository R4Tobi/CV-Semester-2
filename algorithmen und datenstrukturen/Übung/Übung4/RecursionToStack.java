package Übung4;

import aud.Stack;

public class RecursionToStack {

  public static int whatStack(int n) {
    // Create a stack to store digits
    Stack<Integer> digitStack = new Stack<>();

    // Push each digit of n onto the stack
    while (n >= 10) {
      digitStack.push(n % 10);
      n /= 10;
    }
    digitStack.push(n); // Push the last digit onto the stack

    // Sum up the digits by popping them from the stack
    int sum = 0;
    while (!digitStack.is_empty()) {
      sum += digitStack.pop();
    }
    return sum;
  }

  public static void main(String[] args) {
    int n = 12345;
    System.out.println("Sum of digits using whatStack: " + whatStack(n));
  }
}