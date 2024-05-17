# Computergrafik

## Vorlesung 1 / Einführung

- [E-Learning](https://elearning.ovgu.de/enrol/index.php?id=16588)
- Einschreibeschlüssel: **raytracer**
- [vc.cs.ovgu.de](https://vc.cs.ovgu.de/index.php?article_id=792&clang=1)
- 2/3 aller Aufgaben (2/3 der Übungen, 2/3 der Programmieraufgaben) => Ziel eigener Raytracer
- Programmiersprache C++

### Übungen

- Programmieraufgaben alle 2-3 Wochen
- Übung jede Woche
- **Übung 1 für Grundlagen sehr wichtig!!!**

- mögliche Punkte.....49
- erreichte Punkte....35
- in Prozent..........71%

### Einführung

### was ist computergrafik

- grafiche Datenverarbeiteung
- "Methoden und Techniken um Daten zu und Von grafischen Displays über Computer"
- Bildsynthese(Erstellen) und Bildanalyse(Erkennen)

#### Richtungen Computergraphik

- Modellierung/Akquisition - Generirung der Bildbeschreibung in maschinell-interpretierbarer Sprache
- Bearbeitung (Processing) - Hierarchien, Segmentierung, Komprimierung, Umwandlungen
- Rendering (Darstellung) - Erzeugung des Bildes aus der Bildbeschreibung
- Interaktion - Manipulation des Bildes

#### Geschichte

- 1950er: vor allem Radare
- 1960er: erste 3D Modelle, Interaktion und erstes Display (IBM), erstes Bildspeicher Display
- 1970er: erste CAD/CAM Systeme, Raster-Scan-Prinzip => kommerzielle Displays, erste Simulatorenund graphische Programmiersprachen, entstehen der SIGGRAPH ~ 1000 Teilnehmer, **erste Verfahren zur Schattierten Objektdarstellung**
- Ende 1970er: Reflexion und Transparenz (Raytracing)
- 1980er: fotorealistische Computergrafiken + Filmeffekte mit Computergrafik, Animations-Software, Beleuchtungs-Simulation, Beginn von VR/AR
- 1990er: Kommunikationstechniken, Daten und Informations-Visualisierung, Graphik im Internet, Multimedia, Non-Photorealistic Rendering
- Ende 1990er: HDR, Entstehung von GPUs => Co-Prozessor für Grafikanwendungen

#### Anwendung

- Eigentlich Überall => Animationen, CAD, Simulation, Militärische Anwendugen, Computerspiele

### Raytracing - ganz kurze Einführung

- Projektion und Rasterisisierung / Raytracing als Ansätze
- Projektion und Rasterisierung
  - Die Polygone werden der Reihe nach rasterisiert
- Raytracing
  - steht für Strahlverfolgung
  - Das Bild wird von den Sehstrahlen abgetastet
  - Strahl wird in den 3-Dimensionalen Raum geworfen
  - Pixel bekommt die Farbe, von dem Objekt, das zuerst getroffen wird
  - jedes Objekt wird geschnitten, nur das nahste Objekt dargestellt
  - Einfaches konzept, aufwendig umzuetzen, da jedes pixel parralel zu allen anderen bestimmt werden muss
  - Grundalgorithmus ist aber Gleich: Schnittpunkt bestimmen => Objekt darstellen

#### Schnittpunktbestimmung - Kugel

- Kugel in implizierter Beschreibung: (*x*-*c*)^2 - r^2 = 0  //Skalare Gleichung
- Strahl in Punkt-Richtungsgleichung: *r*(t) = *o* + t**d*  //Skalare Gleichung
- zu lösen: (*o*+ t *d* - *c*)^2 - r^2 = 0
  - alles außer t ist bekannt, quadratische Gleichung einfach zu lösen
  - für bestimmte t ist die Gleichung erfüllt => Strahl trifft Kugel

#### Schnitttest Strahl Dreieck

- Objekte sehr oft Dreiecksnetze
- Strahl: *r*(t) = *o* + t**d*  //Skalare Gleichung
- Dreieck: 3 Punkte *a*,*b*,*c*
  - baryzentrische Koordinaten => baryzentrische Linearkombination: p = u ∙ *a* + v ∙ *b* + w ∙ *c*, mit u + v + w = 1
  - man ist innerhalb des dreiecks, wenn für u,v,w gilt: 0 <= u,v,w <= 1
- zu lösen: [*o* + t *d* = u ∙ *a* + v ∙ *b* + w ∙ *c*, u + v + w = 1]
  - lineares Gleichunggsystem mit 4 unbekannten
  - i.a. eindeutige Lösung

## Vorlesung 2 / Einführung + Modellierung

### Raytracing

- Schatten: heller/Dunkler des strahls
- Reflexionen: "Strahlverfolgung" mit sekundärem Strahl (einfallswinkel = ausfallswinkel)
- Beugung/Brechung: "Strahlverfolgung" mit sekundärem Strahl. nach brechungsgesetzen
- Sichtbarkeit: Vergleichen von Beträgen der "Strahlen" erstes objekt ist sichtbar
- Schattierung: Kugel, die beleuchtet wird, ist nicht überall gleich hell -> Beleuchtungsberechnung

### Geometrische Modellierung

#### Modelle

##### Drahtmodell

- beschreiben von Objekten durch ihre Konturen
- zwischen den Elementen der Beschreibung einer Konturen bestehen keine Beziehungen + Mehrdeutigkeit

=> Flächenmoddell

##### Flächenmodell

- polygonale Flächenrepräsentation
- parametrische Flächenrepräsentation

##### Körpermodelle

- beschreiben die Objekte als komplette, körperhafte struktur
- für jeden punkt kann entschieden werden, ob er sich innerhalb oder außerhalb des Körpers befindet
- Für Körpermodelle lassen sich Verdeckungs- und Schnittberechnng durchführen

#### Körpermodellierung

##### Boundary Repräsentation (B-Rep)

- Graphenstruktur, die begrenzte Oberflächenteile eines Körpers und ihre Lage zueinander topologisch Beschreibt
- Beseteht aus mehreren Objekte:
  - Schale (aus Facetten zusammengesetzte, zusammenhängende geschlossene Oberfläche)
  - Fläche (von einer oder mehr Konturen begrenzt)
  - Kontur (abgeschlossene, orientierte Folge vo Kanten)
  - Kante (Geraden oder Kurzven, durch 2 Punkte begrenze)
  - Knoten (Punkte)

##### CSG

- Graph in Baumstruktur
- nicht eindeutig

##### Octrees

- das gesamte gebiet ist schwarz/weiß => Bild ferig
- Fall 3: Teile sind schwarz, andere sind weiß
- "Wurzel" -> rekursive Unterteilung -> "weiß, schwarz, oder grau" -> rekursive Unterteilung
- Teile und herrsche prinzip

## Kurven und Flächenmodellierung

### Beispiele

- Autokarosserienetwurf, Flugzeug- und Schiffbau, Simulation von Bewegungsabläufe, Animation, Filmindustrie, Computerkunst

### Freiformkurven / Freiformflächen

- parametrische Kurven/Flächen mit intuitiven Kontrollpunkten
- algebraische Definition:
  - Durch Definition non n-1 Basisfunktion $f_i (t), i = 0, ..., n$
  - dann wird durch die Kontrollpunkte $p_i$ die Kurve definiert als
  - $x(t) = ∑_{i=0}^{n} p_i f_i (t)$
- geometrische Definition:
  - Beschreibung einer geometrischen Konstruktion, wie aus den Kontrollpunkten $p_i$ und dem Parameterwert t der entsprechende Kurvenpunkt konstruiert werden kann

### Der de-Casteljau-Algorithmus

- Geometrisches Verfahren zur Konstruktion von Freiformkurven
- Basiert auf wiederholter linearer Interpolation
