# Logik Übung Blatt 8

## 8.1

a) wahr

- Hornalgorithmus ist für Horn KLauseln konzipiert
- Horn KLauseln haben höchstens ein positives Literal
- kann effizient entscheiden, ob eine Menge von Horn-Klauseln erfüllbar ist
- kann nicht für allgemeine Sätze (beliebige Klauseln) verwendet werden

b) falsch

- Der Hornalgorithmus ist darauf ausgelegt zu überprüfen, ob eine Menge von Horn-Klauseln erfüllbar ist
- Der Hornalgorithmus kann jedoch nicht entscheiden, ob eine allgemeine Aussage eine Tautologie ist, da er nur für die spezielle Klasse der Horn-Klauseln funktioniert und Tautologien außerhalb dieser Klasse nicht behandelt

c) wahr

- vollständiger und korrekter Algorithmus zur Entscheidung der Erfüllbarkeit von Aussagenlogik-Formeln in konjunktiver Normalform
- kann für jeden Satz in Aussagenlogik bestimmen, ob er erfüllbar (WT-erfüllbar) ist.

d) falsch

- DPLL-Algorithmus prüft die Erfüllbarkeit von Aussagenlogik-Formeln
- DPLL-Algorithmus kann nicht direkt bestimmen, ob ein Satz eine Tautologie ist, da er nicht dafür entwickelt wurde, die Unerfüllbarkeit der Negation zu prüfen, sondern nur die Erfüllbarkeit einer gegebenen Formel

## 8.2

### a)

$
(A \lor B \lor \neg C) \land (A \lor \neg B \lor C) \land (\neg A \lor B \lor C) \land (\neg A \lor \neg B \lor \neg C)
$

Fall 1: \(A = \text{wahr}\)

- Ersetzen von \(A\) durch wahr in allen Klauseln:

$\text{Resultierende Klauseln:} \quad (B \lor \neg C), (\neg B \lor C), (\neg A \lor \neg B \lor \neg C)$

- Die Klausel \((\neg A \lor \neg B \lor \neg C)\) bleibt als \((\neg B \lor \neg C)\).

*Vereinfachte Formel:*

$(B \lor \neg C) \land (\neg B \lor C) \land (\neg B \lor \neg C)$

**Wahl eines Literals und Aufspaltung:**

- Wähle \(B\) als Literal.

Fall 1.1: \(B = \text{wahr}\)

$\text{Resultierende Klauseln:} \quad (B \lor \neg C), (\neg B \lor C) \Rightarrow \text{widersprüchlich} (\text{Konflikt mit } \neg B)$

Fall 1.2: \(B = \text{falsch}\)

$\text{Resultierende Klauseln:} \quad (\neg C), (C), (\neg C) \Rightarrow \text{widersprüchlich} (\text{Konflikt mit } C)$

Beide Unterfälle von \(A = \text{wahr}\) führen zu einem Widerspruch.

Fall 2: \(A = \text{falsch}\)

- Ersetzen von \(A\) durch falsch in allen Klauseln:

$\text{Resultierende Klauseln:} \quad (B \lor \neg C), (\neg B \lor C), (B \lor C), (\neg B \lor \neg C)$

**Wahl eines Literals und Aufspaltung:**

- Wähle \(B\) als Literal.

Fall 2.1: \(B = \text{wahr}\)

$\text{Resultierende Klauseln:} \quad (\neg C), (C) \Rightarrow \text{widersprüchlich} (\text{Konflikt mit } \neg C)$

Fall 2.2: \(B = \text{falsch}\)

$\text{Resultierende Klauseln:} \quad (\neg C), (C) \Rightarrow \text{widersprüchlich} (\text{Konflikt mit } C)$

Beide Unterfälle von \(A = \text{falsch}\) führen zu einem Widerspruch.

**Schlussfolgerung für Satz a:**

- Unlösbar. Es gibt keine Zuweisung, die alle Klauseln gleichzeitig wahr macht.

### b)

$\neg A \land \neg B \land (B \lor A)$

**Schritte des DPLL-Algorithmus:**

