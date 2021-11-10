import java.util.ArrayList;

public class Restaurant {


    //Constants


    public final int
            BORDER = -1,    VOID = 0,       CHARACTER = 1,
            TABLE = 2,      DECORATION = 3, TARGETED_POSITION = 4,
            STAIRS = 5;


    //Attributes


    private int[][] map = new int[27][18];
    private ArrayList<Character> characters = new ArrayList();
    private ArrayList<Table> tables = new ArrayList();
    private ArrayList<Decoration> decorations = new ArrayList();


    //Constructor


    public Restaurant() {
        generateBorders();
    }


    //Methods


    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void addDecoration(Decoration decoration) {
        decorations.add(decoration);
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public synchronized void addToMap(int x, int y, int elementType) {
        map[x][y] = elementType;
    }

    private void generateBorders() {

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (x == 0 || x == map.length-1) { map[x][y] = BORDER; }
                if (y < 4 || y == map[0].length-1) { map[x][y] = BORDER;}
                if (y == 4 && (x < 10 || x > 22)) { map[x][y] = BORDER;}
                if (y == 5 && ((x < 9 && x > 6) || x > 22)) { map[x][y] = BORDER;}
                //if (y == map[0].length-2 && x > 6 && x < 10) { map[x][y] = BORDER;}
                if (x == 9 && y > 4 && y < 16) { map[x][y] = STAIRS;}
            }
        }
        map[9][16] = BORDER;
    }

    public int[][] getMap() {
        return map;
    }

    public int[][] voidPositions() {
        if (voidCount() == 0) {
            return null;
        }
        int[][] voidPositions = new int[voidCount()][2];
        int i = 0;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == VOID) {
                    voidPositions[i][0] = x;
                    voidPositions[i][1] = y;
                    i++;
                }
            }
        }
        return voidPositions;
    }

    public int voidCount() {
        int count = 0;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == VOID) {
                    count++;
                }
            }
        }
        return count;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public ArrayList<Decoration> getDecorations() {
        return decorations;
    }
}
