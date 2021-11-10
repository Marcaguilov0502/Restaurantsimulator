public class Decoration {


    //Constants


    public static final int
            DOOR = 0, PAINTING = 1, SIGN = 2,
            KITCHEN_BAR = 3, KITCHEN_PIG = 4;


    //Attributes


    private int x,y;
    private int type;

    private int level;


    //Constructor


    public Decoration(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        addToRestaurantMap(x,y,type);
    }


    //Methods


    private void addToRestaurantMap(int x, int y, int type) {
        int width = 1, height = 1;
        switch (type) {
            case DOOR:
                height = 2;
                break;
            case PAINTING:
                height = 3;
                break;
            case SIGN:
            case KITCHEN_PIG:
                width = 2;
                height = 2;
                break;
            case KITCHEN_BAR:
                height = 13;
                break;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (Window.restaurant.getMap()[x+i][y+j] == Window.restaurant.VOID) {
                    Window.restaurant.addToMap(x + i, y + j, Window.restaurant.DECORATION);
                }
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
