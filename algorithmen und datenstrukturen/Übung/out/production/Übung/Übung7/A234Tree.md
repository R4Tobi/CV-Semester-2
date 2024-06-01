# Balanced Tree: 234-Tree

Ein 2-3-4 Baum kann Knoten mit 2, 3 oder 4 Unterknoten haben:

Ein 2-Knoten hat einen Wert und zwei Kinder.
Ein 3-Knoten hat zwei Werte und drei Kinder.
Ein 4-Knoten hat drei Werte und vier Kinder.

3, 7, 5, 15, 17, 9, 13, 21, 11, 19

## Bottom-Up Einfügen

```plaintext
3|7|5
```

```plaintext
  7
 / \
3  5|15|17
```

```plaintext
  7|15
 / | \
3  5  9|17
```

```plaintext
  7|15
 / | \
3  5  9|13|17
```

```plaintext
 7|13|15
 / | | \
3  5  9 11|17|21
```

```plaintext
    13
   /  \
  7    15|17
 / \   / | \
3  5  9 11  21
```

## Top-Down Einfügen

```plaintext
3|7|5
```

```plaintext
7
/ \
3  5|15|17
```

```plaintext
7|15
/ | \
3  5  9|17
```

```plaintext
7|15
/ | \
3  5  9|13|17
```

```plaintext
 7|13|15
 / | | \
3  5  9 17|21
```

```plaintext
   13
  /  \
 7    15
/ \   / \
3  5  9 17|11|21
```

```plaintext
       13
      /  \
     7   15|11
    / \   / \
   3   5 9  17|19|21
```

3, 5, 7, 9, 11, 13, 15, 17, 19, 21

## Bottom-Up Einfügen

```plaintext
3|5|7
```

```plaintext
   5
  / \
 3   7|9|11
```

```plaintext
   5|9
  / | \
 3  7  11|13|15
```

```plaintext
  5|9|13
  / | | \
3  7  11 15|17|19
```

```plaintext
    9
   / \
  5  13|15
 / \   / | \
3  7  11 17  19|21
````

## Top-Down Einfügen

```plaintext
3|5|7
```

```plaintext
   5
  / \
 3   7|9|11
```

```plaintext
   5|9
  / | \
 3  7  11|13|15
```

```plaintext
  5|9|13
  / | | \
3  7  11 15|17
```

```plaintext
    9
   / \
  5    13
 / \   / \
3  7  11 15|17|19
```

```plaintext
       9
      / \
     5   13|17
    / \   / | \
   3   7 11 15 19|21
```
