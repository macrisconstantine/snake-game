import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class youtubecomment extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static int DELAY = 75;
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

    youtubecomment(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        easy = new JButton("Easy");
        medium = new JButton("Medium");
        hard = new JButton("Hard");
        insane = new JButton("Insane");
        this.add(easy);
        this.add(medium);
        this.add(hard);
        this.add(insane);
        easy.addActionListener(e -> {
            DELAY = 100;
            text = false;
            running = true;
            startGame();
            easy.hide();
            medium.hide();
            hard.hide();
            insane.hide();
        });
        medium.addActionListener(e -> {
            DELAY = 75;
            text = false;
            running = true;
            startGame();
            easy.hide();
            medium.hide();
            hard.hide();
            insane.hide();

        });
        hard.addActionListener(e -> {
            DELAY = 50;
            text = false;
            running = true;
            startGame();
            easy.hide();
            medium.hide();
            hard.hide();
            insane.hide();


        });
        insane.addActionListener(e -> {
            DELAY = 25;
            text = false;
            running = true;
            startGame();
            easy.hide();
            medium.hide();
            hard.hide();
            insane.hide();
        });

    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        direction = 'R';
        bodyParts = 6;
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void  draw(Graphics g) {

        if(running) {
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0; i<bodyParts;i++) {
                if(i==0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }else {
                    g.setColor(new Color(36,70,10));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Courier",Font.PLAIN, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize()/2 +15);
        }else if(!text){
            gameOver(g);

        }
    }
    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

    }
    public void move() {
        for(int i = bodyParts;i>0;i--) {
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
        if((x[0]==appleX)&&(y[0]==appleY)) {
            bodyParts += 1;
            if(DELAY == 100) {
                applesEaten +=1;
            }else if (DELAY == 75) {
                applesEaten += 2;
            }else if(DELAY == 50) {
                applesEaten += 3;
            }else if(DELAY == 25) {
                applesEaten += 5;
            }
            newApple();
        }

    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts;i>0;i--) {
            if((x[0])==x[i]&&(y[0]==y[i])) {
                running = false;
            }
            //checks if head touches left border
            if(x[0]< 0) {
                x[0] = SCREEN_WIDTH - UNIT_SIZE;
            }
            //checks if head touches right border
            if(x[0]> SCREEN_WIDTH - UNIT_SIZE) {
                x[0] = 0;
            }
            //checks if head touches top border
            if(y[0]< 0) {
                y[0] = SCREEN_HEIGHT - UNIT_SIZE;
            }
            //checks if head touched bottom border
            if(y[0]> SCREEN_HEIGHT - UNIT_SIZE) {
                y[0] = 0;
            }
            if (!running&&!text) {
                timer.stop();
            }
        }


    }
    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize()/2 +10);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/3);
        g.setFont(new Font("Ink Free",Font.BOLD, 45));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Press Space to Restart", (SCREEN_WIDTH - metrics2.stringWidth("Press Space to Restart"))/2, SCREEN_HEIGHT/2);
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
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
            if(!running&&!text) {
                if(e.getKeyChar()==KeyEvent.VK_SPACE) {
                    startGame();
                    for(int i=bodyParts;i>0;i--) {
                        x[i] = bodyParts*-1;
                        y[i] = 0;
                    }
                    x[0] = 0;
                    y[0] = 0;
                    repaint();
                    applesEaten = 0;

                }

            }

        }
    }

}