import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScalableSprite extends Sprite {


    //Constants


    public final int
            ONLY = 0,               CORNER_LEFT = 1,        HORIZONTAL_MIDDLE = 2,  CORNER_RIGHT = 3,
            CORNER_UP = 4,          CORNER_UP_LEFT = 5,     EDGE_UP = 6,            CORNER_UP_RIGHT = 7,
            VERTICAL_MIDDLE = 8,    EDGE_LEFT = 9,          MIDDLE = 10,            EDGE_RIGHT = 11,
            CORNER_DOWN = 12,       CORNER_DOWN_LEFT = 13,  EDGE_DOWN = 14,         CORNER_DOWN_RIGHT = 15;


    //Attributes


    private int size;

    private Sprite
            only,               cornerLeft,         horizontalMiddle,   cornerRight,
            cornerUp,           cornerUpLeft,       edgeUp,             cornerUpRight,
            verticalMiddle,     edgeLeft,           middle,             edgeRight,
            cornerDown,         cornerDownLeft,     edgeDown,           cornerDownRight;


    //Constructor


    public ScalableSprite(String fileName) throws IOException {
        super(fileName);
        if (image.getWidth() != image.getHeight() || image.getWidth() % 4 != 0) {
            throw new IllegalArgumentException("Not a correct-sized image for an scalable sprite");
        }
        size = image.getWidth()/4;
        generateSprites();
    }


    //Methods


    public Sprite composite(int width, int height) {

        Sprite composition = new Sprite(new BufferedImage(width*size, height*size, BufferedImage.TYPE_INT_ARGB));
        Graphics2D g = composition.image.createGraphics();

        if (width < 1 || height < 1) {throw new IllegalArgumentException("Sprite must have positive dimensions");}

        if (width == 1 && height == 1) {
            g.drawImage(only.image,0,0,null);
        } else if (width > 1 && height == 1) {
            g.drawImage(cornerLeft.image,0,0,null);
            for (int i = 1; i < width-1; i++) {
                g.drawImage(horizontalMiddle.image, size*i,0,null);
            }
            g.drawImage(cornerRight.image,(width-1)*size,0,null);
        } else if (width == 1 && height > 1) {
            g.drawImage(cornerUp.image,0,0,null);
            for (int i = 1; i < height-1; i++) {
                g.drawImage(verticalMiddle.image, 0,size*i,null);
            }
            g.drawImage(cornerDown.image,0,(height-1)*size,null);
        } else {

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    if (y == 0) {
                        g.drawImage(edgeUp.image, x*size, y*size,null);
                    } else if (y == height-1) {
                        g.drawImage(edgeDown.image, x*size, y*size,null);
                    } else {
                        if (x == 0) {
                            g.drawImage(edgeLeft.image, x*size, y*size,null);
                        } else if (x == width-1) {
                            g.drawImage(edgeRight.image, x*size, y*size,null);
                        } else {
                            g.drawImage(middle.image, x*size, y*size,null);
                        }
                    }


                }
            }
            g.drawImage(cornerUpLeft.image,0,0,null);
            g.drawImage(cornerUpRight.image,(width-1)*size,0,null);
            g.drawImage(cornerDownLeft.image,0,(height-1)*size,null);
            g.drawImage(cornerDownRight.image,(width-1)*size,(height-1)*size,null);
        }
        g.dispose();
        return composition;
    }

    public void generateSprites() {

        only = new Sprite(image.getSubimage(0,0,size,size));

        cornerLeft = new Sprite(image.getSubimage(size,0,size,size));
        horizontalMiddle = new Sprite(image.getSubimage(size*2,0,size,size));
        cornerRight = new Sprite(image.getSubimage(size*3,0,size,size));

        cornerUp = new Sprite(image.getSubimage(0,size,size,size));
        verticalMiddle = new Sprite(image.getSubimage(0,size*2,size,size));
        cornerDown = new Sprite(image.getSubimage(0,size*3,size,size));

        cornerUpLeft = new Sprite(image.getSubimage(size,size,size,size));
        edgeUp = new Sprite(image.getSubimage(size*2,size,size,size));
        cornerUpRight = new Sprite(image.getSubimage(size*3,size,size,size));

        edgeLeft = new Sprite(image.getSubimage(size,size*2,size,size));
        middle = new Sprite(image.getSubimage(size*2,size*2,size,size));
        edgeRight = new Sprite(image.getSubimage(size*3,size*2,size,size));

        cornerDownLeft = new Sprite(image.getSubimage(size,size*3,size,size));
        edgeDown = new Sprite(image.getSubimage(size*2,size*3,size,size));
        cornerDownRight = new Sprite(image.getSubimage(size*3,size*3,size,size));


    }

    public Sprite getSpritePart(int spritePartID) {
        switch (spritePartID) {
            case ONLY: return only;

            case CORNER_LEFT: return cornerLeft;
            case HORIZONTAL_MIDDLE: return horizontalMiddle;
            case CORNER_RIGHT: return cornerRight;

            case CORNER_UP: return cornerUp;
            case VERTICAL_MIDDLE: return verticalMiddle;
            case CORNER_DOWN: return cornerDown;

            case CORNER_UP_LEFT: return cornerUpLeft;
            case EDGE_UP: return edgeUp;
            case CORNER_UP_RIGHT: return cornerUpRight;

            case EDGE_LEFT: return edgeLeft;
            case MIDDLE: return middle;
            case EDGE_RIGHT: return edgeRight;

            case CORNER_DOWN_LEFT: return cornerDownLeft;
            case EDGE_DOWN: return edgeDown;
            case CORNER_DOWN_RIGHT: return cornerDownRight;

            default:
                throw new IllegalArgumentException("Not existing sprite part " + spritePartID);
        }
    }

}
