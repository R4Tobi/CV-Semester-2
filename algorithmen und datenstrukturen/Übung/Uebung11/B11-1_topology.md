# Blatt 11 - Aufgabe 1

## A. Graph der Aufgaben und Topologische Sortierung

### Aufgabenliste

- Mathehausaufgaben machen
- Abwaschen
- Partymusik bei Spotify / iTunes zusammenstellen
- Mathebuch aus Bibliothek holen
- In die Stadt fahren
- AuD-Übung vorbereiten
- Freund / Freundin vom Bahnhof abholen
- Schuhe putzen
- Internet-Recherche nach Musik
- Computer aufräumen
- Müll rausbringen
- Computer am Netz anschließen
- Spülmaschine leeren
- AuD Aufgaben bearbeiten
- Cola kaufen
- Aus der Stadt zurückkommen

### Abhängigkeiten (Annahmen)

- Für 9\. Internet-Recherche nach Musik muss 12. Computer am Netz anschließen erledigt sein.
- Für 3\. Partymusik bei Spotify / iTunes zusammenstellen muss 9. Internet-Recherche nach Musik abgeschlossen sein.
- 6\. AuD-Übung vorbereiten benötigt 4. Mathebuch aus Bibliothek holen.
- 14\. AuD Aufgaben bearbeiten braucht 6. AuD-Übung vorbereiten.
- 7\. Freund / Freundin vom Bahnhof abholen benötigt 5. In die Stadt fahren.
- 15\. Cola kaufen benötigt 5. In die Stadt fahren.
- 16\. Aus der Stadt zurückkommen benötigt 7. Freund / Freundin vom Bahnhof abholen und 15. Cola kaufen.
- 8\. Schuhe putzen sollte vor 5. In die Stadt fahren erledigt werden.

### Erstellung eines gerichteten azyklischen Graphen (DAG)

- Um einen DAG mit mindestens 20 Kanten zu erstellen, fügen wir zusätzliche Abhängigkeiten hinzu:
  - 1\. Mathehausaufgaben machen sollte vor 14. AuD Aufgaben bearbeiten erledigt werden.
  - 2\. Abwaschen sollte vor 13. Spülmaschine leeren erledigt werden.
  - 11\. Müll rausbringen sollte vor 12. Computer am Netz anschließen erledigt werden.
  - 10\. Computer aufräumen sollte vor 3. Partymusik bei Spotify / iTunes zusammenstellen erledigt werden.
  - 9\. Internet-Recherche nach Musik sollte vor 3. Partymusik bei Spotify / iTunes zusammenstellen erledigt werden.
  - 13\. Spülmaschine leeren sollte vor 8. Schuhe putzen erledigt werden.
- Mit diesen zusätzlichen Abhängigkeiten haben wir mindestens 20 Kanten.

### Topologische Sortierung

1. Müll rausbringen (11)
2. Computer am Netz anschließen (12)
3. Internet-Recherche nach Musik (9)
4. Partymusik bei Spotify / iTunes zusammenstellen (3)
5. Mathehausaufgaben machen (1)
6. Mathebuch aus Bibliothek holen (4)
7. AuD-Übung vorbereiten (6)
8. AuD Aufgaben bearbeiten (14)
9. Computer aufräumen (10)
10. Spülmaschine leeren (13)
11. Abwaschen (2)
12. Schuhe putzen (8)
13. In die Stadt fahren (5)
14. Freund / Freundin vom Bahnhof abholen (7)
15. Cola kaufen (15)
16. aus der Stadt zurückkommen (16)

#### Erklärung des Algorithmus zur Topologischen Sortierung

Die Topologische Sortierung eines DAG ist eine lineare Anordnung von Knoten, bei der für jede gerichtete Kante 𝑢𝑣 von Knoten 𝑢 zu Knoten 𝑣 𝑢 vor 𝑣 in der Anordnung kommt.

Der Algorithmus umfasst im Allgemeinen:

1. Identifizieren der Knoten ohne eingehende Kanten.
2. Entfernen dieser Knoten und ihrer ausgehenden Kanten aus dem Graphen.
3. Wiederholen des Prozesses mit der nächsten Menge von Knoten ohne eingehende Kanten.
4. Dieser Vorgang wird fortgesetzt, bis alle Knoten entfernt sind.

## B. Entfernen von Kanten im Rock-Paper-Scissors-Lizard-Spock Graphen

Im Rock-Paper-Scissors-Lizard-Spock Graph hat jeder Knoten zwei eingehende und zwei ausgehende Kanten, die einen Zyklus bilden. Um den Graphen azyklisch zu machen, muss mindestens eine Kante von jedem Knoten entfernt werden, um sicherzustellen, dass keine Zyklen existieren.

Minimale Anzahl der zu entfernenden Kanten:
Jeder Knoten verbindet sich mit zwei anderen Knoten und bildet einen Zyklus. Um den Zyklus zu brechen und eine topologische Sortierung zu ermöglichen:

Entferne eine Kante von jedem Paar von Knoten (jedes Knotenpaar hat zwei gerichtete Kanten zwischen ihnen).
Dies stellt sicher, dass keine Zyklen existieren und der Graph azyklisch wird. Somit müssen mindestens:

5 Kanten (eine für jeden Knoten) entfernt werden.

Dadurch bleibt die Struktur erhalten, die einen gerichteten Pfad ohne Zyklen ermöglicht und eine topologische Sortierung möglich macht.
