import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Window extends JFrame  {

    public static Restaurant restaurant;

    public Window(String name, int width, int height) {
        super(name);
        this.setSize(width+15, height+39);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {

        int width = 1080, height = 720;

        restaurant = new Restaurant();

        Window window = new Window("Restaurant Simulator", width, height);
        Viewer viewer = new Viewer(width, height);
        window.add(viewer);
        window.setVisible(true);
        new Thread(viewer).start();

        Table table = new Table(13,8,3,2);
        restaurant.addTable(table);

        restaurant.addDecoration( new Decoration(24,4, Decoration.DOOR));
        restaurant.addDecoration( new Decoration(8,2, Decoration.PAINTING));
        restaurant.addDecoration( new Decoration(21,1, Decoration.SIGN));
        restaurant.addDecoration( new Decoration(1,4, Decoration.KITCHEN_BAR));
        restaurant.addDecoration( new Decoration(5,4, Decoration.KITCHEN_PIG));

        restaurant.addCharacter(new Chef(table, 2,5));
        restaurant.addCharacter(new Chef(table, 3,5));
        restaurant.addCharacter(new Chef(table, 4,5));
        restaurant.addCharacter(new Chef(table, 2,6));
        restaurant.addCharacter(new Chef(table, 3,6));
        restaurant.addCharacter(new Chef(table, 4,6));

        restaurant.addCharacter(new Client(table,12,11));
        restaurant.addCharacter(new Client(table,11,11));
        restaurant.addCharacter(new Client(table,10,11));
        restaurant.addCharacter(new Client(table,11,15));

        for (Character c : restaurant.getCharacters()) {
            new Thread(c).start();
        }

    }

}
