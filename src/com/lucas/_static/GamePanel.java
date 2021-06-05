package com.lucas._static;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int x[] = new int [GAME_UNITS];
    final int y[] = new int [GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int DELAY = 75 ;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    static boolean gameOn = false;
    static boolean fail = false;




    GamePanel(){
        random = new Random();
        this.setPreferredSize( new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MykeyAdaper());
        startGame();
    }

    public void startGame(){
         newApple();
         running = true;
         fail= false;
         timer(DELAY);
    }

    public void timer (int d){
        timer = new Timer(d, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g){

        if (!fail) {
            //Draws a gird
            for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            // Draw the Apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Draw Snake Body
            for (int i = 0; i < bodyParts; i++) {
                //Head
                if (i == 0) {
                    g.setColor(Color.red);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                // Body
                else {
                    g.setColor(new Color(45, 180, 0));

                    //Rainbow snake
                    // g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                //Snake Color
                g.setColor(Color.green);
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + applesEaten ,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten ))/2, g.getFont().getSize());
            }
        }else {
            gameOver(g);
        }
    }

    //Generates a new Apple
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/ UNIT_SIZE)) * UNIT_SIZE;
    }


    // Moves the Snake
    public void move(){
        for (int i = bodyParts ; i > 0; i--){
            x[i] = x [i-1];
            y[i] = y [i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y [0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }


    }

    // Pause and resume
    public void pause() {
        GamePanel.gameOn = false;
        timer.stop();
    }

    public void resume() {
        GamePanel.gameOn = true;
        timer.start();
    }


    //Checks if the snake and the apple are in the same spot if that is the case adds a body-part to the snake and increases the score and the speed of the game
    public void checkApple(){

            if ((this.x[0] == this.appleX) && (this.y[0] == this.appleY)) {
                bodyParts++;
                applesEaten++;
                if (DELAY >= 20) {
                    DELAY = DELAY - 1;
                    timer.setDelay(DELAY);
                }
                newApple();
            }


    }
    // Collisions with walls
    public void checkCollisions(){

        for (int i = bodyParts; i > 0; i--){
            if (( x[0]== x[i]) && (y[0] == y[i])){
                fail = true;
            }
        }
        // left wall
        if(x[0] < 0){
            fail = true;
        }
        //right wall
        if (x[0] > SCREEN_WIDTH ){
            fail = true;
        }
        // top wall
        if(y[0] < 0){
            fail = true;
        }
        //bottom wall
        if (y[0] > SCREEN_HEIGHT ){
            fail = true;
        }
        //Stops the game after the collision
        if (fail){

            timer.stop();
        }

    }
    // Game over screen
    public void gameOver (Graphics g){

        //Game over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 3);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press Enter To Play Again", (SCREEN_WIDTH - metrics2.stringWidth("Press Enter To Play Again")) / 2, SCREEN_HEIGHT / 2);

        //SCORE
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics3.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());


   }

   public void resetGame(){
        startGame();
        repaint();
        bodyParts = 6;
        applesEaten = 0;
        DELAY = 75 ;
        direction = 'R';
        gameOn = false;
        fail = false;
        x[0] = 1;
        y[0] = 1;


   }

    @Override
    public void actionPerformed(ActionEvent e){
        if (!fail){
            checkApple();
            move();
            checkCollisions();
        }

        repaint();
    }


    //Controls for the game  movement, pause, resume and restart
    public class MykeyAdaper extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }break;
                case KeyEvent.VK_A:
                    if (direction != 'R'){
                        direction = 'L';
                    }break;
                case KeyEvent.VK_D:
                    if (direction != 'L'){
                        direction = 'R';
                    }break;
                case KeyEvent.VK_W:
                    if (direction != 'D'){
                        direction = 'U';
                    }break;
                case KeyEvent.VK_S:
                    if (direction != 'U'){
                        direction = 'D';
                    }break;
                case KeyEvent.VK_SPACE:
                    if(GamePanel.gameOn) {
                        pause();
                    } else {
                        resume();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (fail){
                        new GameFrame();

                    }
            }
        }
    }
}
