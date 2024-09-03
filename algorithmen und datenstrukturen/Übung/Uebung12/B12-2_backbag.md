# Blatt 12

## Aufgabe 2

### Aufgabe 1: Greedy-Prinzip

Um die Gegenstände mit dem Greedy-Prinzip zu packen, wählen wir ein Kriterium, das die Auswahl der Gegenstände leitet.
 Ein häufig verwendetes Kriterium für das Greedy-Prinzip beim Rucksackproblem ist das Verhältnis von Wert zu Größe (Value-to-Weight Ratio).

#### Berechnung der Value-to-Weight Ratios

- \( a_1: \frac{3}{3} = 1 \)
- \( a_2: \frac{5}{4} = 1.25 \)
- \( a_3: \frac{8}{6} \approx 1.33 \)
- \( a_4: \frac{9}{7} \approx 1.29 \)

Sortieren wir die Gegenstände nach dem Verhältnis von Wert zu Größe in absteigender Reihenfolge:

1. \( a_3: \frac{8}{6} \approx 1.33 \)
2. \( a_4: \frac{9}{7} \approx 1.29 \)
3. \( a_2: \frac{5}{4} = 1.25 \)
4. \( a_1: \frac{3}{3} = 1 \)

Nun packen wir die Gegenstände der Reihe nach in den Rucksack, bis dessen Kapazität von 15 erreicht ist:

1. Packe \( a_3 \) (Größe: 6, Wert: 8) – verbleibende Kapazität: 9
2. Packe \( a_4 \) (Größe: 7, Wert: 9) – verbleibende Kapazität: 2
3. \( a_2 \) und \( a_1 \) passen nicht mehr in den verbleibenden Platz.

Die Gesamtlösung nach dem Greedy-Prinzip ist also:
- Eingepackte Gegenstände: \( a_3, a_4 \)
- Gesamter Wert: \( 8 + 9 = 17 \)

#### Ist diese Lösung optimal?

Nein, das Greedy-Prinzip liefert nicht immer die optimale Lösung für das Rucksackproblem, insbesondere nicht, wenn es um das klassische 0/1-Rucksackproblem geht.
 Hier ist die Lösung suboptimal, was durch das Ausprobieren aller möglichen Kombinationen (z.B. durch dynamische Programmierung) bestätigt werden kann.

### Aufgabe 2: Dynamische Programmierung

Das Prinzip der dynamischen Programmierung beim Rucksackproblem (Knapsack Problem) beruht darauf, Teillösungen systematisch zu speichern und wiederzuverwenden, um die optimale Gesamtlösung zu finden.

#### Ansatz der Dynamischen Programmierung

Wir definieren \( dp[i][w] \) als den maximalen Wert, der mit den ersten \( i \) Gegenständen und einer Kapazität \( w \) erreicht werden kann.

1. **Initialisierung**:
   - \( dp[0][w] = 0 \) für alle \( w \) (0 Gegenstände bedeuten 0 Wert)
   - \( dp[i][0] = 0 \) für alle \( i \) (Rucksackkapazität 0 bedeutet 0 Wert)

2. **Rekursive Formel**:
   - Wenn \( \text{Größe}(i) > w \), dann \( dp[i][w] = dp[i-1][w] \)
   - Andernfalls \( dp[i][w] = \max(dp[i-1][w], dp[i-1][w-\text{Größe}(i)] + \text{Wert}(i)) \)

3. **Füllen der Tabelle**:
   - Wir berechnen die Werte für \( dp[i][w] \) für \( i = 1 \) bis \( n \) und \( w = 1 \) bis \( W \).

#### Berechnung für dieses Problem

| i | Größe \( w \) | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  | 8  | 9  | 10 | 11 | 12 | 13 | 14 | 15 |
|---|---------------|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|
| 0 |               | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  | 0  |
| 1 |               | 0  | 0  | 0  | 3  | 3  | 3  | 3  | 3  | 3  | 3  | 3  | 3  | 3  | 3  | 3  | 3  |
| 2 |               | 0  | 0  | 0  | 3  | 5  | 5  | 5  | 8  | 8  | 8  | 8  | 8  | 8  | 8  | 8  | 8  |
| 3 |               | 0  | 0  | 0  | 3  | 5  | 5  | 8  | 9  | 10 | 11 | 13 | 14 | 14 | 16 | 17 | 18 |
| 4 |               | 0  | 0  | 0  | 3  | 5  | 5  | 8  | 9  | 12 | 14 | 14 | 17 | 18 | 18 | 18 | 18 |

Das Ergebnis der dynamischen Programmierung für die Rucksackgröße 15 ist 18, was durch die Gegenstände \( a_2, a_3, a_4 \) erreicht wird. Diese Lösung ist optimal und höher als die Lösung des Greedy-Algorithmus.

#### Fazit

Die dynamische Programmierung liefert die optimale Lösung für das Rucksackproblem, während das Greedy-Prinzip eine schnelle, aber nicht immer optimale Lösung bietet.
