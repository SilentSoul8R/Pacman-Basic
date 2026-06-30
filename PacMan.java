public class PacMan extends Entity {
    private int score;

    public PacMan() {
        super(1, 1);  // Call parent constructor
        this.score = 0;
    }

    public PacMan(int x, int y) {
        super(x, y);  // Call parent constructor
        this.score = 0;
    }

    public void setScore(int value) {
        this.score = value;
    }

    public int getScore() {
        return this.score;
    }

    public void incrementScore() {
        this.score++;
    }


    @Override
    public void move(char[][] maze, int rows, int cols) {
        // PacMan doesn't move automatically - movement handled by user input
    }

    // Specific PacMan movement based on direction
    public void move(String direction) {
        if (direction.equals("w")) {
            this.setX(this.getX() - 1);
        } else if (direction.equals("s")) {
            this.setX(this.getX() + 1);
        } else if (direction.equals("a")) {
            this.setY(this.getY() - 1);
        } else if (direction.equals("d")) {
            this.setY(this.getY() + 1);
        }
    }

    // Override abstract method from Entity
    @Override
    public String getSymbol() {
        return "P1";
    }
}