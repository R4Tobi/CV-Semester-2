# Blatt 8 Aufgabe 1.1

## Minimale Anzahl von Schlüsseln in einem B-Baum

Für einen B-Baum der Ordnung `t` und der Höhe `h`, errechnet sich die minimale Anzahl an Schlüsseln wie folgt:

Minimale Anzahl = `1 + (t-1) * Σ(t^i) für i = 1 bis h-1`

## Maximale Anzahl von Schlüsseln in einem B-Baum

Für einen B-Baum der Ordnung `t` und der Höhe `h`, errechnet sich die maximale Anzahl an Schlüsseln wie folgt:

Maximale Anzahl = `Σ((2t-1) * t^i) für i = 0 bis h-1`

## Anwendung auf unseren Fall

`t = 2`, `h = 3`

$$ n_{min} = 1 + (2 - 1) * \sum\nolimits_{i=1}^{3-1}(t^i) = 1 + 6 = 7 $$

$$ n_{max} = \sum\nolimits_{i = 0}^{3 - 1}((2(2) - 1) * 2^i) = (3 * 1) + (3 * 2) + (3 * 4) = 21 $$