1\. **Anwendung der Unit Propagation:**

- Unit-Klauseln: \(\neg A\) und \(\neg B\).

2\. **Ersetzen von \(\neg A\) und \(\neg B\):**

$\text{Resultierende Klauseln:} \quad (\neg A), (\neg B), (B \lor A)$

- Ersetzen von \(A\) und \(B\) durch falsch:

$\text{Resultierende Klauseln:} \quad \neg A, \neg B, \text{widersprüchlich}$


**Schlussfolgerung für Satz b:**

- Unlösbar. Es gibt keine Zuweisung, die alle Klauseln gleichzeitig wahr macht.

## 8.2

a)

$\text{Sa} = (A \lor \neg B \lor \neg D) \land \neg E \land (\neg C \lor A) \land (\neg B \lor D) \land B$

- **Hornformel:** Ja, da jede Klausel höchstens ein positives Literal enthält:
  - \(A \lor \neg B \lor \neg D\) (ein positives Literal: \(A\))
  - \(\neg E\) (kein positives Literal)
  - \(\neg C \lor A\) (ein positives Literal: \(A\))
  - \(\neg B \lor D\) (ein positives Literal: \(D\))
  - \(B\) (ein positives Literal: \(B\))

**Konditionale Form:**

- \(\neg B \land \neg D \implies A\)
- \(\neg E\)
- \(\neg C \implies A\)
- \(\neg B \implies D\)
- \(B\)

**Erfüllbarkeitsprüfung:**

1. Initiale Variablenbelegung: \( \{\} \)
2. \(B\) ist wahr aufgrund der Klausel \(B\).
3. \(\neg B\) ist falsch, daher sind die Klauseln \(\neg B \land \neg D \implies A\) und \(\neg B \implies D\) erfüllt.
4. \(\neg E\) muss wahr sein.
5. \(\neg C \implies A\) erfüllt, wenn \(\neg C\) wahr ist oder \(A\) wahr ist.

Erfüllbare Belegung:

- \(B = \text{wahr}, E = \text{falsch}\)
- \(A = \text{wahr} \text{ (wenn } C \text{ falsch) oder } A \text{ falsch (wenn } C \text{ wahr)}\)
  
Eine mögliche Belegung:

- \(B = \text{wahr}, E = \text{falsch}, C = \text{wahr}, A = \text{wahr}\)

**Erfüllbar:** Ja

b)

$\text{Sb} = (\neg A \lor B) \land (B \lor D) \land B$

- **Hornformel:** Ja, da jede Klausel höchstens ein positives Literal enthält:
  - \(\neg A \lor B\) (ein positives Literal: \(B\))
  - \(B \lor D\) (ein positives Literal: \(B\) oder \(D\))
  - \(B\) (ein positives Literal: \(B\))

**Konditionale Form:**

- \(\neg A \implies B\)
- \(\neg B \implies D\)
- \(B\)

**Erfüllbarkeitsprüfung:**

1. Initiale Variablenbelegung: \( \{\} \)
2. \(B\) ist wahr aufgrund der Klausel \(B\).
3. \(\neg A \implies B\) ist erfüllt.
4. \(\neg B\) ist falsch, daher \(D\) kann beliebig sein.

Erfüllbare Belegung:

- \(B = \text{wahr}\)

**Erfüllbar:** Ja

c)

$\text{Sc} = (\neg A \lor \neg B \lor \neg D) \land \neg E \land (\neg C \lor A) \land \neg C \land (\neg A \lor D)$

- **Hornformel:** Ja, da jede Klausel höchstens ein positives Literal enthält:
  - \(\neg A \lor \neg B \lor \neg D\) (kein positives Literal)
  - \(\neg E\) (kein positives Literal)
  - \(\neg C \lor A\) (ein positives Literal: \(A\))
  - \(\neg C\) (kein positives Literal)
  - \(\neg A \lor D\) (ein positives Literal: \(D\))

**Konditionale Form:**
- \(\neg A \land \neg B \implies \neg D\)
- \(\neg E\)
- \(\neg C \implies A\)
- \(\neg C\)
- \(\neg A \implies D\)

