import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static java.awt.GridBagConstraints.*;

public class Window extends JFrame  {

    public static Restaurant restaurant;

    public Window(String name) {
        super(name);
        //this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setResizable(false);
        this.setLayout(new GridBagLayout());

    }

    public void showWindow() throws IOException {
        int vWidth = 1080, vHeight = 720;
        int cpWidth = 300, cpHeight = 720;
        Viewer viewer = new Viewer();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = BOTH;

        this.getContentPane().add(viewer, gbc);

        ControlPanel controlPanel = new ControlPanel();

        gbc.weightx = 0.1f;
        gbc.gridx = 1;
        gbc.gridy = 0;

        this.getContentPane().add(controlPanel, gbc);


        viewer.setSize(vWidth-56, vHeight-56);
        controlPanel.setSize(cpWidth, cpHeight-56);
        this.setVisible(true);
        new Thread(viewer).start();
        this.setSize(vWidth+cpWidth-40, cpHeight-17);

    }

    public static void createRestaurant() {

        restaurant = new Restaurant();

        Table table = new Table(13,8,3,2);
        restaurant.addTable(table);

        restaurant.addDecoration( new Decoration(3,3, Decoration.DOOR));
        restaurant.addDecoration( new Decoration(24,4, Decoration.DOOR));
        restaurant.addDecoration( new Decoration(8,2, Decoration.PAINTING));
        restaurant.addDecoration( new Decoration(21,1, Decoration.SIGN));
        restaurant.addDecoration( new Decoration(1,4, Decoration.KITCHEN_BAR));
        restaurant.addDecoration( new Decoration(5,4, Decoration.KITCHEN_PIG));
    }

    public static void main(String[] args) throws IOException {

        Window window = new Window("Restaurant Simulator");
        createRestaurant();
        window.showWindow();

    }

}