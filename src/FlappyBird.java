import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// TO STORE ALL THE PIPES
import java.util.ArrayList;

// TO PLACE PIPES AT RANDOM POSITION
import java.util.Random;



public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardwidth = 360;
    int boardheight = 640;

    // IMAGES
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird
    int birdX = boardwidth / 8;
    int birdY = boardheight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    // Bird Class
    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        // CREATE A CONSTRUCTOR
        Bird(Image img){
            this.img = img;

        }

    }

    // Pipes
    int pipeX = boardwidth; // RIGHT SIDE OF THE BOARD
    int pipeY = 0; // TOP OF THE BOARD
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; // CHECK WHETHER THE FLAPPY BIRD PASSED THE PIPES

        Pipe(Image img){
            this.img = img;
        }


    }

    // GAME
    Bird bird;
    int velocityX = -4; // MOVE THE PIPES TO THE LEFT 
    int velocityY = 0; // MOVE UP/DOWN
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();


    // CREATE A TIMER
    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false; // UNLESS THE BIRD FALLS OFF OR COLLIDES WITH ONE OF THE PIPES
    double score = 0;


    // CREATE A CONSTRUCTOR
    FlappyBird(){
        setPreferredSize(new Dimension(boardwidth, boardheight));

        setFocusable(true);
        addKeyListener(this);
        

        // LOAD IMAGES
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // CREATE BIRD OBJECT
        bird = new Bird(birdImg);

        // ARRAY LIST FOR THE PIPES
        pipes = new ArrayList<Pipe>();


        // CREATE PLACE PIPES TIMER
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                    placePipes();
            }
        });
        placePipesTimer.start();

        // TIMER OBJECT
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

        // FUNCTION TO DRAW THE IMAGE ON TO THE BACKGROUND
        public void paintComponent(Graphics g){
            // FUNCTION OF JPANEL
            super.paintComponent(g);
            draw(g);

        }

        public void draw(Graphics g){
            //System.out.println("Draw");
            
            // DRAW BACKGROUND IMAGE
            g.drawImage(backgroundImg, 0, 0, boardwidth, boardheight, null);

            // DRAW THE BIRD
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

            // DRAW PIPES
            for (int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }

            // score 
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            if(gameOver)
            {
                g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
            }

            else
            {
                g.drawString(String.valueOf((int) score), 10, 35);
            }

        }

        // BIRD MOVEMENT
        public void move(){
            velocityY += gravity;
            bird.y += velocityY;
            bird.y = Math.max(bird.y, 0);

            //PIPES
            for (int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                pipe.x += velocityX;
                
                // IF THE BIRD HAS NOT PASSED THIS PIPE & X POSITION IS PAST THE RIGHT SIDE OF PIPE
                if(!pipe.passed && bird.x > pipe.x + pipe.width) 
                {
                    pipe.passed = true;
                    score += 0.5; // 0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                }

                if (collision(bird, pipe)){ 
                    gameOver = true;
                }
            }
            // IF THE BIRD FALLS
            if (bird.y > boardheight){
                gameOver = true;
            }

        }


        // PLACING PIPES
        public void placePipes(){
            // RANDOMLY ASSIGN THE PIPES
            // pipeY (0) - pipeHeight / 4 (128) --->
            // (0-1) * pipeHeight / 2 -> (256) (0-256)
            // 1/4 pipeHight -> 3/4 pipeHeight
            int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight/2));

            // OPENING SPACE BETWEEN TOPPIPE AND BOTTOMPIPE
            int openingSpace = boardheight / 4;


            Pipe topPipe = new Pipe(topPipeImg);
            topPipe.y = randomPipeY;
            pipes.add(topPipe);

            Pipe bottomPipe = new Pipe(bottomPipeImg);
            bottomPipe.y = topPipe.y +  + pipeHeight + openingSpace;
            pipes.add(bottomPipe);

        }
        // COLLISION OF BIRD AND PIPE
        public boolean collision(Bird a, Pipe b){
            return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
            a.x + a.width > b.x && // a's top right corner passes b's top left corner
            a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
            a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            move();
            // SCHEDULE A CALL TO paintComponent
            repaint();
            if (gameOver){
                placePipesTimer.stop();
                gameLoop.stop();
            }
        }

        

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                velocityY = -9;
                if(gameOver)
                {
                    // reset the conditions and restart the game
                    bird.y = birdY;
                    velocityY = 0;
                    pipes.clear();
                    score = 0;
                    gameOver = false;
                    gameLoop.start();
                    placePipesTimer.start();

                }
            }
       
        }

        @Override
        public void keyTyped(KeyEvent e) {
           
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }


    
    }