
Die Funktion whatRec(int n) berechnet die Summe der Ziffern einer ganzen Zahl n. 
**Berechnung:** Die Funktion zerlegt die Zahl n in ihre einzelnen Ziffern und addiert sie. Sie funktioniert wie folgt:

1. Wenn die Zahl n kleiner als 10 ist, besteht sie nur aus einer Ziffer. In diesem Fall gibt die Funktion einfach n zurück.
2. Andernfalls ruft die Funktion sich selbst rekursiv auf, um die Summe der Ziffern von n / 10 (ohne die letzte Ziffer) zu berechnen, und addiert dann diese Summe zur letzten Ziffer von n (n % 10).

**Beisüiel** whatRec(1234)

- whatRec(1234) ruft whatRec(123) auf und addiert 4.
- whatRec(123) ruft whatRec(12) auf und addiert 3.
- whatRec(12) ruft whatRec(1) auf und addiert 2.
- whatRec(1) gibt einfach 1 zurück.
- Die Rekursion endet, und die Summe 1 + 2 + 3 + 4 = 10 wird zurückgegeben.
