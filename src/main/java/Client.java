import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Client extends Character {


    //Attributes


    private boolean isHungry = true;


    //Constructor


    public Client(Table table, int x, int y) {
        super(x,y,table);
    }

    @Override
    public void run() {
        System.out.println("Client thread started");
        try {
            while (true) {
                if (Window.restaurant.paused()) {
                    sleep(1500);
                    continue;
                }
                if (isOnTarget()) {
                    if (exit && x == 24 && y == 6) {
                        Window.restaurant.getCharacters().remove(this);
                        break;
                    } else if (isNextToTable() && isThereAnyMeal() && isHungry) {
                        lookAtTable();
                        approachToTable();
                        eat();
                    } else {
                        turnAround();
                        isHungry = true;
                    }
                    lookForTarget();
                }
                followPath();
            }
            Window.restaurant.addToMap(x,y,Window.restaurant.VOID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void targetExit() {
        target(new int[]{24,6});
    }

    @Override
    protected void lookForTarget() {
        if (exit) {
            targetExit();
        } else if (isThereAnyMeal() && isHungry) {
            targetTable();
        } else {
            targetRandom();
        }
    }

    @Override
    protected int[][] turnIntoPathMap(int[][] map) {

        int VOID = Window.restaurant.VOID;
        int TARGETED_POSITION = Window.restaurant.TARGETED_POSITION;

        int[][] pathFindingMap = new int[map.length][map[0].length];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == VOID || map[x][y] == TARGETED_POSITION) {
                    pathFindingMap[x][y] = PathFinder.VOID;
                } else {
                    pathFindingMap[x][y] = PathFinder.OBSTACLE;
                }
            }
        }
        return pathFindingMap;
    }

    @Override
    protected void targetRandom() {
        Random r = new Random();
        int[][] voidPositions = Window.restaurant.voidPositions();
        ArrayList<int[]> positions = new ArrayList<>();
        for (int[] p : voidPositions) {
            if (p[0] > 9) {
                positions.add(p);
            }
        }
        int[] position = positions.get(r.nextInt(positions.size()));
        target(position);
    }

    private void eat() throws InterruptedException {
        table.takeMeal();
        for (int i = 0; i < 15; i++) {
            currentSpriteVariant = CharacterSprite.ACTION1_1;
            sleep((long) (speed/4));
            currentSpriteVariant = CharacterSprite.ACTION1_2;
            sleep((long) (speed/4));
            currentSpriteVariant = CharacterSprite.ACTION1_3;
            sleep((long) (speed/4));
            currentSpriteVariant = CharacterSprite.FRONT_N;
        }
        lookAtTable();
        isHungry = false;
    }

    private boolean isThereAnyMeal() {
        return (table.placesWithMealCount() > 0 && Window.restaurant.tableAccessVoidsCount(table) > 0);
    }

}
