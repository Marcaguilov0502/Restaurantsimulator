import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static java.lang.Thread.sleep;

public abstract class Character implements Runnable {


    //Constants


    public final int FRONT = 0, BACK = 1, LEFT = 2, RIGHT = 3;
    public final int BASE_SPEED = 250;

    //Attributes


    protected int speed;
    protected int currentSpriteVariant;
    protected int x,y;
    protected int targetX, targetY;
    protected Queue<int[]> path = new LinkedList<>();
    protected float xOffset, yOffset;
    protected int[] floor = new int[2];
    protected Table table;


    //Constructor


    public Character(int x, int y, Table table) {
        Random r = new Random();
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        xOffset = 0;
        yOffset = 0;
        this.currentSpriteVariant = CharacterSprite.FRONT_N;
        speed = BASE_SPEED + r.nextInt(BASE_SPEED);
        Window.restaurant.addToMap(x,y,Window.restaurant.CHARACTER);
        this.table = table;
    }


    //Methods


    protected void findPath() {
        int[][] restaurantMap = Window.restaurant.getMap();
        PathFinder pf = new PathFinder(turnIntoPathMap(restaurantMap));
        pf.setOrigin(this.x, this.y);
        pf.setTarget(targetX,targetY);
        path = pf.findPath();
    }

    public int getCurrentSpriteVariant() {
        return currentSpriteVariant;
    }

    public float getXWithOffset() {
        return ((float)x + xOffset);
    }

    public float getYWithOffset() {
        return ((float)y + yOffset);
    }

    protected void lookAtTable() {
        int[][] map = Window.restaurant.getMap();
        int TABLE = Window.restaurant.TABLE;
        if (map[x+1][y] == TABLE) {
            currentSpriteVariant = CharacterSprite.RIGHT_N;
        } else if (map[x-1][y] == TABLE) {
            currentSpriteVariant = CharacterSprite.LEFT_N;
        } else if (map[x][y+1] == TABLE) {
            currentSpriteVariant = CharacterSprite.FRONT_N;
        } else if (map[x][y-1] == TABLE ) {
            currentSpriteVariant = CharacterSprite.BACK_N;
        }
    }

    protected abstract void lookForTarget();

    public int nextFloor() {
        int f0 = floor[0];
        int f1 = floor[1];
        floor = new int[]{f1, 0};
        return f0;
    }

    protected boolean isNextToTable() {
        int[][] map = Window.restaurant.getMap();
        int TABLE = Window.restaurant.TABLE;
        return (map[x+1][y] == TABLE || map[x-1][y] == TABLE || map[x][y+1] == TABLE || map[x][y-1] == TABLE );
    }

    protected boolean isOnTarget() {
        return (targetX == x && targetY == y);
    }

