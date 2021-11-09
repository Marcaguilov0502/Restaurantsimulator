import static java.lang.Thread.sleep;

public class Client extends Character {


    //Attributes


    private Table table;


    //Constructor


    public Client(Table table, int x, int y) {
        super(x,y);
        this.table = table;
    }

    @Override
    public void run() {
        System.out.println("Client thread started");
        try {
            while (true) {
                if (onTarget()) {
                    turnAround();
                    table.takeMeal();
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
