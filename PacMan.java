public class PacMan {
    private int x;
    private int y;
    private int score;

    public PacMan() {
        this.x = 1;
        this.y = 1;
        this.score = 0;
    }

    public PacMan(int x, int y) {
        this.x = x;
        this.y = y;
        this.score = 0;
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

    public void setScore(int value) {
        this.score = value;
    }

    public int getScore() {
        return this.score;
    }

    public void incrementScore() {
        this.score++;
    }

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
}
