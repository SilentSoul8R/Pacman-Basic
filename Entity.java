public abstract class Entity {
    protected int x;
    protected int y;

    public Entity() {
        this.x = 0;
        this.y = 0;
    }

    public Entity(int x, int y) {
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


    public abstract void move(char[][] maze, int rows, int cols);


    public abstract String getSymbol();


    public boolean isAt(int row, int col) {
        return this.x == row && this.y == col;
    }


    public boolean collidesWith(Entity other) {
        return this.x == other.x && this.y == other.y;
    }
}