**Erfüllbarkeitsprüfung:**
1. Initiale Variablenbelegung: \( \{\} \)
2. \(\neg E\) muss wahr sein.
3. \(\neg C\) muss wahr sein.
4. \(\neg C \implies A\) ist erfüllt, \(A\) muss wahr sein.
5. \(\neg A\) ist falsch, \(D\) muss wahr sein.
6. \(\neg A \land \neg B \implies \neg D\) ist erfüllt, da \(A\) wahr ist.

Erfüllbare Belegung:
- \(E = \text{falsch}, C = \text{falsch}, A = \text{wahr}, D = \text{wahr}\)

**Erfüllbar:** Ja

d)

$\text{Sd} = (\neg E \lor \neg B \lor \neg D) \land (\neg E \lor D) \land B \land \neg A \land (\neg C \lor E) \land C$


- **Hornformel:** Ja, da jede Klausel höchstens ein positives Literal enthält:
  - \(\neg E \lor \neg B \lor \neg D\) (kein positives Literal)
  - \(\neg E \lor D\) (ein positives Literal: \(D\))
  - \(B\) (ein positives Literal: \(B\))
  - \(\neg A\) (kein positives Literal)
  - \(\neg C \lor E\) (ein positives Literal: \(E\))
  - \(C\) (ein positives Literal: \(C\))

**Konditionale Form:**

- \(\neg E \land \neg B \implies \neg D\)
- \(\neg E \implies D\)
- \(B\)
- \(\neg A\)
- \(\neg C \implies E\)
- \(C\)

**Erfüllbarkeitsprüfung:**

1. Initiale Variablenbelegung: \( \{\} \)
2. \(B\) muss wahr sein.
3. \(\neg A\) muss wahr sein.
4. \(C\) muss wahr sein.
5. \(\neg C \implies E\) ist erfüllt, \(E\) muss wahr sein.
6. \(\neg E \implies D\) ist erfüllt, \(D\) muss wahr sein.

Erfüllbare Belegung:

- \(A = \text{falsch}, B = \text{wahr}, C = \text{wahr}, E = \text{wahr}, D = \text{wahr}\)

**Erfüllbar:** Ja

## 8.4

### Satz

$(\neg A \rightarrow \neg A) \land (B \rightarrow B)$

Um zu überprüfen, ob der Satz erfüllbar ist und ob es sich um eine Tautologie handelt, müssen wir zunächst die Implikationen in ihre äquivalente disjunktive Form umwandeln.

### Umwandlung in disjunktive Form

- \(\neg A \rightarrow \neg A\) ist äquivalent zu \(\neg (\neg A) \lor \neg A\), was \(A \lor \neg A\) ergibt.
- \(B \rightarrow B\) ist äquivalent zu \(\neg B \lor B\).

Somit lautet der umgewandelte Satz:
\[
(A \lor \neg A) \land (\neg B \lor B)
\]

### Überprüfung mittels DPLL Algorithmus

1. **Formel in konjunktiver Normalform (CNF):**
$(A \lor \neg A) \land (\neg B \lor B)$

2. **Schritte des DPLL-Algorithmus:**
   - Beide Klauseln \( (A \lor \neg A) \) und \( (\neg B \lor B) \) sind Tautologien, da sie immer wahr sind, unabhängig von der Belegung der Variablen \(A\) und \(B\).

### Erfüllbarkeit

Da jede Klausel eine Tautologie ist, ist der gesamte Ausdruck immer wahr, unabhängig von der Belegung der Variablen.

### Tautologie

Ein Satz ist eine Tautologie, wenn er unter jeder möglichen Belegung der Variablen wahr ist. Da beide Klauseln \( (A \lor \neg A) \) und \( (\neg B \lor B) \) immer wahr sind, ist auch der gesamte Satz eine Tautologie.

**Erfüllbar:** Ja, der Satz ist erfüllbar.

**Tautologie:** Ja, der Satz ist eine Tautologie.