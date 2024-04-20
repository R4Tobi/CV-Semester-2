package Übung2;

import aud.Stack;

public class TPalindrome {
    public static boolean isTPalindrome(String text) {
        Stack<Character> stack = new Stack<>();
        StringBuilder innerText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') {
                // Check if the inner text is a mirror image
                String innerStr = innerText.toString();
                StringBuilder reversed = new StringBuilder(innerStr).reverse();
                if (!innerStr.equals(reversed.toString())) {
                    return false; // If it's not a mirror image, return false
                }
                innerText.setLength(0); // Clear inner text for the next parentheses
            } else if (c != ')') {
                // Push non-parenthesis characters onto the stack
                stack.push(c);
            } else {
                // Ignore ')' as it should only occur within parentheses
            }
            // Collect characters for inner text
            if (c != '(' && c != ')') {
                innerText.append(c);
            }
        }

        // Check if the remaining stack content is a palindrome
        StringBuilder remaining = new StringBuilder();
        while (!stack.is_empty()) {
            remaining.insert(0, stack.pop());
        }

        return isPalindrome(remaining.toString());
    }
    
    private static boolean isPalindrome(String str) {
        int left = 0;
        int right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
    
    // Testmethode zum Ausführen der Prüfung
    public static void main(String[] args) {
        String[] true_ = new String[]{"abc(ah(otto)v(atta)ha)cba", "(*)", "otto", "al(otto)la", "abc(aha)(u)cba" , "abc(ah(otto)v(atta)ha)cba"}; // Beispieltexte für einen T-Palindrom
        String[] false_ = new String[] { "a(b)cca(b)", "abc", "abc(ah(otto)h)cba" };
        
        for(String item : true_){
            System.out.println(item + ":" + isTPalindrome(item));
        }

        for (String item : false_) {
            System.out.println(item + ":" + isTPalindrome(item));
        }
    }
}
