import javax.swing.*;
import java.awt.*; //abstract window toolkit
import java.awt.event.*;
import java.util.concurrent.ConcurrentLinkedQueue; //allows multiple programs to exectue simultaneously

// Provides raw keyboard input using a small Swing window.
// Keys are captured instantly without needing Enter.

public class RawConsoleInput {
    private volatile boolean running = true; //changes to this are immediatly visible to all threads, hence volatile
    private ConcurrentLinkedQueue<Character> keyQueue = new ConcurrentLinkedQueue<>();
    private JFrame frame; //used to create main window of program
    

     //Starts the input listener with a small key capture window.

    public void start() {
        running = true;
        
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("PacMan Controls - Keep this focused!");
            frame.setSize(350, 100);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            frame.setResizable(false);
            
            // Center label
            JLabel label = new JLabel("<html><center>WASD to move, Q to quit<br>Keep this window focused!</center></html>", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            frame.add(label);
            
            // Key listener for capturing input
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    char key = Character.toLowerCase(e.getKeyChar());
                    if (key == 'w' || key == 'a' || key == 's' || key == 'd' || key == 'q') {
                        keyQueue.offer(key);
                    }
                }
            });
            
            // Handle window close
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    keyQueue.offer('q');  // Treat close as quit
                }
            });
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.requestFocus();
        });
    }

     // Gets the next key pressed and removes it from queue.
     // return The key character, or 0 if no key was pressed.

    public char getKey() {
        Character key = keyQueue.poll();
        return key != null ? key : 0;
    }
    

    // Checks if a key is available.

    public boolean hasKey() {
        return !keyQueue.isEmpty();
    }
    

     // Stops the input listener and closes the window.

    public void stop() {
        running = false;
        if (frame != null) {
            SwingUtilities.invokeLater(() -> {
                frame.dispose();
            });
        }
    }
}
