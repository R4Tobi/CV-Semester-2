import Uebung8.MaxHeap;
package Uebung8;

// Wir nehmen an, dass die MaxHeap-Klasse bereits implementiert ist
public class HeapSort<T extends Comparable<T>> extends MaxHeap<T> {

    public HeapSort(T[] array) {
        super(array);
    }

    public HeapSort() {
        super();
    }

    // Methode zum Vertauschen des maximalen Elements (Wurzel) mit dem letzten
    // Element im Heap
    public void swapMax() {
        if (size() > 1) {
            swap(0, size() - 1); // Vertauscht das Wurzelelement (Index 0) mit dem letzten Element
            decreaseSize(); // Verringert die Größe des Heaps um 1
            maxHeapify(0); // Stellt die Max-Heap-Eigenschaft von der Wurzel aus wieder her
        }
    }

    public int size() {
        
    }

    // Implementierung des Heapsort-Algorithmus
    public void heapSort() {
        // Baut den Max-Heap auf
        buildMaxHeap();

        // Wiederholt swapMax, bis der Heap leer ist
        for (int i = size() - 1; i > 0; i--) {
            swapMax(); // Vertauscht das Maximum nach hinten und reduziert den Heap

            // Wir müssen sicherstellen, dass wir den reduzierten Heap korrekt behandeln
            // Das letzte Element wird jetzt nicht mehr als Teil des Heaps betrachtet
        }
    }

    // Hilfsmethode zum Aufbau des Max-Heaps
    private void buildMaxHeap() {
        for (int i = size() / 2 - 1; i >= 0; i--) {
            maxHeapify(i);
        }
    }

    // Hauptmethode zum Testen des Heapsort-Algorithmus
    public static void main(String[] args) {
        Integer[] array = { 3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5 };
        HeapSort<Integer> sorter = new HeapSort<>(array);
        sorter.heapSort();

        System.out.println("Sorted array:");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
    }
}