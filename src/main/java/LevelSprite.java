import java.io.IOException;

public class LevelSprite extends Sprite{


    //Attributes

    private int width, height;
    private Sprite[] levels;


    //Constructor


    public LevelSprite(String fileName, int width, int height) throws IOException {
        super(fileName);
        this.width = width;
        this.height = height;
        generateSprites();
    }


    //Methods


    public void generateSprites() {
        int columns = (image.getWidth()/width);
        int rows = (image.getHeight()/height);

        levels = new Sprite[columns*rows];

        int i = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {

                levels[i] = new Sprite(image.getSubimage(x*width,y*height, width, height));
                i++;

            }
        }
    }

    public Sprite getLevel(int level) {
        return levels[level];
    }

}
