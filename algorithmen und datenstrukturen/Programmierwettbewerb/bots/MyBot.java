package bots;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.simulation.Tower;
import com.gatdsen.simulation.*;

/**
 * In dieser Klasse implementiert ihr euren Bot.
 */
public class MyBot extends Bot {
    /**
     * Hier müsst ihr euren vollständigen Namen angeben
     * 
     * @return Euer vollständiger Name im Format: "Vorname(n) Nachname"
     */
    @Override
    public String getStudentName() {
        return "Tobias Baake";
    }

    /**
     * Hier müsst ihr eure Matrikelnummer angeben
     * 
     * @return Eure Matrikelnummer
     */
    @Override
    public int getMatrikel() {
        return 247074;
    }

    /**
     * Hier könnt ihr eurem Bot einen (kreativen) Namen geben
     * 
     * @return Der Name eures Bots
     */
    @Override
    public String getName() {
        return "Mäidscha Monogwäm der Geile Oida";
    }

    /**
     * Wird vor Beginn des Spiels aufgerufen. Die erlaubte Berechnungszeit für diese
     * Methode beträgt 1 Sekunde.
     * Diese Methode kann daher verwendet werden, um Variablen zu initialisieren und
     * einmalig, sehr rechenaufwändige
     * Operationen durchzuführen.
     * 
     * @param state Der {@link StaticGameState Spielzustand} zu Beginn des Spiels
     */
    @Override
    public void init(StaticGameState state) {
        System.out.println("Der Bot \"" + getName() + "\" wurde initialisiert!");
    }

    private boolean last = false;

    /**
     * Wird aufgerufen, wenn der Spieler seinen Zug für die aktuelle Runde
     * durchführen soll. Die erlaubte
     * Berechnungszeit für diese Methode beträgt 0,5 Sekunden bzw. 500
     * Millisekunden.
     * <p>
     * Der {@link StaticGameState Spielzustand} reflektiert dabei den Zustand des
     * Spiels vor dem Zug des Spielers. Der
     * Zustand ist statisch, das heißt bei Aufrufen des {@link Controller
     * Controllers} werden diese Änderungen nicht im
     * Spielzustand in dieser Runde reflektiert, sondern erst in der nächsten Runde,
     * wenn man den neuen Spielzustand erhält.
     * <p>
     * Der Controller ermöglicht dir die Steuerung, um Aktionen, wie bspw. das
     * Platzieren von Türmen, auszuführen. Die
     * übergebene Controller-Instanz deaktiviert sich nach Ende des Zuges permanent.
     * 
     * @param state      Der {@link StaticGameState Spielzustand} vor der Ausführung
     *                   des aktuellen Zuges
     * @param controller Der {@link Controller Controller}, um Aktionen auszuführen
     */
    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        System.out.println("Der Bot \"" + getName() + "\" ist am Zug in Runde " + state.getTurn() + "!");

        // Spielfeldgröße erhalten
        int width = state.getBoardSizeX();
        int height = state.getBoardSizeY();

        // Wenn die Runde 10 erreicht ist und der "last"-Schalter noch nicht aktiviert wurde
        if (state.getTurn() >= 10 && !this.last) {
            // Hole die Position der Käsetile
            PathTile cheese = state.getMyPlayerState().getCheeseTile();
            IntVector2 pos = cheese.getPosition();
            // Versuche, Türme um den Käsetile herum zu platzieren
            for (int i = -1; i < 1; i++) {
                for (int j = -1; j < 1; j++) {
                    // platziere den Tutm, wenn die Fläche bebaubar ist.
                    if (isValidPosition(pos.x + i, pos.y + j, width, height)) {
                        controller.placeTower(pos.x + i, pos.y + j, Tower.TowerType.CATANA_CAT);
                        // Überprüfe, ob ein Turm platziert wurde
                        if (state.getMyPlayerState().getBoard()[pos.x + i][pos.y + j] instanceof TowerTile) {
                            this.last = true;
                            break;
                        }
                    }
                }
            }
        }

        // Spielfeld erhalten
        Tile[][] map = state.getMyPlayerState().getBoard();
        int paths;

