# Ad-Hoc Aufgabe

## umgekehrte polnische Notation

```pseudo
Funktion evaluierePolnischeNotation(ausdruck):
    stack = leerer Stack()

    // Schleife über den Ausdruck von rechts nach links
    für i von länge(ausdruck) - 1 bis 0:
        zeichen = ausdruck[i]

        wenn istOperand(zeichen):
            stack.push(int(zeichen))  // Konvertierung des Zeichens in einen Integer, falls nötig

        sonst wenn istOperator(zeichen):
            operand1 = stack.pop()
            operand2 = stack.pop()
            ergebnis = wendeOperatorAn(operand1, operand2, zeichen)
            stack.push(ergebnis)

    // Das letzte Element im Stack ist das Ergebnis
    return stack.pop()

// Hilfsfunktionen, die implementiert werden müssen:
Funktion istOperand(zeichen):
    // Überprüfe, ob das Zeichen ein gültiger Operand ist
    return istZahl(zeichen) oder istVariable(zeichen)

Funktion istOperator(zeichen):
    // Liste aller gültigen Operatoren
    gültigeOperatoren = ['+', '-', '*', '/']
    return zeichen in gültigeOperatoren

Funktion wendeOperatorAn(operand1, operand2, operator):
    // Wende den gegebenen Operator auf die Operanden an
    switch operator:
        Fall '+':
            return operand1 + operand2
        Fall '-':
            return operand1 - operand2
        Fall '*':
            return operand1 * operand2
        Fall '/':
            return operand1 / operand2
        // Füge weitere Operatoren nach Bedarf hinzu
```
