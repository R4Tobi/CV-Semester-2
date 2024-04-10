# Vorlesung 1

## Einführung

- [E-Learning](https://aud.vc.cs.ovgu.de/)

## Listen

- Folge von Daten und lineare Anordnung von einträgen
- Operationen:
  - sequentielle Suche
  - Einfügen und Löschen an beliebigen Positionen
- Anwendungsfälle
  - List mit head und tail
  - Stack
  - Queue
- Implementierung
  - Arrays oder verkettete Listen

[a,b,c] => [x,y,z]
head       tail

- head(x:L) = x //pop()
- tail(L:x) = L //push()

### Listen als Felder

- pushback(index), popback(index) als methoden
- capacity() / size() um Informationen über das Feld zu bekommen
- Listen sind klassen:
  - T[] data
  - attribute data und größe sind privat => nicht manipulierbar
- O(n) für jedes pushback, wo die größe verändert wird => es werden 3 Einträge statt einem hinzugefügt
- Kosten: cost(n) = n + Σ<sup>[n/k]</sup> <sub>[l+1]</sub> (k*i)
