# Blatt 11  - Aufgabe 3 (1)

Hier sind die Schritte:

1. Wähle einen Startknoten. Wir beginnen mit Knoten 0.
2. Finde die Kante mit dem kleinsten Gewicht, die einen Knoten verbindet, der bereits im MST ist, mit einem Knoten, der noch nicht im MST ist.
3. Füge diese Kante und den verbundenen Knoten zum MST hinzu.
4. Wiederhole die Schritte 2 und 3, bis alle Knoten im MST enthalten sind.

Lass uns das Schritt für Schritt durchführen:

1. Startknoten: 0
    - Kanten: (0, 2) mit Gewicht 5, (0, 1) mit Gewicht 2
    - Wähle (0, 1) mit Gewicht 2
2. Knoten im MST: 0, 1
    - Kanten: (0, 2) mit Gewicht 5, (1, 2) mit Gewicht 1, (1, 3) mit Gewicht 6
    - Wähle (1, 2) mit Gewicht 1
3. Knoten im MST: 0, 1, 2
    - Kanten: (0, 2) mit Gewicht 5, (2, 3) mit Gewicht 8, (2, 4) mit Gewicht 9
    - Wähle (0, 2) mit Gewicht 5
4. Knoten im MST: 0, 1, 2, 3
    - Kanten: (2, 3) mit Gewicht 8, (2, 4) mit Gewicht 9, (3, 4) mit Gewicht 7
    - Wähle (3, 4) mit Gewicht 7
5. Knoten im MST: 0, 1, 2, 3, 4
    - Alle Knoten sind im MST enthalten.

Die Kanten des MST sind: (0, 1) mit Gewicht 2, (1, 2) mit Gewicht 1, (0, 2) mit Gewicht 5, (3, 4) mit Gewicht 7.

Die Summe der Kantengewichte des MST ist: 2 + 1 + 5 + 7 = 15.

Also ist die Summe der Kantengewichte des minimalen aufspannenden Baums (MST) 15.
