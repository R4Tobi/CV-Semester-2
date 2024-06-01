package Uebung1;

import aud.Queue;

public class JosephusProblem {

    // Solve the Josephus problem for a list of persons and a number k
    // of persons to be skipped. The solution should "simulate" the
    // counting-out and return a queue that contains all persons in the
    // order they were counted out.
    //
    public static Queue<String> solve(String[] persons, int k) {
        Queue<String> queue = new Queue<>(persons.length);
        Queue<String> result = new Queue<>(persons.length);

        // Alle Personen in die Queue einreihen
        for (String person : persons) {
            queue.enqueue(person);
        }

        while (!queue.is_empty()) {
            // k-1 Personen werden von vorne entnommen und hinten wieder angefügt
            for (int i = 1; i < k; i++) {
                queue.enqueue(queue.dequeue());
            }
            // Die k-te Person wird ausgezählt und in das Ergebnis eingefügt
            result.enqueue(queue.dequeue());
        }

        return result;
    }

    public static void main(String[] args) {
        String[] persons = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};
        Queue<String> result = JosephusProblem.solve(persons, 7);

        // Drucke die Liste der ausgezählten Personen
        System.out.println("Ausgezählte Personen: " + result.toString());
    }
  }