import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static java.lang.Thread.sleep;

public abstract class Character implements Runnable {


    //Constants


    public final int FRONT = 0, BACK = 1, LEFT = 2, RIGHT = 3;
    public final int BASE_SPEED = 300;

    //Attributes


    protected int speed;
    protected int currentSpriteVariant;
    protected int x,y;
    protected int targetX, targetY;
    protected Queue<int[]> path = new LinkedList<int[]>();
    protected float xOffset, yOffset;
    protected int facing;
    private int[] floor = new int[2];


    //Constructor


    public Character(int x, int y) {
        Random r = new Random();
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        xOffset = 0;
        yOffset = 0;
        this.currentSpriteVariant = CharacterSprite.FRONT_N;
        this.facing = FRONT;
        speed = BASE_SPEED + r.nextInt(BASE_SPEED);
        Window.restaurant.addToMap(x,y,Window.restaurant.CHARACTER);
    }


    //Methods


    public void findPath() {
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        int[][] restaurantMap = Window.restaurant.getMap();
        PathFinder pf = new PathFinder(turnIntoPathMap(restaurantMap));
        pf.setOrigin(this.x, this.y);
        pf.setTarget(targetX,targetY);
        path = pf.findPath();
    }

    public int nextFloor() {
        int f0 = floor[0];
        int f1 = floor[1];
        floor = new int[]{f1, 0};
        return f0;
    }

    public int getCurrentSpriteVariant() {
        return currentSpriteVariant;
    }

    public void setCurrentSpriteVariant(int currentSpriteVariant) {
        this.currentSpriteVariant = currentSpriteVariant;
    }

    public int getX() {
        return x;
    }

    public float getXWithOffset() {
        return ((float)x + xOffset);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public float getYWithOffset() {
        return ((float)y + yOffset);
    }

    public boolean onTarget() {
        return (targetX == x && targetY == y);
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] turnIntoPathMap(int[][] map) {

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


    //Actions


    @Override
    public void run() {

    }

    public void followPath() throws InterruptedException {

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
        } else if (distanceX == 0 && distanceY == 0){ }
        else {
            findPath();
            //throw new IllegalArgumentException();
        }
    }

    public void moveDown() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        if (map[x][y+1] != VOID && map[x][y+1] != TARGETED_POSITION && map[x][y+1] != STAIRS) {
            findPath();
            return;
        }
        facing = FRONT;
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

    public void moveDownstairs() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        currentSpriteVariant = CharacterSprite.LEFT_N;
        float diagonalSpeed = (float) ((float) speed *Math.sqrt(2));
        float increment = 1f/diagonalSpeed;
        floor[1] = map[x-2][y+1];
        Window.restaurant.addToMap(x-2,y+1,CHARACTER);
        for (int i = 0; i < (int)diagonalSpeed*2; i+=1) {
            xOffset -= increment;
            if (i > diagonalSpeed/2f && i < diagonalSpeed*3f/2f) {
                yOffset += increment;
            }
            sleep(1);
            if (i > (((float) speed)*6f)/4f) { currentSpriteVariant = CharacterSprite.LEFT_R; }
            else if (i > (((float) speed)*5f)/4f) {currentSpriteVariant = CharacterSprite.LEFT_N;}
            else if (i > ((float) speed)*4/4f) {currentSpriteVariant = CharacterSprite.LEFT_L;}
            else if (i > (((float) speed)*3f)/4f) { currentSpriteVariant = CharacterSprite.LEFT_R; }
            else if (i > (((float) speed)*2f)/4f) {currentSpriteVariant = CharacterSprite.LEFT_N;}
            else if (i > ((float) speed)/4f) {currentSpriteVariant = CharacterSprite.LEFT_L;}
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

    public void moveLeft() throws InterruptedException {
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
        facing = LEFT;
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

    public void moveRight() throws InterruptedException {
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
        facing = RIGHT;
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

    public void moveToTarget() throws InterruptedException {
        int distanceX = targetX - x;
        int distanceY = targetY - y;
        if ((facing == BACK && distanceY < 0) || (facing == FRONT && distanceY > 0)) {
            moveToTargetY();
        } else if ((facing == LEFT && distanceX < 0) || (facing == RIGHT && distanceX > 0)) {
            moveToTargetX();
        } else if (Math.abs(distanceX) < Math.abs(distanceY) && distanceX != 0) {
            moveToTargetX();
        } else if (distanceY != 0) {
            moveToTargetY();
        } else if (distanceX != 0) {
            moveToTargetX();
        }
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

    public void moveUp() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        if (map[x][y-1] != VOID && map[x][y-1] != TARGETED_POSITION && map[x][y-1] != STAIRS) {
            findPath();
            return;
        }
        facing = BACK;
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

    public void moveUpstairs() throws InterruptedException {
        int[][] map = Window.restaurant.getMap();
        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;
        int CHARACTER = Window.restaurant.CHARACTER;
        int STAIRS = Window.restaurant.STAIRS;
        currentSpriteVariant = CharacterSprite.RIGHT_N;
        float diagonalSpeed = (float) ((float) speed *Math.sqrt(2));
        float increment = 1f/diagonalSpeed;
        floor[1] = map[x+2][y-1];
        Window.restaurant.addToMap(x+2,y-1,CHARACTER);
        for (int i = 0; i < (int)diagonalSpeed*2; i+=1) {
            xOffset += increment;
            if (i > diagonalSpeed/2f && i < diagonalSpeed*3f/2f) {
                yOffset -= increment;
            }
            sleep(1);
            if (i > (((float) speed)*6f)/4f) { currentSpriteVariant = CharacterSprite.RIGHT_R; }
            else if (i > (((float) speed)*5f)/4f) {currentSpriteVariant = CharacterSprite.RIGHT_N;}
            else if (i > ((float) speed)*4/4f) {currentSpriteVariant = CharacterSprite.RIGHT_L;}
            else if (i > (((float) speed)*3f)/4f) { currentSpriteVariant = CharacterSprite.RIGHT_R; }
            else if (i > (((float) speed)*2f)/4f) {currentSpriteVariant = CharacterSprite.RIGHT_N;}
            else if (i > ((float) speed)/4f) {currentSpriteVariant = CharacterSprite.RIGHT_L;}
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

    public void testSprites() {
        currentSpriteVariant++;
        if (currentSpriteVariant > 17) {
            currentSpriteVariant = 0;
        }
    }

    public void turnAround() throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            if (facing == FRONT) {
                facing = LEFT;
                currentSpriteVariant = CharacterSprite.LEFT_N;
            } else if (facing == LEFT) {
                facing = BACK;
                currentSpriteVariant = CharacterSprite.BACK_N;
            } else if (facing == BACK) {
                facing = RIGHT;
                currentSpriteVariant = CharacterSprite.RIGHT_N;
            } else if (facing == RIGHT) {
                facing = FRONT;
                currentSpriteVariant = CharacterSprite.FRONT_N;
            }
            sleep(speed);
        }
    }

    public void wander() {
        if (onTarget()) {
            floor[0] = 0;
            floor[1] = 0;
            Random r = new Random();
            int[][] voidPositions = Window.restaurant.voidPositions();
            int[] newTarget = voidPositions[r.nextInt(voidPositions.length)];
            targetX = newTarget[0];
            targetY = newTarget[1];
            Window.restaurant.addToMap(targetX,targetY,Window.restaurant.TARGETED_POSITION);
        }
    }

}
