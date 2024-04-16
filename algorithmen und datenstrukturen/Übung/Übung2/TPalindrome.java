package Übung2;

import aud.Stack;

public class TPalindrome {
    public static boolean isTPalindrome(String text) {
        Stack<Character> stack = new Stack<>();
    
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
    
            switch (c) {
                case '(':
                    // Einfach eine öffnende Klammer auf den Stapel legen
                    stack.push(c);
                    break;
                case ')':
                    // Hier überprüfen wir den Inhalt zwischen den Klammern
                    StringBuilder temp = new StringBuilder();
                    while (!stack.is_empty() && stack.top() != '(') {
                        temp.insert(0, stack.pop()); // Fügen Sie Zeichen am Anfang ein
                    }
                    
                    if (stack.is_empty()) {
                        return false; // Ungültige Klammerung
                    }
                    
                    // Die öffnende Klammer entfernen
                    stack.pop();
    
                    if (!isPalindrome(temp.toString())) {
                        return false;
                    }
                    // Ein gültiges Palindrom in Klammern wurde gefunden
                    // Wir behandeln es als ein Stern (*) im Text
                    if (!stack.is_empty() && stack.top() == '*') {
                        // Wenn das Top-Element ein Stern ist, entferne es
                        stack.pop();
                    } else {
                        // Andernfalls füge einen Stern hinzu
                        stack.push('*');
                    }
                    break;
                case '*':
                    // Sterne dürfen nicht direkt im Text stehen
                    return false;
                default: // Für a-Z
                    if (!Character.isAlphabetic(c)) {
                        return false; // Ungültiges Zeichen gefunden
                    }
                    if (!stack.is_empty() && stack.top() == '*') {
                        // Wenn das Top-Element ein Stern ist, entferne es
                        stack.pop();
                    } else {
                        // Andernfalls füge das Zeichen hinzu
                        stack.push(c);
                    }
            }
        }
    
        // Überprüfen, ob der verbleibende Stapelinhalt ein Palindrom ist
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
        String text = "(abc(aha)(u)cba)"; // Beispieltext für einen T-Palindrom
        System.out.println(text + " : " + isTPalindrome(text));
    }
}
