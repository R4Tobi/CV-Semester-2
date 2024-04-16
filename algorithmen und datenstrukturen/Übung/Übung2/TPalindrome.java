package Übung2;

import aud.Stack;

public class TPalindrome {
    public static boolean isTPalindrome(String text) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            switch (c) {
                case '(':
                case '*':
                    stack.push(c);
                    break;
                case ')':
                    StringBuilder temp = new StringBuilder();
                    while (!stack.is_empty() && stack.top() != '(') {
                        temp.append(stack.pop());
                    }
                    if (stack.is_empty() || stack.pop() != '(') {
                        return false; // Ungültige Klammerung
                    }
                    if (isPalindrome(temp.toString())) {
                        stack.push('*');
                    } else {
                        return false;
                    }
                    break;
                default: // Für a-Z
                    if (!Character.isAlphabetic(c)) {
                        return false; // Ungültiges Zeichen gefunden
                    }
                    if (!stack.is_empty() && stack.top() == '*') {
                        stack.pop();
                    } else {
                        stack.push(c);
                    }
            }
        }

        // Überprüfen, ob der verbleibende Stapelinhalt ein Palindrom ist
        StringBuilder remaining = new StringBuilder();
        while (!stack.is_empty()) {
            remaining.append(stack.pop());
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
        String text = "abc(ah*v*ha)cba"; // Beispieltext für einen T-Palindrom
        System.out.println(text + " :" + isTPalindrome(text));
    }
}
