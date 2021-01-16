import javax.swing.JFrame;

public class Main {

    public Main() {

        // Create JJFrame and Window
        JFrame frame = new JFrame();
        Window game = new Window();

        // Add the game to the frame
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake");

        // Set the frame
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    public static void main(String[] args) {

        // Load the main
        new Main();

    }

}