        // Array zur Verfolgung der platzierten Türme
        int[][] towers = new int[width][height];

        // Über das gesamte Spielfeld iterieren
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Überprüfen, ob die Kachel bebaut werden kann
                if (map[i][j].isBuildable()) {

                    // Anzahl der Pfade in verschiedenen Radien um die aktuelle Position erhalten
                    int paths3 = getSourroundingPaths(map, i, j, width, height, 1);
                    int paths5 = getSourroundingPaths(map, i, j, width, height, 2);
                    int paths9 = getSourroundingPaths(map, i, j, width, height, 4);

                    // Anzahl der Türme in verschiedenen Radien um die aktuelle Position erhalten
                    int towers5 = getSorroundingTowers(towers, i, j, width, height, 2, 2);
                    int towers9 = getSorroundingTowers(towers, i, j, width, height, 4, 3);

                    // Platzieren und Aufrüsten von Türmen basierend auf der Anzahl der Pfade und Türme
                    if (paths3 >= 5) {
                        controller.placeTower(i, j, Tower.TowerType.CATANA_CAT);
                        controller.upgradeTower(i, j);
                        towers[i][j] = 1;
                    } else if (paths5 >= 10 && towers5 < 2) {
                        controller.placeTower(i, j, Tower.TowerType.MINIGUN_CAT);
                        controller.upgradeTower(i, j);
                        towers[i][j] = 2;
                    } else if (paths9 >= 15 && towers9 < 10) {
                        controller.placeTower(i, j, Tower.TowerType.MAGE_CAT);
                        controller.upgradeTower(i, j);
                        towers[i][j] = 3;
                    }
                }
            }
        }

        // Senden von Gegnern zum Spieler
        for (int i = 0; i < 3; i++) {
            controller.sendEnemyToPlayer(Enemy.EnemyType.EMP_ENEMY);
        }
    }

    /**
     * Zählt die Anzahl der Pfade in einem bestimmten Radius um die Position (x, y).
     * 
     * @param map    Das Spielfeld
     * @param x      Die x-Koordinate
     * @param y      Die y-Koordinate
     * @param width  Die Breite des Spielfeldes
     * @param height Die Höhe des Spielfeldes
     * @param radius Der Radius um die Position (x, y)
     * @return Die Anzahl der Pfade in einem bestimmten Radius um die Position (x, y)
     */
    private int getSourroundingPaths(Tile[][] map, int x, int y, int width, int height, int radius) {
        int paths = 0;

        // Überprüfen aller Positionen im angegebenen Radius
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                if (isValidPosition(i, j, width, height) && map[i][j].isPath()) {
                    paths++;
                }
            }
        }

        return paths;
    }

    /**
     * Zählt die Anzahl der Türme des Typs {@code towerType} in einem bestimmten
     * Radius um die Position (x, y).
     * 
     * @param towers    Die Türme auf dem Spielfeld
     * @param x         Die x-Koordinate
     * @param y         Die y-Koordinate
     * @param width     Die Breite des Spielfeldes
     * @param height    Die Höhe des Spielfeldes
     * @param radius    Der Radius um die Position (x, y)
     * @param towerType Der Typ des Turms
     * @return Die Anzahl der Türme des Typs {@code towerType} in einem bestimmten Radius um die Position (x, y)
     */
    private int getSorroundingTowers(int[][] towers, int x, int y, int width, int height, int radius, int towerType) {
        int towerCount = 0;

        // Überprüfen aller Positionen im angegebenen Radius
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                if (isValidPosition(i, j, width, height) && towers[i][j] == towerType) {
                    towerCount++;
                }
            }
        }

        return towerCount;
    }

    /**
     * Überprüft, ob die Position (x, y) innerhalb des Spielfeldes liegt.
     * 
     * @param x      Die x-Koordinate
     * @param y      Die y-Koordinate
     * @param width  Die Breite des Spielfeldes
     * @param height Die Höhe des Spielfeldes
     * @return true, wenn die Position innerhalb des Spielfeldes liegt, ansonsten false
     */
    private boolean isValidPosition(int x, int y, int width, int height) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
}
