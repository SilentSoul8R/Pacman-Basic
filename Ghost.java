import java.util.Random;


public class Ghost extends Entity {
    private String ghostId;  // To differentiate between ghosts

    public Ghost() {
        super(0, 0);
        this.ghostId = "G";
    }

    public Ghost(int x, int y, String ghostId) {
        super(x, y);
        this.ghostId = ghostId;
    }

    public String getGhostId() {
        return ghostId;
    }

    // Override abstract method from Entity
    @Override
    public void move(char[][] maze, int rows, int cols) {
        Random rand = new Random();
        int randomMove = rand.nextInt(4);
        int newX = this.getX();
        int newY = this.getY();

        if (randomMove == 0) {
            newX = newX - 1;
        } else if (randomMove == 1) {
            newX = newX + 1;
        } else if (randomMove == 2) {
            newY = newY - 1;
        } else if (randomMove == 3) {
            newY = newY + 1;
        }

        // Check coordinates first, then borders and obstacles
        if (newX >= 0 && newY >= 0 && newX < rows && newY < cols && maze[newX][newY] != '#') {
            this.setX(newX);
            this.setY(newY);
        }
    }

    // Override abstract method from Entity
    @Override
    public String getSymbol() {
        return ghostId;
    }
}