# Übungsblatt 1

## Form

- √(5) => Quadratwurzel aus 5
- [1,2,3] => Vektor
- |x| => Betrag von x

## Aufgabe 1.1: Normalisierung

a) [3,2,9]

- Euklidische Form: `√(3^2 + 2^2 + 9^2) = √(94) ≈ 9,695359`
- Normalisierung: `|[3,2,9]| ∙ x = 1; √(94) ∙ x = 1; x = 1/√(94) ≈ 0,103142`

b) [-5,0,0]

- Euklidische Form: `√((-5)^2 + 0^2 + 0^2) = √(25) = 5`
- Normalisierung: `|[-5,0,0]| ∙ x = 1; 5 ∙ x = 1; x = 1/5 = 0,2`

c) [0,-1,6]

- Euklidische Form: `√(0^2 + (-1)^2 + 6^2) = √(37) ≈ 6,082762`
- Normalisierung: `|[0,-1,6]| ∙ x = 1; √(37) ∙ x = 1; x = 1/√(37) ≈ 0,164398`

## Aufgabe 1.2: Skalarprodukt

a) [3,2,-1] ∘ [-1,5,2]

- Ergebnis: `= 3 ⋅ (-1) + 2 ⋅ 5 + (-1) ⋅ 2 = 5`
- geometrische Interpretation: Die Vektoren sind nicht senkrecht zueinander.

b) [9,0,2] ∘ [0,-4,0]

- Ergebnis: `= 9 ⋅ 0 + 0 ⋅ (-4) + 2 ⋅ 0 = 0`
- geometrische Interpretation: Die Vektoren sind orthogonal zueinander.

c) 1/6 ∙ [4,2,4] ∘ [-2,-1,-2]

- Ergebnis: `= 1/6 ∙ 4 ⋅ (-2) + 1/6 ∙ 2 ⋅ (-1) + 1/6 ∙ 4 ⋅ (-2) = -3`
- geometrische Interpretation: Die Vektoren sind nicht orthogonal zueinander.

## Aufgabe 1.3: Kreuzprodukt

a)

[1,-3,2] x [3,7,4] = [-26,2,16]

b)

[0,0,1] x [1,1,0] = [-1,1,0]

- Der Ergebnisvektor ist senkrecht zu beiden anderen vektoren. Der Betrag der Länge des Ergebnisvektors ist gleich der Fäche des Parraleleogramms, das von Vektor a und b aufgespannt werden.

## Aufgabe 1.4: Eigenschaften des Kreuzprodukts

1. Zeigen, dass a ∘ c = 0:

   a ∘ c = a ∘ (a × b)

   Verwenden wir die Eigenschaft des Skalarprodukts, dass a ∘ (b × c) = (a × b) ∘ c, dann haben wir:

   a ∘ (a × b) = (a × a) ∘ b

   Da das Kreuzprodukt zweier gleicher Vektoren null ist, wird a × a = 0:

   a ∘ c = 0

2. Zeigen, dass b · c = 0:

   b ∘ c = b ∘ (a × b)

   Verwenden wir wieder die Eigenschaft des Skalarprodukts, dass a ∘ (b × c) = (a × b) ∘ c, dann haben wir:

   b ∘ (a × b) = (b × b) ∘ a

   Da das Kreuzprodukt zweier gleicher Vektoren null ist und das Skalarprodukt kommutativ ist, wird b × b = 0:

   b ∘ c = 0

Da a ∘ c = 0 und b ∘ c = 0, ist der Vektor c orthogonal zu sowohl a als auch b.

## Aufgabe 1.5: Fehlersuche

a) sin^-1: -0,929611

b) cos^-1: 2,500407

- Beide Vektoren spannen ein Dreieck auf
- der cosinus-Wert ist der Richige, weil wir eine Ankathete und die Hypotenuse mit den Vektoren gegeben haben
- Beim Sinus würde die Gegenkathete verwendet werden, die haben wir in unseren Vektoren aber nicht

## Aufgabe 1.6: Vektorzerlegung

1. zu b parralel:

a<sub>||</sub> = (a ∙ b)/(|b|<sup>2</sup>) ∙ b = (7)/(5) ∙ b = [-(28)/(5),(21)/(25)]

2. zu b orthogonal:
a<sub>T</sub> = a - a<sub>||</sub> = [-(28)/(5),(21)/(25)] - [2,5] = [-(38)/(5),-(104)/(25)]
