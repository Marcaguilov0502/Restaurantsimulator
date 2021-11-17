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
    private boolean paused = false;


    //Constructor


    public Restaurant() {
        generateBorders();
    }


    //Methods


    public synchronized void addCharacter(Character character) {
        characters.add(character);
        new Thread(character).start();
    }

    public synchronized void addChef() {
        if (map[3][5] == VOID) {
            addCharacter(new Chef(tables.get(0), 3, 5));
        }
    }

    public synchronized void addClient() {
        if (map[24][6] == VOID) {
            addCharacter(new Client(tables.get(0), 24, 6));
        }
    }

    public void addDecoration(Decoration decoration) {
        decorations.add(decoration);
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public synchronized void addToMap(int x, int y, int elementType) {
        if (x == 0 && y == 0 && (elementType == VOID || elementType == TARGETED_POSITION)) {
            return;
        }
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

    public synchronized ArrayList<Character> getCharacters() {
        return characters;
    }

    public ArrayList<Decoration> getDecorations() {
        return decorations;
    }

    public int[][] getMap() {
        return map;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public synchronized int[][] kitchenVoids() {
        if (kitchenVoidCount() == 0) {
            return null;
        }

        int[][] kitchenPositions = new int[kitchenVoidCount()][2];
        int i = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == VOID) {
                    kitchenPositions[i][0] = x;
                    kitchenPositions[i][1] = y;
                    i++;
                }
            }
        }
        return kitchenPositions;
    }

    public synchronized int kitchenVoidCount() {
        int count = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == VOID) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean paused() {return paused;}

    public void pause() {
        paused = !paused;
    }

    public void removeChef() {
        for (Character c : characters) {
            if (c instanceof Chef && !c.exit) {
                c.exit();
                return;
            }
        }
    }

    public void removeClient() {
        for (Character c : characters) {
            if (c instanceof Client && !c.exit) {
                c.exit();
                return;
            }
        }
    }

    public synchronized int[][] tableAccessVoids(Table table) {

        int tableAccessPositionsCount = tableAccessVoidsCount(table);

        if (tableAccessPositionsCount == 0) {
            return null;
        }

        int[][] tableAccessPositions = new int[tableAccessPositionsCount][2];

        int tableXStart = table.getX();
        int tableXEnd = tableXStart + table.width();
        int tableYStart = table.getY();
        int tableYEnd = tableYStart + table.height();
        int i = 0;

        for (int x = tableXStart; x < tableXEnd && i < tableAccessPositionsCount; x++) {
            if (map[x][tableYStart-1] == VOID) {
                tableAccessPositions[i] = new int[]{x,tableYStart-1};
                i++;
            } else if (map[x][tableYEnd] == VOID ) {
                tableAccessPositions[i] = new int[]{x,tableYEnd};
                i++;
            }
        }

        for (int y = tableYStart; y < tableYEnd && i < tableAccessPositionsCount; y++) {
            if (map[tableXStart-1][y] == VOID ) {
                tableAccessPositions[i] = new int[]{tableXStart-1,y};
                i++;
            } else if (map[tableXEnd][y] == VOID ) {
                tableAccessPositions[i] = new int[]{tableXEnd,y};
                i++;
            }
        }

        return tableAccessPositions;
    }

    public synchronized int tableAccessVoidsCount(Table table) {
        int tableXStart = table.getX();
        int tableXEnd = tableXStart + table.width();
        int tableYStart = table.getY();
        int tableYEnd = tableYStart + table.height();
        int count = 0;

        for (int x = tableXStart; x < tableXEnd; x++) {
            if (map[x][tableYStart-1] == VOID ||map[x][tableYEnd+1] == VOID ) {
                count++;
            }
        }

        for (int y = tableYStart; y < tableYEnd; y++) {
            if (map[tableXStart-1][y] == VOID ||map[tableXEnd+1][y] == VOID ) {
                count++;
            }
        }

        return count;
    }

    public synchronized int[][] voidPositions() {
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

    public synchronized int voidCount() {
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

}
