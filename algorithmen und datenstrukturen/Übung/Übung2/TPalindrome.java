package Übung2;

import aud.Stack;

/**
 * Die Klasse TPalindrome bietet eine Methode zur Überprüfung, ob ein Text ein sogenanntes "T-Palindrom" ist.
 * Ein T-Palindrom ist ein Text, der als Palindrom gelesen werden kann, wobei Inhalt in runden Klammern ignoriert wird,
 * wenn dieser ebenfalls ein Palindrom ist. Zusätzlich verarbeitet die Methode auch Sternchen (*) im Text.
 */
public class TPalindrome {
    
    /**
     * Überprüft, ob der übergebene Text ein T-Palindrom ist.
     *
     * @param text Der zu überprüfende Text.
     * @return true, wenn der Text ein T-Palindrom ist, sonst false.
     */
    public static boolean isTPalindrome(String text) {
        Stack<Character> stack = new Stack<>();
        
        // Durchläuft den gesamten Text Buchstabe für Buchstabe
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Verarbeitung einer öffnenden Klammer
            if (c == '(') {
                stack.push(c);
            } 
            // Verarbeitung einer schließenden Klammer
            else if (c == ')') {
                StringBuilder temp = new StringBuilder();
                // Extrahieren des Inhalts zwischen den Klammern
                while (!stack.is_empty() && stack.top() != '(') {
                    temp.insert(0, stack.pop());
                }
                
                // Überprüfen auf ungültige Klammerung
                if (stack.is_empty()) {
                    return false;
                }
                
                // Entfernen der öffnenden Klammer
                stack.pop();

                // Überprüfen, ob der Inhalt zwischen den Klammern ein Palindrom ist
                if (isPalindrome(temp.toString())) {
                    // Sternchen-Logik nach dem Palindrom
                    if (!stack.is_empty() && stack.top() == '*') {
                        stack.pop();
                    } else {
                        stack.push('*');
                    }
                } else {
                    return false; // Kein Palindrom zwischen den Klammern
                }
            } 
            // Verarbeitung alphabetischer Zeichen
            else if (Character.isAlphabetic(c)) {
                // Sternchen-Logik
                if (!stack.is_empty() && stack.top() == '*') {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            } 
            // Ungültiges Zeichen gefunden
            else {
                return false;
            }
        }

        // Überprüfung des restlichen Stapel-Inhalts auf Palindrom
        StringBuilder remaining = new StringBuilder();
        while (!stack.is_empty()) {
            remaining.append(stack.pop());
        }

        return isPalindrome(remaining.toString());
    }

    /**
     * Hilfsmethode zur Überprüfung, ob ein String ein Palindrom ist.
     *
     * @param str Der zu überprüfende String.
     * @return true, wenn str ein Palindrom ist, sonst false.
     */
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

    /**
     * Hauptmethode zum Testen der isTPalindrome Methode mit unterschiedlichen Strings.
     *
     * @param args Kommandozeilenargumente, nicht verwendet.
     */
    public static void main(String[] args) {
        String[] true_ = new String[]{"abc(ah(otto)v(atta)ha)cba", "(*)", "otto", "al(otto)la", "abc(aha)(u)cba" , "abc(ah(otto)v(atta)ha)cba"}; // Strings, die T-Palindrome sein sollten
        String[] false_ = new String[] { "a(b)cca(b)", "abc", "abc(ah(otto)h)cba" }; // Strings, die keine T-Palindrome sein sollten
        
        // Testen der Strings, die T-Palindrome sein sollten
        for(String item : true_){
            System.out.println(item + ":" + isTPalindrome(item));
        }

        // Testen der Strings, die keine T-Palindrome sein sollten
        for (String item : false_) {
            System.out.println(item + ":" + isTPalindrome(item));
        }
    }
}
