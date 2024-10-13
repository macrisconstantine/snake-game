import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    int delay = 70;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    boolean text = true;
    JButton easy;
    JButton medium;
    JButton hard;
    JButton insane;

    GamePanel(){
        random = new Random();
        this.setPreferredSize((new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)));
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        easy = new JButton("Easy");
        medium = new JButton("Medium");
        hard = new JButton("Hard");
        insane = new JButton("INSANE");
        this.add(easy);
        this.add(medium);
        this.add(hard);
        this.add(insane);
        easy.addActionListener(e -> {
            delay = 80;
            text = false;
            running = true;
            startGame();
        });
        medium.addActionListener(e -> {
            delay = 70;
            text = false;
            running = true;
            startGame();
        });
        hard.addActionListener(e -> {
            delay = 45;
            text = false;
            running = true;
            startGame();
        });
        insane.addActionListener(e -> {
            delay = 25;
            text = false;
            running = true;
            startGame();
        });
    }

    public void startGame() {
        easy.setVisible(false);
        medium.setVisible(false);
        hard.setVisible(false);
        insane.setVisible(false);
        text = false;
        newApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
/**
     for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
     g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
     g.drawLine(SCREEN_WIDTH, i * UNIT_SIZE, 0, i * UNIT_SIZE);
     }
 */
    public void draw(Graphics g) {

        if(running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(4,30,4));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(30,150,10));
                    //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //displays current score during game
            g.setColor(Color.BLACK);
            g.setFont(new Font("DialogInput", Font.PLAIN, 30));
            g.drawString("Length:"+applesEaten, 20, SCREEN_HEIGHT - g.getFont().getSize());

        }
        if (text) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("DialogInput", Font.PLAIN, 80));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Snakey Boi", (SCREEN_WIDTH - metrics.stringWidth("Snakey Boi"))/2, SCREEN_HEIGHT/2);
        }
        if (!running && !text) {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move() {
        for(int i=bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }
    public void checkApple() {
        if((x[0]==appleX) && (y[0]==appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i=bodyParts;i>0;i--) {
            if((x[0]==x[i]) && (y[0]==y[i])) {
                running = false;
                break;
            }
        }
        //checks if head touches left border
        if(x[0] < 0) {
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
            // running = false;
        }
        //checks if head touches right border
        if(x[0] > SCREEN_WIDTH-UNIT_SIZE) {
            x[0] = 0;
            //running = false;
        }
        //checks if head touches top border
        if(y[0] < 0) {
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
            //running = false;
        }
        //checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT-UNIT_SIZE) {
            y[0] = 0;
            // running = false;
        }
        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        //Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("monospaced", Font.BOLD, 70));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        //score
        g.setColor(Color.black);
        g.setFont(new Font("monospaced", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("SCORE:"+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE:"+applesEaten))/2,360);
        //prompt to restart game
        g.setColor(Color.black);
        g.setFont(new Font("monospaced", Font.PLAIN, 25));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("[press <enter> to play again]", (SCREEN_WIDTH - metrics3.stringWidth("[press <enter> to play again]"))/2,405);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    //if(direction != 'R') {
                        direction = 'L';
                    //}
                    break;
                case KeyEvent.VK_RIGHT:
                    //if(direction != 'L') {
                        direction = 'R';
                    //}
                    break;
                case KeyEvent.VK_UP:
                    //if(direction != 'D') {
                        direction = 'U';
                    //}
                    break;
                case KeyEvent.VK_DOWN: direction = 'D';
                    //if(direction != 'U') {

                    //}
                    break;
                case KeyEvent.VK_ENTER:
                    if(!running) {
                        x[0] = 0;
                        y[0] = 0;
                        direction = 'R';
                        repaint();
                        applesEaten = 0;
                        bodyParts = 6;
                        startGame();
                    }
            }
        }
    }
}
