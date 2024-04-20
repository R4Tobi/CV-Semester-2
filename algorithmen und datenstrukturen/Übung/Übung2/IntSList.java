package Übung2;

import java.util.function.Predicate;
import aud.SList;

/**
 * Eine Klasse zur Verwaltung einer einfach verketteten Liste von ganzzahligen
 * Daten.
 */
public class IntSList {
    SList<Integer> li;

    /**
     * Erzeugt eine neue Instanz von IntSList.
     */
    public IntSList() {
        this.li = new SList<>();
    }

    /**
     * Fügt ein Element am Anfang der Liste hinzu.
     * 
     * @param obj Das einzufügende Element.
     */
    public void push_front(int obj) {
        li.push_front(obj);
    }

    /**
     * Filtert die Liste anhand eines Prädikats und gibt eine neue Liste mit den
     * gefilterten Elementen zurück.
     * 
     * @param p Das Prädikat, das angibt, welche Elemente gefiltert werden sollen.
     * @return Eine neue IntSList mit den gefilterten Elementen.
     */
    public IntSList filter(Predicate<Integer> p) {
        IntSList filteredList = new IntSList();
        for (Integer item : li) {
            if (p.test(item)) {
                filteredList.push_front(item);
            }
        }
        return filteredList;
    }

    /**
     * Hauptmethode zum Testen der IntSList-Klasse.
     * 
     * @param args Die Befehlszeilenargumente (nicht verwendet).
     */
    public static void main(String[] args) {
        IntSList list = new IntSList();
        list.push_front(85);
        list.push_front(72);
        list.push_front(93);
        list.push_front(81);
        list.push_front(74);
        list.push_front(42);
        list.push_front(34);
        list.push_front(21);
        list.push_front(17);
        list.push_front(12);

        System.out.println("Original list: " + list.li);

        // Test 1: Filter even numbers
        IntSList evenNumbers = list.filter(num -> num % 2 == 0);
        System.out.println("Filtered even numbers: " + evenNumbers.li);

        // Test 2: Filter numbers greater than 42
        IntSList greaterThan42 = list.filter(num -> num > 42);
        System.out.println("Filtered numbers greater than 42: " + greaterThan42.li);
    }
}