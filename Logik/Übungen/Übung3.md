# Logik Übung 3

## 3.1

- a) Prädikate sind wahr oder falsch (abhängig von der Welt).
- b) Ein atomarer Satz besteht aus n Objekten.
- c) Wenn die logische Theorie {P1, P2,...,Pn} WT-erfullbar ist, dann sind die Sätze P1, P2,...,Pn alle wahr
- d) Wenn ein Satz P keine BW-Wahrheit ist, dann ist {P} WT-erfullbar.

## 3.2

- a) A -> Falsch: Keine Tautologie, wenn A wahr ist, muss die Implikation falsch sein
- b) (A ⋀ B) ⋁ (¬A ⋀ ¬B): keine Tautologie, wenn A und B unterschiedliche Wahrheitswerte haben, sind die in Klammern gesetzen Aussagen falsch
- c) ((A -> B) -> A) -> A: Tautologie,
- d) ((A ⋀ B) ⋁ C) <-> ((A ⋁ C) ⋀ (B ⋁ C)): Tautologie, Operation ⋁ ist distributiv

## 3.3

1. Für `¬A → B` gilt:
   - `¬A = W` und `B = W`
   - `¬A = W` und `B = F`
   - `¬A = F` und `B = W`
   - `¬A = F` und `B = F`

2. Für `¬(A ∧ B) ∨ C` gilt:
   - `A = W`, `B = W`, und `C = F`
   - `A = W`, `B = F`, und `C = W`
   - `A = F`, `B = W`, und `C` beliebig
   - `A = F`, `B = F`, und `C` beliebig

Mögliche Belegung: `A = F`, `B = W`, und `C` beliebig