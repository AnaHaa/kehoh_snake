import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Window extends JPanel implements Runnable, KeyListener {

    // Set the window for commands etc, 512x512 is set to 16x16 grid, with block size 32
    public int WIDTH = 512, HEIGHT = 512;
    private Thread thread;
    private boolean run, right = true, left = false, up = false, down = false, wait = false, stopped;

    // Set the snake
    private ArrayList<Snake> snake;

    // Set the item component and the item array for item sorting
    private Item item;
    private ArrayList<Item> items;

    // Random for the item coordinates
    private Random rand;

    // Snake starting values
    private int x = 3, y = 3, size = 4, tick = 0;

    public Window() {
        // Set the window from the variables and add eventlistener
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        snake = new ArrayList<Snake>();
        items = new ArrayList<Item>();
        rand = new Random();
        // Start the game
        start();
    }
    // Function to start the game from thread
    public void start() {
        run = true;
        stopped = false;
        thread = new Thread(this);
        thread.start();
    }

    // Function to stop the game from thread
    public void stop() {
        run = false;
        try {
            thread.join();
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
    }

    // The time function used to forward the game-time
    public void time() {
        // Create the snake from the variables, with block size 32
        Snake snakebit;
        if (snake.size() == 0) {
            snakebit = new Snake(x, y, 32);
            snake.add(snakebit);
        }
        // Forward the time
        tick++;
        // Every 0.5 s move the snake using the coordinates
        if (tick > 5000000) {
            if (right) x++;
            if (left) x--;
            if (up) y--;
            if (down) y++;
            // Reset the tick to 0 to use the function again and set wait to false for keypress
            tick = 0;
            wait = false;
            // Draw a snakebit
            snakebit = new Snake(x, y, 32);
            // Add to the snake the bit
            snake.add(snakebit);
            // Used to control the snake size, cant be over than the set value
            if (snake.size() > size) {
                snake.remove(0);
            }
        }
        // Create a new item for snake, can only be between 0, because 1 item is allowed on the grid
        if (items.size() == 0) {
            // Coordinates are 16, 16, block size 32
            int itemX = rand.nextInt(16);
            int itemY = rand.nextInt(16);
            // check if the snake is in the item, if so, create again and check again
            outer:
                for (int i = 0; i < snake.size(); i++) {
                    // For every snake bit, check if item is in it
                    if (itemX == snake.get(i).getX() && itemY == snake.get(i).getY()) {
                        itemX = rand.nextInt(16);
                        itemY = rand.nextInt(16);
                        break outer;
                    }
                }
            // Create the item using the values
            item = new Item(itemX, itemY, 32);
            // Add to the item array
            items.add(item);
        }
        // Check if snake is in the item coordinates in the item array (0)
        if(x == items.get(0).getX() && y == items.get(0).getY()) {
            size++;
            items.remove(0);
        }
        // Check if the snake is touching itself, if so, game over
        for (int i = 0; i < snake.size(); i++) {
            // For every snake bit, check if head is in it
            if (x == snake.get(i).getX() && y == snake.get(i).getY()) {
                if (i != snake.size() - 1) {
                    stopped = true;
                }
            }
        }
        // Check if the snake is touching border, if so, game over
        if (x < 0 || x > 15 || y < 0 || y > 15) {
            stopped = true;
        }
        // Check if the snake is size 16, if so, win => used for testing etc
        if (snake.size() == 16) {
            stopped = true;
        }
    }

    // Function to paint the graphics
    public void paint(Graphics bit) {
        // Set block color
        bit.setColor(Color.BLACK);
        bit.fillRect(0, 0, WIDTH, HEIGHT);
        // Set border color
        bit.setColor(Color.green);
        bit.drawRect(0, 0, WIDTH, HEIGHT);
        // Draw the 16x16 grid
        for (int loopDraw = 0; loopDraw < 16; loopDraw++) {
            bit.drawLine(loopDraw * 32, 0, loopDraw * 32, HEIGHT);
            bit.drawLine(0, loopDraw * 32, HEIGHT, loopDraw * 32);
        }
        // Draw the snake
        for (int i = 0; i < snake.size(); i++) {
            snake.get(i).draw(bit);
        }
        // Draw the item
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(bit);
        }
        // Used to stop the game, painted last
        if (stopped) {
            gameOver(bit);
            stop();
        }
    }

    // The game over function
    public void gameOver(Graphics g) {
        g.setColor(Color.green);
        g.setFont( new Font("Bauhaus 93", Font.BOLD, 80));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    // Run the game
    @Override
    public void run() {
        // While the thing is running
        while(run) {
            // Do time and paint the game
            time();
            // Re-do the paint
            repaint();
        }
    }

    // Capture the key
    @Override
    public void keyPressed(KeyEvent e) {
        // Get the key pressed
        int key = e.getKeyCode();

        // If the key is right arrow, set right boolean true and other false etc
        // Snake cannot go from up to down and so on -> would kill the snake
        // Wait is used to control the snake, so it cannot overlap itself, aka left right up in less than 0.5 s
        if (wait == false) {
            if(key == KeyEvent.VK_RIGHT && !left) {
                wait = true;
                right = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_LEFT && !right) {
                wait = true;
                left = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_UP && !down) {
                wait = true;
                up = true;
                right = false;
                left = false;
            } else if (key == KeyEvent.VK_DOWN && !up) {
                wait = true;
                down = true;
                right = false;
                left = false;
            }
        }
    }

    // Do nothing, used to override
    @Override
    public void keyReleased(KeyEvent e) { }

    // Do nothing, used to override
    @Override
    public void keyTyped(KeyEvent e) { }
}
