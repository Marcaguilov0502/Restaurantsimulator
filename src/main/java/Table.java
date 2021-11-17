import java.util.Random;

import static java.lang.Thread.sleep;

public class Table {


    //Constants


    private final int MAX_MEALS_PER_PLACE = 5;


    //Attributes


    private int eaten = 0, placed = 0;

    private final int x,y;

    private int[][] places;


    //Constructor


    public Table(int x, int y, int width, int height) {
        this.places = new int[width][height];
        this.x = x;
        this.y = y;
        for (int tx = x; tx < width+x; tx++) {
            for (int ty = y; ty < height+y; ty++) {
                Window.restaurant.addToMap(tx,ty,Window.restaurant.TABLE);
            }
        }

    }


    //Methods


    public int[][] freePlaces() {
        if (freePlacesCount() == 0) {
            return null;
        } else {
            int[][] freePlaces = new int[freePlacesCount()][2];
            int i = 0;
            for (int x = 0; x < places.length; x++) {
                for (int y = 0; y < places[0].length; y++) {

                    if (places[x][y] < MAX_MEALS_PER_PLACE) {
                        freePlaces[i][0] = x;
                        freePlaces[i][1] = y;
                        i++;
                    }

                }
            }
            return freePlaces;
        }
    }

    public int freePlacesCount() {
        int count = 0;
        for (int x = 0; x < places.length; x++) {
            for (int y = 0; y < places[0].length; y++) {
                if (places[x][y] < MAX_MEALS_PER_PLACE) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getEatenMeals() {
        return eaten;
    }

    public int getMealAt(int x, int y) {
        return places[x][y];
    }

    public int getPlacedMeals() {
        return placed;
    }

    public int[] getRandomPlace() {
        Random r = new Random();
        int x = r.nextInt(width());
        int y = r.nextInt(height());
        return new int[]{x,y};
    }

    public int[] getRandomPlace(int[][] posiblePlaces) {
        if (posiblePlaces == null) {
            return null;
        }
        Random r = new Random();
        int i = r.nextInt(posiblePlaces.length);
        return posiblePlaces[i];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int maxMeals() {
        return height()*width()*MAX_MEALS_PER_PLACE;
    }

    public int mealCount() {
        int mealCount = 0;
        for (int x = 0; x < places.length; x++) {
            for (int y = 0; y < places[0].length; y++) {
                mealCount += places[x][y];
            }
        }
        return mealCount;
    }

    public int height() {
        return places[0].length;
    }

    public synchronized void placeMeal() throws InterruptedException {
        while (freePlacesCount() == 0) {
            wait();
        }
        int[] place = getRandomPlace(freePlaces());
        places[place[0]][place[1]]++;
        System.out.println("A meal was placed");
        System.out.println("Total = " +mealCount());
        placed++;
        notifyAll();
    }

    public int[][] placesWithMeal() {
        if (placesWithMealCount() == 0) {
            return null;
        } else {
            int[][] placesWithMeal = new int[placesWithMealCount()][2];
            int i = 0;
            for (int x = 0; x < places.length; x++) {
                for (int y = 0; y < places[0].length; y++) {

                    if (places[x][y] > 0) {
                        placesWithMeal[i][0] = x;
                        placesWithMeal[i][1] = y;
                        i++;
                    }

                }
            }
            return placesWithMeal;
        }
    }

    public int placesWithMealCount() {
        int count = 0;
        for (int x = 0; x < places.length; x++) {
            for (int y = 0; y < places[0].length; y++) {
                if (places[x][y] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public synchronized void takeMeal() throws InterruptedException {
        while (placesWithMealCount() == 0) {
            wait();
        }
        int[] place = getRandomPlace(placesWithMeal());
        places[place[0]][place[1]]--;
        System.out.println("A meal was taken");
        System.out.println("Total = " +mealCount());
        eaten++;
        notifyAll();
    }

    public int width() {
        return places.length;
    }

}
