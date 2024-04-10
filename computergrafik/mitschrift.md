# Vorlesung 1

- [E-Learning](https://elearning.ovgu.de/enrol/index.php?id=16588)
- Einschreibeschlüssel: **raytracer**
- [vc.cs.ovgu.de](https://vc.cs.ovgu.de/index.php?article_id=792&clang=1)
- 2/3 aller Aufgaben (2/3 der Übungen, 2/3 der Programmieraufgaben) => Ziel eigener Raytracer
- Programmiersprache C++

## Übungen

- Programmieraufgaben alle 2-3 Wochen
- Übung jede Woche
- **Übung 1 für Grundlagen sehr wichtig!!!**

## Einführung

### was ist computergrafik

- grafiche Datenverarbeiteung
- "Methoden und Techniken um Daten zu und Von grafischen Displays über Computer"
- Bildsynthese(Erstellen) und Bildanalyse(Erkennen)

#### Richtungen Computergraphik

- Modellierung/Akquisition - Generirung der Bildbeschreibung in maschinell-interpretierbarer Sprache
- Bearbeitung (Processing) - Hierarchien, Segmentierung, Komprimierung, Umwandlungen
- Rendering (Darstellung) - Erzeugung des Bildes aus der Bildbeschreibung
- Interaktion - Manipulation des Bildes

### Geschichte

- 1950er: vor allem Radare
- 1960er: erste 3D Modelle, Interaktion und erstes Display (IBM), erstes Bildspeicher Display
- 1970er: erste CAD/CAM Systeme, Raster-Scan-Prinzip => kommerzielle Displays, erste Simulatorenund graphische Programmiersprachen, entstehen der SIGGRAPH ~ 1000 Teilnehmer, **erste Verfahren zur Schattierten Objektdarstellung**
- Ende 1970er: Reflexion und Transparenz (Raytracing)
- 1980er: fotorealistische Computergrafiken + Filmeffekte mit Computergrafik, Animations-Software, Beleuchtungs-Simulation, Beginn von VR/AR
- 1990er: Kommunikationstechniken, Daten und Informations-Visualisierung, Graphik im Internet, Multimedia, Non-Photorealistic Rendering
- Ende 1990er: HDR, Entstehung von GPUs => Co-Prozessor für Grafikanwendungen

### Anwendung

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
  - baryzentrische Koordinaten => baryzentrische Linearkombination: p = u * *a* + v * *b* + w * *c*, mit u + v + w = 1
  - man ist innerhalb des dreiecks, wenn für u,v,w gilt: 0 <= u,v,w <= 1
- zu lösen: [*o* + t *d* = u* *a* + v * *b* + w * *c*, u + v + w = 1]
  - lineares Gleichunggsystem mit 4 unbekannten
  - i.a. eindeutige Lösung
