import java.util.Random;

import static java.lang.Thread.sleep;

public class Chef extends Character {


    //Attributes


    boolean hasMeal = false;


    //Constructor


    public Chef(Table table, int x, int y) {
        super(x,y,table);
    }


    //Methods


    @Override
    public void run() {
        System.out.println("Chef thread started");
        try {
            while (true) {
                if (Window.restaurant.paused()) {
                    sleep(1500);
                    continue;
                }
                if (isOnTarget()) {
                    if (exit && x == 3 && y == 5) {
                        Window.restaurant.getCharacters().remove(this);
                        break;
                    } else if (isInKitchen() && !hasMeal) {
                        cook();
                        lookForTarget();
                    } else if (isNextToTable() && hasMeal && isThereAnyPlace()) {
                        lookAtTable();
                        serve();
                        lookForTarget();
                    } else {
                        turnAround();
                        lookForTarget();
                    }
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
        target(new int[]{3,5});
    }

    @Override
    protected void lookForTarget() {
        if (exit) {
            targetExit();
        } else if (hasMeal && isThereAnyPlace()) {
            targetTable();
        } else if (!hasMeal) {
            targetKitchen();
        } else {
            targetRandom();
        }
    }

    @Override
    protected void targetRandom() {
        Random r = new Random();
        int[][] voidPositions = Window.restaurant.voidPositions();
        int[] position = voidPositions[r.nextInt(voidPositions.length)];
        target(position);
    }

    private void cook() throws InterruptedException {
        currentSpriteVariant = CharacterSprite.FRONT_N;
        sleep((long) (speed/2));
        currentSpriteVariant = CharacterSprite.ACTION1_1;
        sleep((long) (speed/2));
        currentSpriteVariant = CharacterSprite.ACTION1_2;
        sleep((long) (speed/2));
        currentSpriteVariant = CharacterSprite.ACTION1_3;
        sleep((long) speed);
        currentSpriteVariant = CharacterSprite.FRONT_N;
        hasMeal = true;
    }

    private boolean isInKitchen() {
        return (x < 9);
    }

    private boolean isThereAnyPlace() {
        return (table.freePlacesCount() > 0);
    }


    private void serve() throws InterruptedException {
        approachToTable();
        table.placeMeal();
        hasMeal = false;
    }


}
