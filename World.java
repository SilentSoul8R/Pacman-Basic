import java.util.ArrayList;
import java.util.List;

public class World {
    private char[][] maze;
    private int rows;
    private int cols;
    private PacMan pacman;
    private List<Ghost> ghosts;  // Use List for polymorphism
    private int foodCount;
    private int lives;
    private volatile boolean gameRunning;
    private RawConsoleInput rawInput;
    private StringBuilder screenBuffer;

    public World(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.maze = new char[rows][cols];
        this.pacman = new PacMan(1, 1);

        // Store ghosts in a list
        this.ghosts = new ArrayList<>();
        this.ghosts.add(new Ghost(rows - 2, cols - 2, "G1"));
        this.ghosts.add(new Ghost(rows - 2, 1, "G2"));

        this.foodCount = 0;
        this.lives = 5;
        this.gameRunning = true;
        this.rawInput = new RawConsoleInput();
        this.screenBuffer = new StringBuilder();
        initializeMaze();
    }


    public char[][] getMaze() {
        return maze;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public PacMan getPacman() {
        return pacman;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public int getLives() {
        return lives;
    }

    public void initializeMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Outer boundaries
                if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                    maze[i][j] = '#';
                }
                // Horizontal walls
                else if ((i == 3 || i == 16) && j >= 6 && j <= 24) {
                    maze[i][j] = '#';
                }
                // Corner pieces near top
                else if ((i == 2 && (j == 6 || j == 24)) || (i == 17 && (j == 6 || j == 24))) {
                    maze[i][j] = '#';
                }
                // Vertical outer walls
                else if ((j == 2 || j == 27) && i > 3 && i < 16) {
                    maze[i][j] = '#';
                }
                // Vertical inner walls
                else if ((j == 7 || j == 25) && i > 2 && i < 14) {
                    maze[i][j] = '#';
                }
                // Small vertical sections
                else if (i == 10 && j >= 3 && j <= 3) {
                    maze[i][j] = '#';
                }
                // Inner maze structures
                else if (i == 8 && j > 13 && j <= 17) {
                    maze[i][j] = '#';
                }
                else if (i == 7 && j >= 7 && j <= 10) {
                    maze[i][j] = '#';
                }
                else if (j == 10 && i > 7 && i < 12) {
                    maze[i][j] = '#';
                }
                else if (i == 11 && j > 10 && j <= 15) {
                    maze[i][j] = '#';
                }
                else if (i == 14 && j >= 7 && j <= 9) {
                    maze[i][j] = '#';
                }
                else if (j == 19 && i > 8 && i < 11) {
                    maze[i][j] = '#';
                }
                else if (i == 6 && j > 20 && j < 25) {
                    maze[i][j] = '#';
                }
                else if (i == 5 && j > 14 && j <= 19) {
                    maze[i][j] = '#';
                }
                // Food dots
                else {
                    maze[i][j] = '.';
                    foodCount++;
                }
            }
        }
    }

    public void clearScreen() {
        ConsoleUtils.clearScreen();
    }

    public void displayBoard() {
        screenBuffer.setLength(0);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean entityDrawn = false;

                // Check PacMan position
                if (pacman.isAt(i, j)) {
                    screenBuffer.append(pacman.getSymbol());
                    entityDrawn = true;
                }

                // Check all ghosts using common Entity interface
                if (!entityDrawn) {
                    for (Entity ghost : ghosts) {
                        if (ghost.isAt(i, j)) {
                            screenBuffer.append(ghost.getSymbol());
                            entityDrawn = true;
                            break;
                        }
                    }
                }

                // Draw maze if no entity
                if (!entityDrawn) {
                    screenBuffer.append(maze[i][j]).append(" ");
                }
            }
            screenBuffer.append("\n");
        }

        screenBuffer.append("\nScore: ").append(pacman.getScore())
                .append(" | Lives: ").append(lives)
                .append(" | Food Remaining: ").append(foodCount).append("\n");
        screenBuffer.append("Move (w/a/s/d) or q to quit - NO ENTER NEEDED!\n");

        ConsoleUtils.clearScreen();
        System.out.print(screenBuffer.toString());
        System.out.flush();
    }

    // Use common Entity interface for collision detection
    public boolean detectCollision() {
        for (Entity ghost : ghosts) {
            if (pacman.collidesWith(ghost)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void movePacman(String direction) {
        int prevX = pacman.getX();
        int prevY = pacman.getY();

        pacman.move(direction);

        // Check boundaries and walls
        if (pacman.getX() < 0 || pacman.getX() >= rows ||
                pacman.getY() < 0 || pacman.getY() >= cols ||
                maze[pacman.getX()][pacman.getY()] == '#') {
            pacman.setX(prevX);
            pacman.setY(prevY);
        }
        // Eat food
        else if (maze[pacman.getX()][pacman.getY()] == '.') {
            maze[pacman.getX()][pacman.getY()] = ' ';
            pacman.incrementScore();
            foodCount--;
        }
    }


    public synchronized void moveGhosts() {
        for (Entity ghost : ghosts) {
            ghost.move(maze, rows, cols);
        }
    }

    public synchronized boolean gameFinished() {
        return foodCount == 0 || lives == 0;
    }

    public synchronized void resetPositions() {
        lives--;
        if (lives > 0) {
            pacman.setX(1);
            pacman.setY(1);

            // Reset all ghosts to starting positions
            ghosts.get(0).setX(rows - 2);
            ghosts.get(0).setY(cols - 2);
            ghosts.get(1).setX(rows - 2);
            ghosts.get(1).setY(1);

            System.out.println("\nGhost caught PacMan! Remaining lives: " + lives);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void play() {
        rawInput.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ConsoleUtils.hideCursor();

        long lastGhostMove = System.currentTimeMillis();
        long ghostMoveInterval = 600;
        long lastRender = System.currentTimeMillis();
        long renderInterval = 150;

        displayBoard();

        while (!gameFinished()) {
            long currentTime = System.currentTimeMillis();

            char key = rawInput.getKey();
            if (key != 0) {
                String command = String.valueOf(key).toLowerCase();
                if (command.equals("q")) {
                    System.out.println("\nGame quit by user!");
                    break;
                }
                if (command.equals("w") || command.equals("a") ||
                        command.equals("s") || command.equals("d")) {
                    movePacman(command);
                }
            }

            if (currentTime - lastGhostMove >= ghostMoveInterval) {
                moveGhosts();
                lastGhostMove = currentTime;

                if (detectCollision()) {
                    resetPositions();
                }
            }

            if (currentTime - lastRender >= renderInterval) {
                displayBoard();
                lastRender = currentTime;
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        gameRunning = false;
        rawInput.stop();
        ConsoleUtils.showCursor();
        displayBoard();

        if (foodCount == 0) {
            System.out.println("\n You Win! Final score: " + pacman.getScore());
        } else if (lives == 0) {
            System.out.println("\n Game Over! Final score: " + pacman.getScore());
        }
    }
}