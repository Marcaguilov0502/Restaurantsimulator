import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Client extends Character {


    //Attributes





    //Constructor


    public Client(Table table, int x, int y) {
        super(x,y,table);
    }

    @Override
    public void run() {
        System.out.println("Client thread started");
        try {
            while (true) {
                if (isOnTarget()) {
                    if (isNextToTable() && isThereAnyMeal() && targetIsNextToTable()) {
                        lookAtTable();
                        eat();
                        targetRandom();
                    } else {
                        turnAround();
                    }
                    lookForTarget();
                }
                followPath();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void lookForTarget() {
        if (isThereAnyMeal()) {
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
        for (int i = 0; i < 8; i++) {
            currentSpriteVariant = CharacterSprite.ACTION1_1;
            sleep(speed/5);
            currentSpriteVariant = CharacterSprite.ACTION1_2;
            sleep(speed/5);
            currentSpriteVariant = CharacterSprite.ACTION1_3;
            sleep(speed/5);
            currentSpriteVariant = CharacterSprite.FRONT_N;
        }
        lookAtTable();
    }

    private boolean isThereAnyMeal() {
        return (table.placesWithMealCount() > 0 && Window.restaurant.tableAccessVoidsCount(table) > 0);
    }

}
