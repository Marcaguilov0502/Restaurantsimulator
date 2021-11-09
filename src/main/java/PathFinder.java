import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class PathFinder {


    //Constants


    public static final int
            OUT = -3,
            ORIGIN = -2, TARGET = -1, VOID = 0,
            SEARCHED = 1, EXPANDED = 2, OBSTACLE = 3;


    //Attributes


    private Stack<int[]> foundPath = new Stack<>();
    private int[] origin, target;
    private int[][] map;
    private int[][][] pathMap;


    //Constructor


    public PathFinder(int[][] map) {
        this.map = cloneMap(map);
        this.pathMap = new int[map.length][map[0].length][2];
    }


    //Methods


    public int[][] cloneMap(int[][] map) {
        int[][] clonedMap = new int[map.length][map[0].length];
        for (int x = 0; x < map.length; x++) {
            System.arraycopy(map[x], 0, clonedMap[x], 0, map[0].length);
        }
        return clonedMap;
    }

    public void createPath(int x, int y) {
        if (x == origin[0] && y == origin[1]) {
            return;
        } else {
            foundPath.push(new int[]{x,y});
            createPath(pathMap[x][y][0], pathMap[x][y][1]);
        }
    }

    public boolean expand() {
        boolean somethingExpanded = false;
        for (int x = 0; x < pathMap.length; x++) {
            for (int y = 0; y < pathMap[0].length; y++) {
                if (map[x][y] == SEARCHED) {
                    map[x][y] = EXPANDED;
                    somethingExpanded = true;
                }
            }
        }
        return somethingExpanded;
    }

    public Queue<int[]> findPath() {
        while (!search()) {
            if (!expand()) {
                Queue<int[]> q = new LinkedList<int[]>();
                return q;
            }
        }
        createPath(target[0], target[1]);
        return getQueue();
    }

    public Queue<int[]> getQueue() {
        Queue<int[]> path = new LinkedList<int[]>();
        while (foundPath.size() > 0) {
            path.add(foundPath.pop());
        }
        return path;
    }

    public boolean lookAtCell(int xCaller, int yCaller, int x, int y) {
        if (x > 0 && x < map.length-1 && y > 0 && y < map[0].length-1 ) {

            switch (map[x][y]) {
                case VOID:
                    map[x][y] = SEARCHED;
                    pathMap[x][y][0] = xCaller;
                    pathMap[x][y][1] = yCaller;
                    return false;
                case TARGET:
                    pathMap[x][y][0] = xCaller;
                    pathMap[x][y][1] = yCaller;
                    return true;
                default:
                    return false;
            }

        } else {
            return false;
        }
    }

    public boolean search() {
        for (int x = 0; x < pathMap.length; x++) {
            for (int y = 0; y < pathMap[0].length; y++) {
                if (map[x][y] == EXPANDED || map[x][y] == ORIGIN) {

                    if (lookAtCell(x,y,x-1,y)) { return true; }
                    if (lookAtCell(x,y,x+1,y)) { return true; }
                    if (lookAtCell(x,y,x,y-1)) { return true; }
                    if (lookAtCell(x,y,x,y+1)) { return true; }

                }
            }
        }
        return false;
    }

    public void setOrigin(int x, int y) {
        this.origin = new int[]{x,y};
        map[x][y] = ORIGIN;
    }

    public void setTarget(int x, int y) {
        this.target = new int[]{x,y};
        map[x][y] = TARGET;
    }
}
