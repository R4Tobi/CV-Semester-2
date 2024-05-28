# RedBlack Tree

- Jeder Knoten ist rot oder schwarz gefärbt
- Die Wurzel ist schwarz gefärbt
- Jedes Blatt ist schwarz gefärbt
- Rote Knoten haben nur schwarze Kinder
- Jeder Pfad von einem Knoten zu einem Blatt hat die gleiche Anzahl an schwarzen Knoten
- nie größer als 2*log(n+2)-2

## 2. Rot-Schwarz Bäume - Algorithmus

6, 7, 3, 4, 2, 1
'#' = schwarz

```plaintext
     6#
    / \
   3   7
```

```plaintext
   6#
  / \
#3  #7
/ \
   4
```

```plaintext
     6#
    / \
   #3  #7
   / \
  2  4
```

```plaintext
       6#
      / \
     #3  #7
    / \
   2   4
  /
 1
```

```plaintext
       6#
      / \
     #3  #7
    / \
  #2   #4
  /
 1
```

## AVL Baum anders Wurzel 3

```plaintext
In einem 2-3-4-Baum kann jeder Knoten zwei, drei oder maximal vier Kinder besitzen und entsprechend ein,
zwei oder maximal drei Datenelemente speichern. In einem Rot-Schwarz-Baum wird jeder Knoten durch einen
oder mehrere Binärknoten dargestellt, wobei die "Farbe" (rot oder schwarz) des Knotens Informationen über die Struktur des ursprünglichen 2-3-4-Baumknotens trägt.
```
