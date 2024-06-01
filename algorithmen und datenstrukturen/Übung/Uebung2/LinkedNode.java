package Uebung2;

/**
 * Eine Klasse, die einen Knoten in einer einfach verketteten Liste darstellt.
 *
 * @param <T> Der Datentyp der im Knoten gespeicherten Daten.
 */
public class LinkedNode<T> {
    private T data_; // Die im Knoten gespeicherten Daten.
    private LinkedNode<T> next_; // Referenz auf den nächsten Knoten.

    /**
     * Konstruiert einen neuen LinkedNode mit den gegebenen Daten und einer null-Referenz für den nächsten Knoten.
     *
     * @param data Die im Knoten zu speichernden Daten.
     */
    public LinkedNode(T data) {
        this.data_ = data;
        this.next_ = null;
    }

    /**
     * Konstruiert einen neuen LinkedNode mit den gegebenen Daten und einer Referenz auf den nächsten Knoten.
     *
     * @param data Die im Knoten zu speichernden Daten.
     * @param next Die Referenz auf den nächsten Knoten.
     */
    public LinkedNode(T data, LinkedNode<T> next) {
        this.data_ = data;
        this.next_ = next;
    }

    /**
     * Gibt die im Knoten gespeicherten Daten zurück.
     *
     * @return Die im Knoten gespeicherten Daten.
     */
    public T getData() {
        return data_;
    }

    /**
     * Setzt die im Knoten zu speichernden Daten.
     *
     * @param data Die im Knoten zu speichernden Daten.
     */
    public void setData(T data) {
        this.data_ = data;
    }

    /**
     * Gibt die Referenz auf den nächsten Knoten zurück.
     *
     * @return Die Referenz auf den nächsten Knoten.
     */
    public LinkedNode<T> getNext() {
        return next_;
    }

    /**
     * Setzt die Referenz auf den nächsten Knoten.
     *
     * @param next Die neue Referenz auf den nächsten Knoten.
     */
    public void setNext(LinkedNode<T> next) {
        this.next_ = next;
    }

    /**
     * Gibt eine String-Repräsentation des Knotens und seiner nachfolgenden Knoten zurück.
     *
     * @return Eine String-Repräsentation des Knotens und seiner nachfolgenden Knoten.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(data_);
        LinkedNode<T> current = next_;
        while (current != null) {
            sb.append(" -> ");
            sb.append(current.data_);
            current = current.next_;
        }
        return sb.toString();
    }

    /**
     * Hauptmethode zum Testen der LinkedNode-Klasse.
     *
     * @param args Kommandozeilenargumente (werden hier nicht verwendet).
     */
    public static void main(String[] args) {
        // Erstellung von Knoten für Montag, Donnerstag und Freitag
        LinkedNode<String> monday = new LinkedNode<>("Montag");
        LinkedNode<String> thursday = new LinkedNode<>("Donnerstag");
        LinkedNode<String> friday = new LinkedNode<>("Freitag");

        // Ausgabe der initialen Liste
        System.out.println("Initiale Liste:");
        System.out.println(monday);
        System.out.println(thursday);
        System.out.println(friday);

        // Hinzufügen von "Mensa gehen" und "Vorlesung besuchen"
        monday.setNext(new LinkedNode<>("Mensa gehen", thursday));
        thursday.setNext(new LinkedNode<>("Vorlesung besuchen", friday));

        // Ausgabe der Liste nach dem Hinzufügen der Aufgaben
        System.out.println("\nListe nach dem Hinzufügen von Aufgaben:");
        System.out.println(monday);
        System.out.println(thursday);
        System.out.println(friday);

        // Weitere Knoten für Dienstag und Mittwoch hinzufügen
        LinkedNode<String> tuesday = new LinkedNode<>("Dienstag", thursday);
        monday.setNext(tuesday);
        LinkedNode<String> wednesday = new LinkedNode<>("Mittwoch", friday);
        tuesday.setNext(wednesday);

        // Ausgabe der Liste nach dem Hinzufügen von Dienstag und Mittwoch
        System.out.println("\nListe nach dem Hinzufügen von Dienstag und Mittwoch:");
        System.out.println(monday);
    }
}
