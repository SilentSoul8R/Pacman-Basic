import java.util.Random;

public class Ghost {
    private int x;
    private int y;

    public Ghost() {
        this.x = 0;
        this.y = 0;
    }

    public Ghost(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int value) {
        this.x = value;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int value) {
        this.y = value;
    }

    public int getY() {
        return this.y;
    }

    public void move(char[][] maze, int m, int n) {
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

        // yaha yay pahlay coords check kar raha, than borders and obstacles inside the map to ensure we can move.
        if (newX >= 0 && newY >= 0 && newX < m && newY < n && maze[newX][newY] != '#') {
            this.setX(newX);
            this.setY(newY);
        }
    }
}