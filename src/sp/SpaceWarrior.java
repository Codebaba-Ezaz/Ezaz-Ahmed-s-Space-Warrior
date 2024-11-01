package sp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceWarrior extends Application {
    private static final int WIDTH = 800, HEIGHT = 600, PLAYER_SIZE = 60, PLAYER_SPEED = 5,
            BULLET_SPEED = 1, ENEMY_SPEED = 1, MAX_ENEMIES = 5;
    private Image playerImage;
    private double playerX, playerY, mouseX;
    private boolean isShooting, isGameOver;
    private List<Image> enemyImages;
    private List<Enemy> enemies;
    private GraphicsContext gc;
    private List<Bullet> bullets;
    private int score;
    private int highScore;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        playerImage = new Image("file:C:\\Users\\Saem\\Desktop\\pl.png");
        enemyImages = new ArrayList<>();
        enemyImages.add(new Image("file:C:\\Users\\Saem\\Desktop\\e1.png"));
        enemyImages.add(new Image("file:C:\\Users\\Saem\\Desktop\\e2.png"));
        enemyImages.add(new Image("file:C:\\Users\\Saem\\Desktop\\e3.png"));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        initializeGame();
        canvas.setFocusTraversable(true);
        canvas.setOnMouseMoved(e -> mouseX = e.getX());
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) isShooting = true;
        });
        canvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY) isShooting = false;
        });
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Space Warrior");
        primaryStage.setScene(scene);
        primaryStage.show();
        readHighScore(); // Read the high score from the file
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isGameOver) update();
                draw();
            }
        }.start();
    }

    private void initializeGame() {
        playerX = (WIDTH - PLAYER_SIZE) / 2;
        playerY = HEIGHT - PLAYER_SIZE;
        isShooting = false;
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        score = 0;
        isGameOver = false;
    }

    private void update() {
        if (isShooting) bullets.add(new Bullet(playerX + PLAYER_SIZE / 2, playerY));
        playerX = mouseX - PLAYER_SIZE / 2;
        if (playerX < 0) playerX = 0;
        else if (playerX > WIDTH - PLAYER_SIZE) playerX = WIDTH - PLAYER_SIZE;
        if (enemies.size() < MAX_ENEMIES) {
            Image randomEnemyImage = enemyImages.get((int) (Math.random() * enemyImages.size()));
            double randomX = Math.random() * (WIDTH - PLAYER_SIZE);
            enemies.add(new Enemy(randomX, 0, randomEnemyImage));
        }
        for (Enemy enemy : enemies) enemy.update();
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.getY() > HEIGHT || !enemy.isAlive()) {
                enemyIterator.remove();
                if (!enemy.isAlive()) score++;
            }
        }
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && bullet.intersects(enemy.getX(), enemy.getY(), PLAYER_SIZE, PLAYER_SIZE)) {
                    bulletIterator.remove();
                    enemy.setAlive(false);
                }
            }
        }
        for (Enemy enemy : enemies) {
            if (playerX < enemy.getX() + PLAYER_SIZE && playerX + PLAYER_SIZE > enemy.getX()
                    && playerY < enemy.getY() + PLAYER_SIZE && playerY + PLAYER_SIZE > enemy.getY()) {
                isGameOver = true;
                break;
            }
        }
    }

    private void draw() {
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.drawImage(playerImage, playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) gc.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), PLAYER_SIZE, PLAYER_SIZE);
        }
        for (Bullet bullet : bullets) bullet.draw();
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillText("Score: " + score, 10, 20);
        gc.setFont(Font.font(20));
        gc.fillText("High Score: " + highScore, WIDTH - 150, 20); // Display high score
        if (isGameOver) {
            gc.fillText("Game Over - Score: " + score, WIDTH / 2 - 100, HEIGHT / 2);
            
           
            if (score > highScore) {
                highScore = score;
                saveHighScore(); 
            }
        }
    }

    private class Bullet {
        private double x, y;

        public Bullet(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            y -= BULLET_SPEED;
        }

        public void draw() {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillOval(x, y, 5, 5);
        }

        public boolean intersects(double enemyX, double enemyY, int enemyWidth, int enemyHeight) {
            return x < enemyX + enemyWidth && x + 5 > enemyX && y < enemyY + enemyHeight && y + 5 > enemyY;
        }
    }

    private class Enemy {
        private double x, y;
        private Image image;
        private boolean alive;

        public Enemy(double x, double y, Image image) {
            this.x = x;
            this.y = y;
            this.image = image;
            this.alive = true;
        }

        public void update() {
            y += ENEMY_SPEED;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Image getImage() {
            return image;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }
    }

    private void readHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScore = Integer.parseInt(line);
            }
        } catch (IOException e) {
        }
    }

    private void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
