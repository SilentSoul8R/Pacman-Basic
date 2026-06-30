public class World {
    private char[][] maze;
    private int rows;
    private int cols;
    private PacMan pacman;
    private Ghost ghost1;
    private Ghost ghost2;
    private int foodCount;
    private int lives;
    private volatile boolean gameRunning;
    private RawConsoleInput rawInput;
    private StringBuilder screenBuffer;

    //A BlockingQueue is an interface within the java.util.concurrent package that extends Queue.
    //A BlockingQueue in Java is a thread-safe data structure that automatically handles flow control, while a LinkedBlockingQueue is a common, optionally-bounded implementation.
    //In this context, synchronization is the fundamental mechanism used internally by BlockingQueue implementations (like LinkedBlockingQueue) to ensure their thread safety.
    //Java uses explicit locks (specifically the ReentrantLock class and associated Condition objects) rather than traditional synchronized blocks for many concurrent utilities [1].
    //These locks manage access to the queue's internal state. When one thread is adding or removing an element, the lock prevents other threads from corrupting the data structure [1].
    //The blocking operations (put and take) use these locks and conditions to efficiently suspend and resume threads when the queue's state changes (e.g., from empty to not empty, or full to not full) [1].

    public World(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.maze = new char[rows][cols];
        this.pacman = new PacMan(1, 1);
        this.ghost1 = new Ghost(rows - 2, cols - 2);
        this.ghost2 = new Ghost(rows - 2, 1);
        this.foodCount = 0;
        this.lives = 5;
        this.gameRunning = true;
        this.rawInput = new RawConsoleInput();
        this.screenBuffer = new StringBuilder();
        initializeMaze();
    }

    // Getter methods for JLineGameConsole
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

    public Ghost getGhost1() {
        return ghost1;
    }

    public Ghost getGhost2() {
        return ghost2;
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
                else if ((j == 5 || j == 25) && i > 5 && i < 14) {
                    maze[i][j] = '#';
                }
                // Small vertical sections
                else if (i == 17 && j >= 3 && j <= 3) {
                    maze[i][j] = '#';
                }
                // Inner maze structures
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
                else if ((i == 13 && j == 9) || (j == 7 && i >= 9 && i <= 12)) {
                    maze[i][j] = '#';
                }
                else if (j == 13 && i > 5 && i <= 8) {
                    maze[i][j] = '#';
                }
                else if (i == 8 && j > 13 && j <= 17) {
                    maze[i][j] = '#';
                }
                else if (j == 17 && i > 8 && i < 15) {
                    maze[i][j] = '#';
                }
                else if (i == 14 && j > 17 && j <= 22) {
                    maze[i][j] = '#';
                }
                else if (j == 19 && i > 8 && i <= 11) {
                    maze[i][j] = '#';
                }
                else if (i == 6 && j > 19 && j < 25) {
                    maze[i][j] = '#';
                }
                else if (i == 5 && j > 14 && j <= 18) {
                    maze[i][j] = '#';
                }
                // Food dots made in maze
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
        screenBuffer.setLength(0);  // Clear buffer
        
        // Build the entire frame in a buffer first
        //A screen buffer is like a "canvas" in memory where text and colors are written. The terminal/console reads from this buffer to display content on screen.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == pacman.getX() && j == pacman.getY()) {
                    screenBuffer.append("P1");
                } else if (i == ghost1.getX() && j == ghost1.getY()) {
                    screenBuffer.append("G1");
                } else if (i == ghost2.getX() && j == ghost2.getY()) {
                    screenBuffer.append("G2");
                } else {
                    screenBuffer.append(maze[i][j]).append(" ");
                }
            }
            screenBuffer.append("\n");
        }
        screenBuffer.append("\nScore: ").append(pacman.getScore())
                   .append(" | Lives: ").append(lives)
                   .append(" | Food Remaining: ").append(foodCount).append("\n");
        screenBuffer.append("Move (w/a/s/d) or q to quit - NO ENTER NEEDED!\n");
        
        // Clear screen and print entire buffer at once for smooth rendering
        ConsoleUtils.clearScreen();
        System.out.print(screenBuffer.toString());
        System.out.flush();
    }

    public boolean detectCollision() {
        return (pacman.getX() == ghost1.getX() && pacman.getY() == ghost1.getY()) ||
                (pacman.getX() == ghost2.getX() && pacman.getY() == ghost2.getY());
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
        ghost1.move(maze, rows, cols);
        ghost2.move(maze, rows, cols);
    }

    public synchronized boolean gameFinished() {
        return foodCount == 0 || lives == 0;
    }

    public synchronized void resetPositions() {
        lives--;
        if (lives > 0) {
            pacman.setX(1);
            pacman.setY(1);
            ghost1.setX(rows - 2);
            ghost1.setY(cols - 2);
            ghost2.setX(rows - 2);
            ghost2.setY(1);
            System.out.println("\nGhost caught PacMan! Remaining lives: " + lives);
            try {
                Thread.sleep(1500); // Pause to show message
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void play() {
        // Start the raw input listener (no Enter key needed!)
        rawInput.start();
        
        // Give the Swing window time to initialize
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Hide cursor for cleaner display
        ConsoleUtils.hideCursor();

        // Main game loop
        long lastGhostMove = System.currentTimeMillis();
        long ghostMoveInterval = 500; // Ghosts move every 500ms
        long lastRender = System.currentTimeMillis();
        long renderInterval = 150; // Render every 150ms (slightly slower for stability)

        displayBoard();

        while (!gameFinished()) {
            long currentTime = System.currentTimeMillis();

            // Process player input (non-blocking, no Enter needed!)
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

            // Move ghosts periodically
            if (currentTime - lastGhostMove >= ghostMoveInterval) {
                moveGhosts();
                lastGhostMove = currentTime;

                // Check collision after ghost movement
                if (detectCollision()) {
                    resetPositions();
                }
            }

            // Render periodically
            if (currentTime - lastRender >= renderInterval) {
                displayBoard();
                lastRender = currentTime;
            }

            // Small sleep to prevent CPU spinning
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