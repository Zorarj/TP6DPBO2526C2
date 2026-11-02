import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.sound.sampled.*; // Untuk Audio
import java.net.URL;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;
    int pipeVelocityX = -2;

    View view;
    Image birdImage;
    Player player;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes; // Diperbaiki dari 'pipe'

    // Status Game & Skor
    boolean gameOver = false;
    double score = 0; // double karena pipa atas dan bawah akan lewat bersamaan (masing-masing +0.5)
    JLabel scoreLabel;

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        // Diperbaiki load gambarnya
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipe();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        // Bonus: Mainkan BGM
        playAudio("assets/bgm.wav");
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void placePipe() {
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    // Fungsi deteksi tabrakan
    public boolean collision(Player a, Pipe b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
                a.getPosX() + a.getWidth() > b.getPosX() &&
                a.getPosY() < b.getPosY() + b.getHeight() &&
                a.getPosY() + a.getHeight() > b.getPosY();
    }

    public void move() {
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // Spesifikasi 4: Tambah skor jika melewati pipa
            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                score += 0.5; // Karena ada 2 pipa (atas & bawah) setiap pasang, totalnya +1
                if (scoreLabel != null) {
                    scoreLabel.setText("Score: " + (int) score);
                }
            }

            // Spesifikasi 1: Nabrak pipa = Game Over
            if (collision(player, pipe)) {
                gameOver = true;
            }
        }

        // Spesifikasi 1: Jatuh ke bawah = Game Over
        if (player.getPosY() + player.getHeight() >= frameHeight) {
            gameOver = true;
        }

        if (gameOver) {
            gameLoop.stop();
            pipesCooldown.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                player.setVelocityY(-10);
            }
        }

        // Spesifikasi 2: Restart jika game over dan menekan 'R'
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            player.setPosY(playerStartPosY);
            player.setVelocityY(0);
            pipes.clear();
            score = 0;
            if (scoreLabel != null) scoreLabel.setText("Score: 0");
            gameOver = false;
            gameLoop.start();
            pipesCooldown.start();
        }
    }

    public void keyReleased(KeyEvent e) {}

    // Bonus: Fungsi Audio
    public void playAudio(String filePath) {
        try {
            URL url = getClass().getResource(filePath);
            if (url != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Looping BGM
            }
        } catch (Exception e) {
            System.out.println("Audio tidak ditemukan atau error: " + e.getMessage());
        }
    }
}