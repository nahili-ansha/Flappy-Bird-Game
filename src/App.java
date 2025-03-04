import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // creating a window
        int boardwidth = 360;
        int boardheight = 640;

        JFrame frame = new JFrame("Flappy Bird");
    
        // SET THE SIZE
        frame.setSize(boardwidth, boardheight);

        // PLACE AT THE CENTER 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // closing the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CREATE AN  INSTANCE OF FlappyBird
        FlappyBird flappybird = new FlappyBird();
        
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);

        
    }
}
