# Blatt 10 - Aufgabe 3

## 1. Breitensuche (BFS) ab Knoten 8

Die Breitensuche (Breadth-First Search, BFS) ist ein Algorithmus, der die Knoten eines Graphen in Schichten durchläuft. BFS beginnt bei einem Startknoten und erkundet zunächst alle benachbarten Knoten (die erste Schicht), bevor es zu den Knoten der nächsten Schicht übergeht.

**Algorithmusbeschreibung:**

1. Initialisiere eine Warteschlange und füge den Startknoten hinzu.
2. Markiere den Startknoten als besucht.
3. Solange die Warteschlange nicht leer ist:
   - Entferne den vordersten Knoten aus der Warteschlange.
   - Besuche alle unbesuchten Nachbarn des Knotens.
   - Füge jeden unbesuchten Nachbarn zur Warteschlange hinzu und markiere ihn als besucht.

**BFS-Reihenfolge und Distanzen:**

- Startknoten: 8
- Besuche die Knoten in aufsteigender Reihenfolge bei gleichen Distanzen.

Schritte der BFS:

1. Starte bei Knoten 8.
2. Nachbarn von 8: 1, 2, 3, 5 (in aufsteigender Reihenfolge).
3. Besuche Knoten 1, 2, 3, 5.
4. Nachbarn von 1: 2, 6, 7 (in aufsteigender Reihenfolge).
5. Besuche Knoten 6, 7.
6. Nachbarn von 2, 3, 5, 6, 7: 4 (in aufsteigender Reihenfolge).
7. Besuche Knoten 4.

Die Reihenfolge der besuchten Knoten und deren Distanzen von Knoten 8 sind:

- Knoten 8 (Distanz 0)
- Knoten 1 (Distanz 1)
- Knoten 2 (Distanz 1)
- Knoten 3 (Distanz 1)
- Knoten 5 (Distanz 1)
- Knoten 6 (Distanz 2)
- Knoten 7 (Distanz 2)
- Knoten 4 (Distanz 3)

## 2. Tiefensuche (DFS) ab Knoten 8

Die Tiefensuche (Depth-First Search, DFS) ist ein Algorithmus, der die Knoten eines Graphen so tief wie möglich erkundet, bevor er zu einem vorherigen Knoten zurückkehrt und andere Wege erkundet.

**Algorithmusbeschreibung:**

1. Starte bei einem Startknoten.
2. Markiere den aktuellen Knoten als besucht.
3. Besuche rekursiv alle unbesuchten Nachbarn des aktuellen Knotens.

**DFS-Reihenfolge:**

- Startknoten: 8
- Besuche die Knoten in aufsteigender Reihenfolge bei gleichen Optionen.

Schritte der DFS:

1. Starte bei Knoten 8.
2. Wähle den kleinsten unbesuchten Nachbarn von 8: 1.
3. Wähle den kleinsten unbesuchten Nachbarn von 1: 2.
4. Wähle den kleinsten unbesuchten Nachbarn von 2: 4.
5. Keine unbesuchten Nachbarn mehr bei 4, gehe zurück zu 2.
6. Keine unbesuchten Nachbarn mehr bei 2, gehe zurück zu 1.
7. Wähle den nächsten unbesuchten Nachbarn von 1: 6.
8. Keine unbesuchten Nachbarn mehr bei 6, gehe zurück zu 1.
9. Wähle den nächsten unbesuchten Nachbarn von 1: 7.
10. Keine unbesuchten Nachbarn mehr bei 7, gehe zurück zu 1.
11. Keine unbesuchten Nachbarn mehr bei 1, gehe zurück zu 8.
12. Wähle den nächsten unbesuchten Nachbarn von 8: 2 (bereits besucht).
13. Wähle den nächsten unbesuchten Nachbarn von 8: 3.
14. Keine unbesuchten Nachbarn mehr bei 3, gehe zurück zu 8.
15. Wähle den nächsten unbesuchten Nachbarn von 8: 5.
16. Keine unbesuchten Nachbarn mehr bei 5, gehe zurück zu 8.

Die Reihenfolge der besuchten Knoten ist:
- Knoten 8
- Knoten 1
- Knoten 2
- Knoten 4
- Knoten 6
- Knoten 7
- Knoten 3
- Knoten 5

Zusammengefasst:

- **BFS-Reihenfolge:** 8, 1, 2, 3, 5, 6, 7, 4
- **DFS-Reihenfolge:** 8, 1, 2, 4, 6, 7, 3, 5