import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import static java.awt.GridBagConstraints.BOTH;

public class ControlPanel extends JPanel {

    public ControlPanel() {
        super();
        this.setLayout(new GridBagLayout());
        addComponents();
        addListeners();
        new Thread(informationPanel).start();
        this.setBackground(new Color(135, 139, 145));
    }

    InformationPanel informationPanel = new InformationPanel();

    Button addChef = new Button("Add Chef");
    Button removeChef = new Button("Remove Chef");

    Button addClient = new Button("Add Client");
    Button removeClient = new Button("Remove Client");

    Button pause = new Button("Pause");
    Button halfSpeed = new Button("Speed / 2");
    Button normalSpeed = new Button("Normal Speed");
    Button doubleSpeed = new Button("Speed * 2");

    Button reset = new Button("Reset");

    Button debugMode = new Button("Debug Mode: OFF");


    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 1;
        gbc.weighty = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.fill = BOTH;
        add(informationPanel, gbc);
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(removeChef, gbc);

        gbc.gridx = 1;
        add(addChef, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(removeClient, gbc);

        gbc.gridx = 1;
        add(addClient, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(pause, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        add(halfSpeed, gbc);

        gbc.gridx = 1;
        add(doubleSpeed, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(normalSpeed, gbc);

        gbc.gridx = 1;
        add(reset, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(debugMode, gbc);

    }

    private void addListeners() {

        removeChef.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window.restaurant.removeChef();
            }
        });

        addChef.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window.restaurant.addChef();
            }
        });

        removeClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window.restaurant.removeClient();
            }
        });

        addClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window.restaurant.addClient();
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window.restaurant.pause();
                if (Window.restaurant.paused()) {
                    pause.setLabel("Play");
                } else {
                    pause.setLabel("Pause");
                }
            }
        });

        halfSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Character c : Window.restaurant.getCharacters()) {
                    c.halfSpeed();
                }
            }
        });

        doubleSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Character c : Window.restaurant.getCharacters()) {
                    c.doubleSpeed();
                }
            }
        });

        normalSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Character c : Window.restaurant.getCharacters()) {
                    c.setNormalSpeed();
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Character c : Window.restaurant.getCharacters()) {
                    c.delete();
                }
                Window.restaurant = new Restaurant();
                Window.createRestaurant();
            }
        });

        debugMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Viewer.debugMode = !Viewer.debugMode;
                if (Viewer.debugMode) {
                    debugMode.setLabel("Debug Mode: ON");
                } else {
                    debugMode.setLabel("Debug Mode: OFF");
                }
            }
        });

    }

}
