import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MySprites {


    //Attributes


    private Sprite background;          public final int BACKGROUND = 0;
    private CharacterSprite chef;       public final int CHEF = 1;
    private CharacterSprite client;     public final int CLIENT = 2;
    private Sprite door;                public final int DOOR = 3;
    private ScalableSprite table;       public final int TABLE = 4;
    private LevelSprite meal;           public final int MEAL = 5;
    private LevelSprite debugBoxes;     public final int DEBUG_BOXES = 6;


    //Constructor


    public MySprites() throws IOException {
        importAll();
    }


    //Methods


    public void importAll() throws IOException {
        background = new Sprite("background.png");
        chef = new CharacterSprite("chef.png");
        client = new CharacterSprite("client.png");
        door = new Sprite("door.png");
        table = new ScalableSprite("table.png");
        meal = new LevelSprite("meal.png",40,40);
        debugBoxes = new LevelSprite("debug_boxes.png",40,40);
    }

    public BufferedImage getCharacterSprite(int spriteID, int variantID) {
        switch (spriteID) {
            case CLIENT:
                return client.getSpriteVariant(variantID).image;
            case CHEF:
                return chef.getSpriteVariant(variantID).image;
            default:
                throw new IllegalArgumentException("Not existing sprite " + spriteID);
        }
    }

    public BufferedImage getLevelSprite(int spriteID, int level) {
        switch (spriteID) {
            case MEAL:
                return meal.getLevel(level).image;
            case DEBUG_BOXES:
                return debugBoxes.getLevel(level).image;
            default:
                throw new IllegalArgumentException("Not existing sprite " + spriteID);
        }
    }

    public BufferedImage getScalableSprite(int spriteID, int width, int height) {
        switch (spriteID) {
            case TABLE:
                return table.composite(width,height).image;
            default:
                throw new IllegalArgumentException("Not existing sprite " + spriteID);
        }
    }

    public BufferedImage getSprite(int spriteID) {
        switch (spriteID) {
            case BACKGROUND:
                return background.image;
            case DOOR:
                return door.image;
            default:
                throw new IllegalArgumentException("Not existing sprite " + spriteID);
        }
    }

}
