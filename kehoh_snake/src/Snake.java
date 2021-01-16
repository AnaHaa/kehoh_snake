import java.awt.*;

public class Snake {

    // Variables for the snake component
    private int x, y, width, height;

    // The Snake component constructor
    public Snake(int x, int y, int tileSize) {
        this.x = x;
        this.y = y;
        width = tileSize;
        height = tileSize;
    }

    // Setter and Getter for the Snake coordinates
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // Draw the snake component using the bit
    public void draw(Graphics bit) {
        bit.setColor(Color.green);
        bit.fillRect(x * width, y * height, width, height);
    }

}
