import java.io.IOException;

//Console utilities for Windows terminal handling.
// Provides screen clearing and raw input mode using native Windows commands.


public class ConsoleUtils {
    
    private static boolean firstRender = true;


    // Clears the console screen and moves cursor to top-left.
    // Uses ANSI escape codes which work in Windows 10+ terminals.


    public static void clearScreen() {
        if (firstRender) {
            // Only do a full clear on first render

            System.out.print("\033[2J");
            firstRender = false;
        }
        // Move cursor to home position (top-left)
        System.out.print("\033[H");
        System.out.flush(); //immediate output, replacing screen
    }
    

     // Resets the first render flag (call when game restarts).

    public static void reset() {
        firstRender = true;
    }

    public static void hideCursor() {
        System.out.print("\033[?25l");
        System.out.flush();
    }

    public static void showCursor() {
        System.out.print("\033[?25h");
        System.out.flush();
    }

}
