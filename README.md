# Console PacMan (Java)

A terminal-based PacMan clone written in Java. The game renders the maze, PacMan, and two ghosts directly in the console, with real-time keyboard input captured through a small Swing helper window (so no Enter key is required).

## Files

| File | Purpose |
|---|---|
| `GameDriver.java` | Entry point. Prints intro text, then creates and starts the `World`. |
| `World.java` | Core game engine: builds the maze, tracks score/lives, runs the game loop, handles movement, collisions, and rendering. |
| `Entity.java` | Abstract base class for anything with a position (`x`, `y`) on the board. Defines `move`, `getSymbol`, `isAt`, and `collidesWith`. |
| `PacMan.java` | Player entity. Extends `Entity`; tracks score and moves based on `w/a/s/d` input. |
| `Ghost.java` | Enemy entity. Extends `Entity`; moves randomly each tick, blocked by walls. |
| `ConsoleUtils.java` | ANSI escape-code helpers for clearing the screen and hiding/showing the cursor. |
| `RawConsoleInput.java` | Captures keyboard input instantly (no Enter needed) via a focused Swing `JFrame` and a thread-safe key queue. |

## How to Compile and Run

```bash
# Compile all source files into an output directory
javac -d out *.java

# Run the game
java -cp out GameDriver
```

When the game starts, a small **"PacMan Controls"** window will pop up — keep that window focused, since it's what captures your keystrokes.

## Controls

| Key | Action |
|---|---|
| `w` | Move up |
| `s` | Move down |
| `a` | Move left |
| `d` | Move right |
| `q` | Quit the game |

## Gameplay

- PacMan (`P1`) starts at the top-left of the maze and must eat all the dots (`.`) to win.
- Two ghosts (`G1`, `G2`) move randomly around the maze every 600ms.
- Colliding with a ghost costs PacMan a life and resets all entities to their starting positions.
- The game ends when all food is eaten (**win**) or PacMan runs out of lives (**game over**).
- The board re-renders roughly every 150ms for smooth animation.

## Design Notes

- `Entity` is an abstract class enabling polymorphism: `World` manages ghosts and PacMan through a common interface for collision detection and rendering.
- `World.movePacman` and `World.moveGhosts` are `synchronized` to keep game state consistent, since input is handled on a separate Swing event thread while the main loop runs concurrently.
- `RawConsoleInput` uses a `ConcurrentLinkedQueue<Character>` so the Swing key listener thread and the main game loop can safely exchange key presses without explicit locking.
