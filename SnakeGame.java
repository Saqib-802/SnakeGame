import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;

    private final ArrayList<Point> snakeBody = new ArrayList<>();
    private Point food;
    private boolean isMoving = false;
    private char direction = 'R';
    private boolean isGameOver = false;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snakeBody.clear();
        snakeBody.add(new Point(0, 0)); // Initial position of the snake
        spawnFood();
        isGameOver = false;
        direction = 'R';
        isMoving = true;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int y = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        food = new Point(x, y);
    }

    public void move() {
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.set(i, new Point(snakeBody.get(i - 1)));
        }

        switch (direction) {
            case 'U':
                snakeBody.get(0).y -= UNIT_SIZE;
                break;
            case 'D':
                snakeBody.get(0).y += UNIT_SIZE;
                break;
            case 'L':
                snakeBody.get(0).x -= UNIT_SIZE;
                break;
            case 'R':
                snakeBody.get(0).x += UNIT_SIZE;
                break;
        }
    }

    public void checkCollision() {
        // Check if the snake collides with itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeBody.get(0).equals(snakeBody.get(i))) {
                isGameOver = true;
            }
        }

        // Check if the snake collides with the walls
        if (snakeBody.get(0).x < 0 || snakeBody.get(0).x >= WIDTH || snakeBody.get(0).y < 0 || snakeBody.get(0).y >= HEIGHT) {
            isGameOver = true;
        }

        // Check if the snake eats the food
        if (snakeBody.get(0).equals(food)) {
            snakeBody.add(new Point(-1, -1)); // Add a dummy point
            spawnFood();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isMoving && !isGameOver) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (!isGameOver) {
            // Draw the snake
            for (Point point : snakeBody) {
                g.setColor(Color.green);
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw the food
            g.setColor(Color.red);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);
        } else {
            // Game over message
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L';
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R';
            } else if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U';
            } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D';
            }

            if (!isMoving) {
                isMoving = true;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
