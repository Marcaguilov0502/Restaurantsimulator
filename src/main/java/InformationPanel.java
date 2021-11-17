import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;

public class InformationPanel extends JPanel implements Runnable{

    public InformationPanel() {
        super();
        this.setBackground(new Color(153, 153, 153));
        setLayout(new GridBagLayout());
        addFields();

    }

    JLabel mealCount = new JLabel("Meals = 0");
    JLabel chefCount = new JLabel("Chefs = 0");
    JLabel clientCount = new JLabel("Clients = 0");
    JLabel averageSpeed = new JLabel("Avg Speed = 0");

    JLabel eatenMeals = new JLabel("Eaten Meals = 0");
    JLabel placedMeals = new JLabel("Placed Meals = 0");

    private void addFields() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = LINE_START;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(mealCount,gbc);

        gbc.gridy = 1;
        add(chefCount,gbc);

        gbc.gridy = 2;
        add(clientCount,gbc);

        gbc.gridy = 3;
        add(averageSpeed,gbc);

        gbc.gridy = 4;
        add(placedMeals,gbc);

        gbc.gridy = 5;
        add(eatenMeals,gbc);

    }

    @Override
    public void run() {
        while (true) {
            updateMealCount();
            updateChefCount();
            updateClientCount();
            updateAverageSpeed();
            updatePlacedMeals();
            updateEatenMeals();
        }
    }

    private void updateMealCount() {
        if ( Window.restaurant.getTables().size() == 0) {return;}
        int mealCount = Window.restaurant.getTables().get(0).mealCount();
        int maxMeals =  Window.restaurant.getTables().get(0).maxMeals();
        this.mealCount.setText("Meals = "+mealCount+"/"+maxMeals);
    }

    private void updateChefCount() {
        int chefCount = 0;
        int exitingCount = 0;

        for (Character c : Window.restaurant.getCharacters()) {
            if (c instanceof Chef) {
                chefCount++;
                if (c.exit) {
                    exitingCount++;
                }
            }
        }
        String exiting = " - " + exitingCount;
        if (exitingCount == 0) {
            exiting = "";
        }
        this.chefCount.setText("Chefs = " + chefCount + exiting);
    }

    private void updateClientCount() {
        int clientCount = 0;
        int exitingCount = 0;

        for (Character c : Window.restaurant.getCharacters()) {
            if (c instanceof Client) {
                clientCount++;
                if (c.exit) {
                    exitingCount++;
                }
            }
        }
        String exiting = " - " + exitingCount;
        if (exitingCount == 0) {
            exiting = "";
        }
        this.clientCount.setText("Clients = " + clientCount+ exiting);
    }

    private void updateAverageSpeed() {
        float speedTotal = 0;
        float speedCount = 0;
        int averageSpeed;
        for (Character c : Window.restaurant.getCharacters()) {
            speedTotal += c.speed;
            speedCount++;
        }
        if (speedCount == 0) {
            averageSpeed = 0;
        } else {
            averageSpeed = (int) (speedTotal/speedCount);
        }

        this.averageSpeed.setText("Avg Speed = "+ averageSpeed);

    }

    private void updatePlacedMeals() {
        if ( Window.restaurant.getTables().size() == 0) {return;}
        int placedMeals = Window.restaurant.getTables().get(0).getPlacedMeals();
        this.placedMeals.setText("Placed Meals = "+placedMeals);
    }

    private void updateEatenMeals() {
        if ( Window.restaurant.getTables().size() == 0) {return;}
        int eatenMeals = Window.restaurant.getTables().get(0).getEatenMeals();
        this.eatenMeals.setText("Placed Meals = "+eatenMeals);
    }

}
