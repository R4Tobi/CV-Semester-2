package Uebung1;

import aud.Stack;

public class Palindrome {
    // Test if text is a palindrome.
    // Ignore upper/lower case, white space, and punctuation.
    public static boolean isPalindrome(String text) {
        // create stack
        Stack<Character> stack = new Stack<>();


        // push all letters to stack
        for (int i = 0; i < text.length() ; i++) {
            if(Character.isLetter(text.charAt(i))){
                stack.push(Character.toLowerCase(text.charAt(i)));
            }
        }

        // compare letters by popping from stack and comparing with text
        for (int i = 0; i < text.length() ; i++) {
            if(Character.isLetter(text.charAt(i))){
                if(stack.pop() != Character.toLowerCase(text.charAt(i))){
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome("O, Streit irritiert so!"));
    }
}