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
                if (isOnTarget()) {
                    if (isInKitchen() && !hasMeal) {
                        cook();
                        lookForTarget();
                    } else if (isNextToTable() && hasMeal && isThereAnyPlace()) {
                        lookAtTable();
                        sleep(speed);
                        table.placeMeal();
                        hasMeal = false;
                        lookForTarget();
                    } else {
                        turnAround();
                        lookForTarget();
                    }
                }
                followPath();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void lookForTarget() {
        if (hasMeal && isThereAnyPlace()) {
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
        sleep(speed/2);
        currentSpriteVariant = CharacterSprite.ACTION1_1;
        sleep(speed/2);
        currentSpriteVariant = CharacterSprite.ACTION1_2;
        sleep(speed/2);
        currentSpriteVariant = CharacterSprite.ACTION1_3;
        sleep(speed);
        currentSpriteVariant = CharacterSprite.FRONT_N;
        hasMeal = true;
    }

    private boolean isInKitchen() {
        return (x < 9);
    }

    private boolean isThereAnyPlace() {
        return (table.freePlacesCount() > 0);
    }



}
