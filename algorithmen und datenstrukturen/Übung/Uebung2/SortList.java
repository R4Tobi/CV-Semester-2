package Uebung2;

import aud.SList;

public class SortList<T extends Comparable<T>> {
    SList<T> li;

    /**
     * Erzeugt eine neue Instanz von SortList.
     */
    public SortList() {
        this.li = new SList<>();
    }

    /**
     * Fügt ein Element in die sortierte Liste ein, falls es noch nicht vorhanden ist.
     * @param obj Das einzufügende Objekt.
     * @return true, wenn das Element eingefügt wurde, sonst false.
     */
    public boolean insert(T obj) {
        if (this.li.empty()) { // Überprüfe ob die Liste leer ist
            this.li.push_front(obj);
            return true;
        }else{
            return false;
        }
    }
}
