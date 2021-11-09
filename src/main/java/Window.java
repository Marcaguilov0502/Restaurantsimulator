import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

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

        Table table = new Table(13,8,5,3);
        restaurant.addTable(table);

        restaurant.addCharacter(new Chef(table, 12,11));
        //restaurant.addCharacter(new Chef(table, 11,11));
        //restaurant.addCharacter(new Chef(table, 13,11));
        //restaurant.addCharacter(new Chef(table, 12,12));
        restaurant.addCharacter(new Client(table,5,5));
        restaurant.addCharacter(new Client(table,5,6));
        //restaurant.addCharacter(new Client(table,6,5));
        //restaurant.addCharacter(new Client(table,6,6));



        for (Character c : restaurant.getCharacters()) {
            new Thread(c).start();
        }

    }

}
