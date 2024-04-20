package Übung2;

import aud.Stack;

/**
 * Die Klasse TPalindrome bietet eine Methode zum Überprüfen von String auf T-Palindrom-Eigenschaften. Das t_Palindrom ist eine Erfindung von Prof. Tönnies, OvGU
 * Ein T-Palindrom ist ein Text, der auch nach Ersetzung aller palindromischen Substrings in Klammern durch einen Stern ein Palindrom bleibt. z.B. lage(otto)rr*egal -> lage*rr*egal
 */
public class TPalindrome {
    /**
    * Überprüft, ob ein gegebener Text ein T-Palindrom ist.
    *
    * @param text Der zu überprüfende Text.
    * @return true, wenn der Text ein T-Palindrom ist, sonst false.
    */
    public static boolean isTPalindrome(String text) {
        Stack<Character> stack = new Stack<>();
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            if (c == '(') {
                // Beginne das Einsammeln von Zeichen bis zur schließenden Klammer
                stack.push(c);
            } else if (c == ')') {
                // Sammle alle Zeichen im inneren der Klammer
                StringBuilder temp = new StringBuilder();
                while (!stack.is_empty() && stack.top() != '(') {
                    temp.insert(0, stack.pop());
                }
                
                if (!stack.is_empty() && stack.top() == '(') {
                    stack.pop(); // Entferne die öffnende Klammer
                    if (isPalindrome(temp.toString())) {
                        // Ersetze den palindromischen Teil in Klammern durch einen Stern
                        stack.push('*');
                    } else {
                        // Der Inhalt der Klammern ist kein Palindrom
                        return false;
                    }
                } else {
                    // Fehlende öffnende Klammer
                    return false;
                }
            } else {
                // Normales Zeichen oder Stern, einfach auf den Stack legen
                stack.push(c);
            }
        }

        // Überprüfe ob das, was auf dem Stack übrig bleibt, ein Palindrom ist
        StringBuilder remaining = new StringBuilder();
        while (!stack.is_empty()) {
            remaining.append(stack.pop());
        }

        return isPalindrome(remaining.toString());
    }
    /**
     * Hilfsmethode zum Überprüfen, ob ein gegebener String ein Palindrom ist. Aus Übung1 übernommen.
     *
     * @param str Der zu überprüfende String.
     * @return true, wenn der String ein Palindrom ist, sonst false.
     */
    private static boolean isPalindrome(String text) {
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

    /**
     * Hauptmethode zum Testen der TPalindrom-Überprüfung.
     * 
     * @param args Kommandozeilenargumente, nicht verwendet.
     */
    public static void main(String[] args) {
        String[] true_ = new String[]{"lage(otto)rr*egal", "abc(ah(otto)v(atta)ha)cba", "(*)", "otto", "al(otto)la", "abc(aha)(u)cba" , "abc(ah(otto)v(atta)ha)cba"}; // Beispieltexte für einen T-Palindrom
        String[] false_ = new String[] { "a(b)cca(b)", "abc", "abc(ah(otto)h)cba" };
        
        for(String item : true_){
            System.out.println(item + ":" + isTPalindrome(item));
        }

        for (String item : false_) {
            System.out.println(item + ":" + isTPalindrome(item));
        }
    }
}