    protected int[][] turnIntoPathMap(int[][] map) {

        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int STAIRS = Window.restaurant.STAIRS;

        int[][] pathFindingMap = new int[map.length][map[0].length];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == VOID || map[x][y] == TARGETED_POSITION || map[x][y] == STAIRS) {
                    pathFindingMap[x][y] = PathFinder.VOID;
                } else {
                    pathFindingMap[x][y] = PathFinder.OBSTACLE;
                }
            }
        }
        return pathFindingMap;
    }

    protected void target(int[] position) {
        Window.restaurant.addToMap(targetX,targetY,Window.restaurant.VOID);
        targetX = position[0];
        targetY = position[1];
        if (Window.restaurant.getMap()[targetX][targetY] == Window.restaurant.VOID) {
            floor[0] = 0;
            floor[1] = 0;
            Window.restaurant.addToMap(targetX, targetY, Window.restaurant.TARGETED_POSITION);
            findPath();
        } else {
            lookForTarget();
        }
    }

    protected boolean targetIsNextToTable() {
        int[][] map = Window.restaurant.getMap();
        int TABLE = Window.restaurant.TABLE;
        return (map[targetX+1][targetY] == TABLE || map[targetX-1][targetY] == TABLE || map[targetX][targetY+1] == TABLE || map[targetX][targetY-1] == TABLE );
    }

    protected void targetKitchen() {
        Random r = new Random();
        int[][] positions = Window.restaurant.kitchenVoids();
        int[] position = positions[r.nextInt(positions.length)];
        target(position);
    }

    protected void targetTable() {
        Random r = new Random();
        int[][] positions = Window.restaurant.tableAccessVoids(table);
        int[] position = positions[r.nextInt(positions.length)];
        target(position);
    }

    protected abstract void targetRandom();


    //Actions


    @Override
    public void run() {

    }

    protected void followPath() throws InterruptedException {

        if (path.size() == 0) {
            Window.restaurant.addToMap(targetX, targetY, Window.restaurant.VOID);
            targetX = x;
            targetY = y;
            return;
        }

        int[] nextStep = path.poll();
        int distanceX = nextStep[0] - x;
        int distanceY = nextStep[1] - y;
        if (distanceX == 1 && distanceY == 0) {
            moveRight();
        } else if (distanceX == -1 && distanceY == 0) {
            moveLeft();
        } else if (distanceY == 1 && distanceX == 0) {
            moveDown();
        } else if (distanceY == -1 && distanceX == 0) {
            moveUp();
        } else {
            findPath();
        }
    }

    protected void moveDown() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        if (map[x][y+1] != VOID && map[x][y+1] != TARGETED_POSITION && map[x][y+1] != STAIRS) {
            findPath();
            return;
        }
        currentSpriteVariant = CharacterSprite.FRONT_N;
        float increment = 1f/((float) speed);
        floor[1] = map[x][y+1];
        Window.restaurant.addToMap(x,y+1,CHARACTER);
        for (int i = 0; i < speed; i+=1) {
            yOffset += increment;
            sleep(1);
            if (i > (((float) speed)*3f)/4f) { currentSpriteVariant = CharacterSprite.FRONT_R; }
            else if (i > (((float) speed)*2f)/4f) {currentSpriteVariant = CharacterSprite.FRONT_N;}
            else if (i > ((float) speed)/4f) {currentSpriteVariant = CharacterSprite.FRONT_L;}
        }
        currentSpriteVariant = CharacterSprite.FRONT_N;
        Window.restaurant.addToMap(x,y,nextFloor());
        y++;
        yOffset = 0;
    }

    protected void moveDownstairs() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int CHARACTER = Window.restaurant.CHARACTER;
        currentSpriteVariant = CharacterSprite.LEFT_N;
        float diagonalSpeed = (float) ((float) speed *Math.sqrt(2));
        float totalSpeed = diagonalSpeed + speed;
        float increment1 = 1f/speed;
        float increment2 = 1f/diagonalSpeed;
        floor[1] = map[x-2][y+1];
        Window.restaurant.addToMap(x-2,y+1,CHARACTER);
        for (int i = 0; i < (int)totalSpeed; i+=1) {
            if (i > diagonalSpeed/2f && i < diagonalSpeed*3f/2f) {
                yOffset += increment2;
                xOffset -= increment2;
            } else {
                xOffset -= increment1;
            }
            sleep(1);
            if (i > (totalSpeed*7f)/8f) {currentSpriteVariant = CharacterSprite.LEFT_R;}
            else if (i > totalSpeed*6f/8f) {currentSpriteVariant = CharacterSprite.LEFT_N;}
            else if (i > totalSpeed*5f/8f) {currentSpriteVariant = CharacterSprite.LEFT_L;}
            else if (i > totalSpeed*4f/8f) {currentSpriteVariant = CharacterSprite.LEFT_N;}
            else if (i > totalSpeed*3f/8f) { currentSpriteVariant = CharacterSprite.LEFT_R; }
            else if (i > totalSpeed*2f/8f) {currentSpriteVariant = CharacterSprite.LEFT_N;}
            else if (i > totalSpeed/8f) {currentSpriteVariant = CharacterSprite.LEFT_L;}
        }
        currentSpriteVariant = CharacterSprite.LEFT_N;
        Window.restaurant.addToMap(x,y,nextFloor());
        x--;
        x--;
        y++;
        yOffset = 0;
        xOffset = 0;
        findPath();
    }

    protected void moveLeft() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        if (map[x-1][y] != VOID && map[x-1][y] != TARGETED_POSITION && map[x-1][y] != STAIRS) {
            findPath();
            return;
        }
        if (map[x-1][y] == STAIRS) {
            if (map[x-2][y+1] != VOID && map[x-2][y+1] != TARGETED_POSITION) {
                findPath();
                return;
            }
            moveDownstairs();
            return;
        }
        currentSpriteVariant = CharacterSprite.LEFT_N;
        float increment = 1f/((float) speed);
        floor[1] = map[x-1][y];
        Window.restaurant.addToMap(x-1,y,CHARACTER);
        for (int i = 0; i < speed; i+=1) {
            xOffset -= increment;
            sleep(1);
            if (i > (((float) speed)*3f)/4f) { currentSpriteVariant = CharacterSprite.LEFT_R; }
            else if (i > (((float) speed)*2f)/4f) {currentSpriteVariant = CharacterSprite.LEFT_N;}
            else if (i > ((float) speed)/4f) {currentSpriteVariant = CharacterSprite.LEFT_L;}
        }
        currentSpriteVariant = CharacterSprite.LEFT_N;
        Window.restaurant.addToMap(x,y,nextFloor());
        x--;
        xOffset = 0;
    }

    protected void moveRight() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        if (map[x+1][y] != VOID && map[x+1][y] != TARGETED_POSITION && map[x+1][y-1] != STAIRS) {
            findPath();
            return;
        }
        if (map[x+1][y-1] == STAIRS) {
            if (map[x+2][y-1] != VOID && map[x+2][y-1] != TARGETED_POSITION) {
                findPath();
                return;
            }
            moveUpstairs();
            return;
        }
        currentSpriteVariant = CharacterSprite.RIGHT_N;
        float increment = 1f/((float) speed);
        floor[1] = map[x+1][y];
        Window.restaurant.addToMap(x+1,y,CHARACTER);
        for (int i = 0; i < speed; i+=1) {
            xOffset += increment;
            sleep(1);
            if (i > (((float) speed)*3f)/4f) { currentSpriteVariant = CharacterSprite.RIGHT_R; }
            else if (i > (((float) speed)*2f)/4f) {currentSpriteVariant = CharacterSprite.RIGHT_N;}
            else if (i > ((float) speed)/4f) {currentSpriteVariant = CharacterSprite.RIGHT_L;}
        }
        currentSpriteVariant = CharacterSprite.RIGHT_N;
        Window.restaurant.addToMap(x,y,nextFloor());
        x++;
        xOffset = 0;
    }

    public void moveToTargetX() throws InterruptedException {
        if(targetX - x > 0) {
            moveRight();
        } else if (targetX - x < 0) {
            moveLeft();
        }
    }

    public void moveToTargetY() throws InterruptedException {
        if(targetY - y > 0) {
            moveDown();
        } else if (targetY - y < 0) {
            moveUp();
        }
    }

    protected void moveUp() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        if (map[x][y-1] != VOID && map[x][y-1] != TARGETED_POSITION && map[x][y-1] != STAIRS) {
            findPath();
            return;
        }
        currentSpriteVariant = CharacterSprite.BACK_N;
        float increment = 1f/((float) speed);
        floor[1] = map[x][y-1];
        Window.restaurant.addToMap(x,y-1,CHARACTER);
        for (int i = 0; i < speed; i+=1) {
            yOffset -= increment;
            sleep(1);
            if (i > (((float) speed)*3f)/4f) { currentSpriteVariant = CharacterSprite.BACK_R; }
            else if (i > (((float) speed)*2f)/4f) {currentSpriteVariant = CharacterSprite.BACK_N;}
            else if (i > ((float) speed)/4f) {currentSpriteVariant = CharacterSprite.BACK_L;}
        }
        currentSpriteVariant = CharacterSprite.BACK_N;
        Window.restaurant.addToMap(x,y,nextFloor());
        y--;
        yOffset = 0;
    }

    protected void moveUpstairs() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int CHARACTER = Window.restaurant.CHARACTER;
        currentSpriteVariant = CharacterSprite.RIGHT_N;
        float diagonalSpeed = (float) ((float) speed *Math.sqrt(2));
        float totalSpeed = diagonalSpeed + speed;
        float increment1 = 1f/speed;
        float increment2 = 1f/diagonalSpeed;
        floor[1] = map[x+2][y-1];
        Window.restaurant.addToMap(x+2,y-1,CHARACTER);
        for (int i = 0; i < (int)totalSpeed; i+=1) {
            if (i > diagonalSpeed/2f && i < diagonalSpeed*3f/2f) {
                yOffset -= increment2;
                xOffset += increment2;
            } else {
                xOffset += increment1;
            }
            sleep(1);

            if (i > totalSpeed*7f/8f) {currentSpriteVariant = CharacterSprite.RIGHT_R;}
            else if (i > totalSpeed*6f/8f) {currentSpriteVariant = CharacterSprite.RIGHT_N;}
            else if (i > totalSpeed*5f/8f) {currentSpriteVariant = CharacterSprite.RIGHT_L;}
            else if (i > totalSpeed*4/8f) {currentSpriteVariant = CharacterSprite.RIGHT_N;}
            else if (i > totalSpeed*3f/8f) { currentSpriteVariant = CharacterSprite.RIGHT_R; }
            else if (i > totalSpeed*2f/8f) {currentSpriteVariant = CharacterSprite.RIGHT_N;}
            else if (i > totalSpeed/8f) {currentSpriteVariant = CharacterSprite.RIGHT_L;}
        }
        currentSpriteVariant = CharacterSprite.RIGHT_N;
        Window.restaurant.addToMap(x,y,nextFloor());
        x++;
        x++;
        y--;
        yOffset = 0;
        xOffset = 0;
        findPath();
    }

    protected void testSprites() throws InterruptedException {

        for (int i = 0; i < 200; i++) {
            currentSpriteVariant++;
            if (currentSpriteVariant > 17) {
                currentSpriteVariant = 0;
            }
            sleep(200);
        }
    }

    protected void turnAround() throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            if (currentSpriteVariant < 3) {
                currentSpriteVariant += 9;
            } else if (currentSpriteVariant < 6 ) {
                currentSpriteVariant += 3;
            } else if (currentSpriteVariant < 9 ) {
                currentSpriteVariant -= 6;
            } else if (currentSpriteVariant < 12 ) {
                currentSpriteVariant -= 6;
            }
            sleep(speed);
        }
    }

}
