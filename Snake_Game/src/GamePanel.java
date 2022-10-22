import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    private final int GAME_AREA_WIDTH = 850, GAME_AREA_HEIGHT = 575;
    private final int GAME_AREA_X = 25, GAME_AREA_Y = 75;
    private final int UNIT = 25;
    private final int HORIZONTAL_GRIDS = GAME_AREA_WIDTH/UNIT;
    private final int VERTICAL_GRIDS = GAME_AREA_HEIGHT/UNIT;
    private final int TOTAL_GRIDS = HORIZONTAL_GRIDS * VERTICAL_GRIDS;

    private final String LEFT = "left", RIGHT = "right", UP = "up", DOWN = "down";
    private int[] snakeXposition = new int[TOTAL_GRIDS];
    private int[] snakeYposition = new int[TOTAL_GRIDS];

    private final int[] xpos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    private final int[] ypos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};
    private final Random random;

    private boolean left=false;
    private boolean right=true;
    private boolean up=false;
    private boolean down=false;
    private boolean needFood = true;
    private boolean gameOver = false;
    private boolean isFirstMove = true;
    private int snakeLength = 3;
    private int score;
    private int food_x;
    private int food_y;

    GamePanel() {
        addKeyListener(this);
        setFocusable(true);
        random = new Random();
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if(!gameOver) {
            drawTitleArea(graphics);
            drawGameArea(graphics);
            drawScore(graphics);
            drawFood(graphics);
            drawSnake(graphics);
            checkCaughtFood();
            checkCollidesWithBody();
            repaint();
        }
        else {
            drawGameOver(graphics);
        }
    }

    private void drawScore(Graphics graphics) {
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        graphics.setColor(Color.WHITE);
        graphics.drawString("SCORE "+ score, 400, 90);
    }

    private void checkCollidesWithBody() {
        //check head hitting any of the body coordinate
        int headX = snakeXposition[0];
        int headY = snakeYposition[0];
        for(int i = snakeLength - 1; i > 0; i--) {
            if(headX == snakeXposition[i] && headY == snakeYposition[i])
                gameOver = true;
        }
    }

    private void checkCaughtFood() {
        //if head hits food
        if(snakeXposition[0] == food_x && snakeYposition[0] == food_y) {
            score++;
            snakeLength++;
            needFood = true;
        }
    }

    private void drawGameOver(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        graphics.drawString("Game Over", 350,250);
        graphics.setColor(Color.ORANGE);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,25));
        graphics.drawString("Press Space to start",320,350);
        graphics.setColor(Color.WHITE);
        graphics.drawString("YOUR SCORE " + score, 340,320);
    }

    private void drawSnake(Graphics graphics) {

        setSnakePositon();

        //drawing snake
        for(int i = 0; i < snakeLength; i++) {
            if(i == 0) //head
                graphics.setColor(Color.RED);
            else //body
                graphics.setColor(Color.ORANGE);

            graphics.fillOval(snakeXposition[i], snakeYposition[i], UNIT,UNIT);
        }

        try {
            Thread.sleep(250);
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restart() {
        gameOver = false;
        score = 0;
        setDirection(RIGHT);
        isFirstMove = true;
        snakeLength = 3;
        repaint();
    }
    private void setSnakePositon() {
        if(isFirstMove) {
            snakeXposition[0] = 200;//head
            snakeXposition[1] = 175;
            snakeXposition[2] = 150;

            snakeYposition[0] = 150;//head
            snakeYposition[1] = 150;
            snakeYposition[2] = 150;
            isFirstMove = false;
        }
        else {
            //updating head first --> looses previous position of head
            updateBodyPosition();
            updateHeadPosition();
        }
    }


    private void updateBodyPosition() {
        for(int i = snakeLength; i > 0; i--) {
            snakeXposition[i] = snakeXposition[i-1];
            snakeYposition[i] = snakeYposition[i-1];
        }
    }

    private void updateHeadPosition() {
        if(left && !right)
            snakeXposition[0] -= UNIT;
        else if(right && !left)
            snakeXposition[0] += UNIT;
        else if (up && !down)
            snakeYposition[0] -= UNIT;
        else if(down && !up)
            snakeYposition[0] += UNIT;

        //if snake goes out of game area
        if(snakeXposition[0] > GAME_AREA_WIDTH)
            snakeXposition[0] = GAME_AREA_X;
        if(snakeXposition[0] < GAME_AREA_X)
            snakeXposition[0] = GAME_AREA_WIDTH;
        if(snakeYposition[0] > 625)
            snakeYposition[0] = 75;
        if(snakeYposition[0] < 75)
            snakeYposition[0] = 625;
    }

    private void drawFood(Graphics graphics) {
        if(needFood)
            setFoodLocation();
        graphics.setColor(Color.YELLOW);
        graphics.fillOval(food_x, food_y, UNIT, UNIT);
    }

    private void setFoodLocation() {
        food_x = xpos[random.nextInt(34)];
        food_y = ypos[random.nextInt(23)];
        needFood = false;
    }

    private void drawGameArea(Graphics graphics) {
        graphics.setColor(Color.BLUE);
        graphics.drawRect(GAME_AREA_X-1, GAME_AREA_Y-1, GAME_AREA_WIDTH +1, GAME_AREA_HEIGHT +1);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_WIDTH, GAME_AREA_HEIGHT);
    }

    private void drawTitleArea(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.drawRect(24, 10, GAME_AREA_WIDTH +1, 55);
    }

    private void setDirection(String direction) {
        left = false;
        right = false;
        up = false;
        down = false;

        switch (direction) {
            case LEFT -> left = true;
            case RIGHT -> right = true;
            case UP -> up = true;
            case DOWN -> down = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_SPACE && gameOver)
            restart();

        else if (keyCode == KeyEvent.VK_LEFT)
            setDirection(LEFT);

        else if(keyCode == KeyEvent.VK_RIGHT)
            setDirection(RIGHT);

        else if(keyCode == KeyEvent.VK_UP)
            setDirection(UP);

        else if(keyCode == KeyEvent.VK_DOWN) {
            setDirection(DOWN);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
