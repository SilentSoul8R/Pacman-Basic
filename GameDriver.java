public class GameDriver {
    public static void main(String[] args) {
        // Enable ANSI support
        ConsoleUtils.enableAnsiSupport();
        // to allow the program to support ansi streams, cmd exe doesnt support it natively

        System.out.println("=== Starting PacMan Game ===");
        System.out.println("Controls: w=up, s=down, a=left, d=right, q=quit");
        System.out.println("NO ENTER KEY NEEDED - Just press the keys!");
        System.out.println("\nStarting in 2 seconds...");
        // stops current executnion for 2 seconds, in timed waiting state, can be interrupted
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt(); //checked exception, must be caught, stored, sets interrupt flag
        }

        World world = new World(20, 30);
        world.play();

        // run on terminal using java -cp out GameDriver
    }
}