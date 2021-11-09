import static java.lang.Thread.sleep;

public class Chef extends Character {


    //Attributes


    private Table table;


    //Constructor


    public Chef(Table table, int x, int y) {
        super(x,y);
        this.table = table;
    }


    //Methods


    @Override
    public void run() {
        System.out.println("Chef thread started");
        try {
            while (true) {
                if (onTarget()) {
                    turnAround();
                    table.placeMeal();
                    table.placeMeal();
                    table.placeMeal();
                    wander();
                    findPath();
                }
                followPath();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
