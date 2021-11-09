import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {


    //Attributes


    private int boxWidth = 40, boxHeight = 40;

    private boolean debugMode = true;
    private int width, height;
    private MySprites mySprites;


    //Constructor


    public Viewer(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        mySprites = new MySprites();
    }


    //Methods


    public synchronized void draw(Graphics g) {

        drawBackground(g);
        drawAllTables(g,Window.restaurant.getTables());
        drawAllCharacters(g, Window.restaurant.getCharacters());
        if (debugMode) {
            drawDebugBoxes(g, Window.restaurant.getMap());
        }
    }

    public void drawAllCharacters(Graphics g, ArrayList<Character> characters) {
        for (Character c : characters) {
            if (c instanceof  Chef) {
                drawChef(g, (Chef) c);
            } else if (c instanceof Client) {
                drawClient(g, (Client) c);
            }
        }
    }

    public void drawAllTables(Graphics g, ArrayList<Table> tables) {
        for (Table t : tables) {
            drawTable(g,t);
        }
    }

    public void drawBackground(Graphics g) {
        g.drawImage(mySprites.getSprite(mySprites.BACKGROUND),0,0,this);
        g.drawImage(mySprites.getSprite(mySprites.DOOR),960,160,this);
    }

    public void drawDebugBoxes(Graphics g, int[][] map) {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                g.drawImage(mySprites.getLevelSprite(mySprites.DEBUG_BOXES,map[x][y]+1),x*boxWidth,y*boxHeight,this);
            }
        }
    }

    public void drawChef(Graphics g, Chef chef) {
        BufferedImage img = mySprites.getCharacterSprite(mySprites.CHEF, chef.getCurrentSpriteVariant());
        g.drawImage(img, (int) (chef.getXWithOffset()*boxWidth), (int) ((chef.getYWithOffset()*boxHeight)-boxHeight/4f), this);
    }

    public void drawClient(Graphics g, Client client) {
        BufferedImage img = mySprites.getCharacterSprite(mySprites.CLIENT, client.getCurrentSpriteVariant());
        g.drawImage(img, (int) (client.getXWithOffset()*boxWidth), (int) ((client.getYWithOffset()*boxHeight)-boxHeight/4f), this);
    }

    public void drawMeal(Graphics g, int x, int y, int level) {
        g.drawImage(mySprites.getLevelSprite(mySprites.MEAL,level),x,y,this);
    }

    public void drawTable(Graphics g, Table table) {
        int w = table.width(),  h = table.height();
        int x = table.getX(),   y = table.getY();
        g.drawImage(mySprites.getScalableSprite(mySprites.TABLE,w,h),x*boxWidth,y*boxHeight,this);
        for (int tx = 0; tx < w; tx++) {
            for (int ty = 0; ty < h; ty++) {

                drawMeal(g, (x+tx)*boxWidth, ((y+ty)*boxHeight)-15, table.getMealAt(tx,ty));

            }
        }
    }

    public void update() {

        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        draw(g);
        g.dispose();
        bs.show();

    }

    @Override
    public void run() {
        while (true) {
            update();
        }
    }

}