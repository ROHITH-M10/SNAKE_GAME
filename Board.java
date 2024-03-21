package snake_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import javax.swing.border.LineBorder;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 700;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 500;
    private  int DELAY = 80;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int danger_x;
    private int danger_y;
    private int score = 0;
    private int count = 0;
    private int bonus_x;
    private int bonus_y;
    

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image danger;
    private Image bonus;
    private Image headpower;
    private Image dotpower;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setBorder(new LineBorder(Color.RED, 2));
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
        
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/snake_game/dot.png");
        ball = iid.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);

        ImageIcon iia = new ImageIcon("src/snake_game/apple.png");
        apple = iia.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);

        ImageIcon iih = new ImageIcon("src/snake_game/head.png");
        head = iih.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        
        ImageIcon iidanger = new ImageIcon("src/snake_game/danger.png");
        danger = iidanger.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        
        ImageIcon iibonus = new ImageIcon("src/snake_game/bonus.png");
        bonus = iibonus.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        
        ImageIcon iihpower = new ImageIcon("src/snake_game/headpower.png");
        headpower = iihpower.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        
        ImageIcon iidpower = new ImageIcon("src/snake_game/dotpower.png");
        dotpower = iidpower.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
    } 

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateDanger();        
        locateApple();
        

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {
        	
        	

            if(count%5==0 && count>0) {
            	g.drawImage(bonus, bonus_x, bonus_y, this);
            }
            else {
            	g.drawImage(apple, apple_x, apple_y, this);
            }
            g.drawImage(danger, danger_x, danger_y, this);
            
         
            if(count%3==0 && count >0) {
            	for (int z = 0; z < dots; z++) {
                	
                    if (z == 0) {
                        g.drawImage(headpower, x[z], y[z], this);
                        
                    } else {
                        g.drawImage(dotpower, x[z], y[z], this);
                    }
                }
       		
            }
            
            else {
            	for (int z = 0; z < dots; z++) {
                	
                    if (z == 0) {
                        g.drawImage(head, x[z], y[z], this);
                        
                    } else {
                        g.drawImage(ball, x[z], y[z], this);
                    }
                }
            }
            

            Toolkit.getDefaultToolkit().sync();
            

        } 
        else {
            gameOver(g);
        }    
        
    }
    
    private void gameOver(Graphics g) {
        
        String msg = "Game Over ";
        String msg2 = "Score : "+score;
        String msg3 = "(Press enter to restart)";
        Font small = new Font("Ink Free", Font.BOLD, 30);
        FontMetrics metr = getFontMetrics(small);
       

        g.setColor(Color.yellow);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT/3 );
        g.drawString(msg3, (B_WIDTH - metr.stringWidth(msg3))/2, B_HEIGHT -80 );
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
        	count++;
            dots++;
            score++;
            
            if (DELAY > 30) {
                DELAY = DELAY - 10;
                timer.setDelay(DELAY);
            }

            if(count%5==0 && count >0) {
            	locateBonus();
            }
            else {
            	locateApple();
            }
            
            locateDanger();
            
        }
    }
    
    private void checkBonus() {

        if ((x[0] == bonus_x) && (y[0] == bonus_y)) {
        	count++;
            dots++;
            score = score + 5;
            locateApple();
            locateDanger();
            
        }
    }
    
    private void checkDanger() {
    	
    	if(count%3==0 && count >0) {
    		
    		inGame = true;
    	}
    	
    	else if ((x[0] == danger_x) && (y[0] == danger_y)) {

                inGame = false;
            
    	}
    }

    private void move() {
    	    	

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }
    
    

    private void locateApple() {
        int r = (int) (Math.random() * (B_WIDTH / DOT_SIZE - 2)) + 1;
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * (B_HEIGHT / DOT_SIZE - 2)) + 1;
        apple_y = r * DOT_SIZE;
    }

    private void locateDanger() {
        int r = (int) (Math.random() * (B_WIDTH / DOT_SIZE - 2)) + 1;
        danger_x = r * DOT_SIZE;
        r = (int) (Math.random() * (B_HEIGHT / DOT_SIZE - 2)) + 1;
        danger_y = r * DOT_SIZE;
    }

    private void locateBonus() {
        int r = (int) (Math.random() * (B_WIDTH / DOT_SIZE - 2)) + 1;
        bonus_x = r * DOT_SIZE;
        r = (int) (Math.random() * (B_HEIGHT / DOT_SIZE - 2)) + 1;
        bonus_y = r * DOT_SIZE;
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            if(count%5==0 && count>0) {
            	checkBonus();
            }
            else {
            	checkApple();
            }
            checkDanger();
            checkCollision();
            move();
        }

        repaint();
    }
    
    //////////////////////
    

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_W) {
            	key = KeyEvent.VK_UP;
            }
            
            if (key == KeyEvent.VK_S) {
            	key = KeyEvent.VK_DOWN;
            }
            if (key == KeyEvent.VK_D) {
            	key = KeyEvent.VK_RIGHT;
            }
            if (key == KeyEvent.VK_A) {
            	key = KeyEvent.VK_LEFT;
            }
            
             
            // sapcebar
            if (key == KeyEvent.VK_SPACE) {
            	timer.stop();
            }
            
            if (key == KeyEvent.VK_X ) {
            	timer.start();
            	inGame = false;  
            	
            }
            
            if (key ==KeyEvent.VK_ENTER) {
            	inGame = false;
                JFrame ex = new Snake();
                ex.setVisible(true);
  
            }
            
            
            
            // resume
            if ((key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_RIGHT) || (key == KeyEvent.VK_UP) || (key == KeyEvent.VK_DOWN)) {
            	timer.start();
            }
            
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}