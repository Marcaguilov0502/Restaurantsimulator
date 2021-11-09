import java.io.IOException;

public class CharacterSprite extends Sprite {


    //Constants


    public static final int
            FRONT_R =   0,  FRONT_N =   1,  FRONT_L =   2,

            BACK_R =    3,  BACK_N =    4,  BACK_L =    5,

            RIGHT_R =   6,  RIGHT_N =   7,  RIGHT_L =   8,

            LEFT_R =    9,  LEFT_N =    10, LEFT_L =    11,

            ACTION1_1 = 12, ACTION1_2 = 13, ACTION1_3 = 14,

            ACTION2_1 = 15, ACTION2_2 = 16, ACTION2_3 = 17;


    //Attributes


    private int width, height;

    private Sprite
            frontR,     frontN,     frontL,

            backR,      backN,      backL,

            rightR,     rightN,     rightL,

            leftR,      leftN,      leftL,

            action1_1,  action1_2,  action1_3,

            action2_1,  action2_2,  action2_3;


    //Constructor


    public CharacterSprite(String fileName) throws IOException {
        super(fileName);
        int w = image.getWidth(), h = image.getHeight();
        if (w%6 != 0 || h%3 != 0) {
            throw new IllegalArgumentException("Not a correct-sized image for a character sprite");
        }
        this.width = w/6;
        this.height = h/3;
        this.generateSprites();
    }


    //Methods

    
    public void generateSprites() {

        frontR = new Sprite(image.getSubimage(0,0,width,height));
        frontN = new Sprite(image.getSubimage(width,0,width,height));
        frontL = new Sprite(image.getSubimage(width*2,0,width,height));

        backR = new Sprite(image.getSubimage(width*3,0,width,height));
        backN = new Sprite(image.getSubimage(width*4,0,width,height));
        backL = new Sprite(image.getSubimage(width*5,0,width,height));

        rightR = new Sprite(image.getSubimage(0,height,width,height));
        rightN = new Sprite(image.getSubimage(width,height,width,height));
        rightL = new Sprite(image.getSubimage(width*2,height,width,height));

        leftR = new Sprite(image.getSubimage(width*3,height,width,height));
        leftN = new Sprite(image.getSubimage(width*4,height,width,height));
        leftL = new Sprite(image.getSubimage(width*5,height,width,height));

        action1_1 = new Sprite(image.getSubimage(0,height*2,width,height));
        action1_2 = new Sprite(image.getSubimage(width,height*2,width,height));
        action1_3 = new Sprite(image.getSubimage(width*2,height*2,width,height));

        action2_1 = new Sprite(image.getSubimage(width*3,height*2,width,height));
        action2_2 = new Sprite(image.getSubimage(width*4,height*2,width,height));
        action2_3 = new Sprite(image.getSubimage(width*5,height*2,width,height));

    }

    public Sprite getSpriteVariant(int spriteVariantID) {
        switch (spriteVariantID) {

            case FRONT_R: return frontR;
            case FRONT_N: return frontN;
            case FRONT_L: return frontL;

            case BACK_R: return backR;
            case BACK_N: return backN;
            case BACK_L: return backL;

            case RIGHT_R: return rightR;
            case RIGHT_N: return rightN;
            case RIGHT_L: return rightL;

            case LEFT_R: return leftR;
            case LEFT_N: return leftN;
            case LEFT_L: return leftL;

            case ACTION1_1: return action1_1;
            case ACTION1_2: return action1_2;
            case ACTION1_3: return action1_3;

            case ACTION2_1: return action2_1;
            case ACTION2_2: return action2_2;
            case ACTION2_3: return action2_3;

            default:
                throw new IllegalArgumentException("Not existing sprite variant " + spriteVariantID);
        }
    }

}
