# Blatt 11 - Aufgabe 2

## 1. Erläutere den in der Vorlesung vorgestellten Dijkstra-Algorithmus

### Dijkstra-Algorithmus:

Setze den Startknoten (0) auf eine Entfernung von 0 und alle anderen Knoten auf unendlich.
Markiere alle Knoten als unbesucht. Setze den Startknoten als aktuellen Knoten.
Für den aktuellen Knoten:
Aktualisiere die Entfernungen zu allen benachbarten Knoten.
Für jeden Nachbarn: Wenn die berechnete Entfernung kleiner ist als die aktuell bekannte Entfernung, aktualisiere die Entfernung.
Markiere den aktuellen Knoten als besucht. Ein besuchter Knoten wird nicht erneut geprüft.
Wähle den unbesuchten Knoten mit der kleinsten Entfernung als neuen aktuellen Knoten und wiederhole Schritt 3-5, bis alle Knoten besucht sind oder der kleinste Abstand zu einem unbesuchten Knoten unendlich ist.
Schritte für das gegebene Beispiel:

#### Initialisierung

Knoten: 0, 1, 2, 3, 4, 5
Entfernungen: [0, ∞, ∞, ∞, ∞, ∞]
Vorgänger: [None, None, None, None, None, None]
Knoten 0:

#### Entfernungen zu Nachbarn aktualisieren

0 -> 1: Entfernung 2
0 -> 2: Entfernung 5
Neue Entfernungen: [0, 2, 5, ∞, ∞, ∞]
Markiere Knoten 0 als besucht.
Knoten 1:

#### Entfernungen zu Nachbarn aktualisieren

1 -> 3: Entfernung 2 + 1 = 3
Neue Entfernungen: [0, 2, 5, 3, ∞, ∞]
Markiere Knoten 1 als besucht.
Knoten 3:

#### Entfernungen zu Nachbarn aktualisieren

3 -> 2: Entfernung 3 + 3 = 6 (keine Aktualisierung, da 5 kleiner ist)
3 -> 4: Entfernung 3 + 8 = 11
3 -> 5: Entfernung 3 + 4 = 7
Neue Entfernungen: [0, 2, 5, 3, 11, 7]
Markiere Knoten 3 als besucht.
Knoten 2:

#### Entfernungen zu Nachbarn aktualisieren

2 -> 4: Entfernung 5 + 7 = 12 (keine Aktualisierung, da 11 kleiner ist)
2 -> 5: Entfernung 5 + 8 = 13 (keine Aktualisierung, da 7 kleiner ist)
Neue Entfernungen: [0, 2, 5, 3, 11, 7]
Markiere Knoten 2 als besucht.
Knoten 5:

#### Keine Nachbarn mit kleineren Entfernungen

Neue Entfernungen: [0, 2, 5, 3, 11, 7]
Markiere Knoten 5 als besucht.
Knoten 4:

#### Keine Nachbarn mit kleineren Entfernungen

Neue Entfernungen: [0, 2, 5, 3, 11, 7]
Markiere Knoten 4 als besucht.
Die kürzesten Entfernungen von Knoten 0 zu allen anderen Knoten sind: [0, 2, 5, 3, 11, 7]

## 2. Konstruiere einen (möglichst einfachen) gewichteten Graphen, bei dem sich für mindestens einen Knoten während der Anwendung des Algorithmus von Dijkstra die Approximation für die Distanz mindestens zweimal ändert

```plaintext
    A
   / \
  1   5
 /     \
B-------C
 \     /
  2   2
   \ /
    D

```

Entfernungen:

- Initial: [0, ∞, ∞, ∞] (Startknoten A)
- Nach A: [0, 1, 5, ∞]
- Nach B: [0, 1, 4, 3] (B aktualisiert C und D)
- Nach D: [0, 1, 3, 3] (D aktualisiert C)

Hier ändert sich die Entfernung zu Knoten C zweimal: erst von ∞ auf 5, dann auf 4, dann auf 3.

## 3. Gib einen (möglichst einfachen) Graphen an, mit dem Du illustrierst, dass der Algorithmus von Dijkstra im Falle negativer Kantengewichte im Allgemeinen nicht korrekt arbeitet

```plaintext
    A
   / \
  1   -3
 /     \
B-------C
 \     /
  4   2
   \ /
    D
```

Entfernungen:

- Initial: [0, ∞, ∞, ∞] (Startknoten A)
- Nach A: [0, 1, ∞, ∞]
- Nach B: [0, 1, 2, ∞] (B -> C)
- Nach C: [0, 1, -2, ∞] (C -> D)

Hier führt das negative Kantengewicht zu einer falschen Distanzberechnung, da Dijkstra den Knoten B nicht mehr prüft, nachdem er einmal besucht wurde. Dies zeigt, dass Dijkstra im Falle negativer Kantengewichte nicht korrekt funktioniert.
