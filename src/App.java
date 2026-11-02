import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("FlappyBird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout()); // Untuk layout menu di tengah

        // Spesifikasi 5: Main Menu
        JButton btnStart = new JButton("Start Game");
        JButton btnExit = new JButton("Exit");

        JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        menuPanel.add(btnStart);
        menuPanel.add(btnExit);
        frame.add(menuPanel);

        btnStart.addActionListener(e -> {
            frame.getContentPane().removeAll(); // Hapus menu
            frame.setLayout(new BorderLayout());

            Logic logika = new Logic();
            View tampilan = new View(logika);
            logika.setView(tampilan);

            // Spesifikasi 3: JLabel untuk skor
            JLabel scoreLabel = new JLabel("Score: 0");
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
            scoreLabel.setForeground(Color.BLACK); // Sesuaikan warna jika perlu
            tampilan.add(scoreLabel);
            logika.setScoreLabel(scoreLabel);

            frame.add(tampilan);
            frame.revalidate();
            frame.repaint();
            tampilan.requestFocusInWindow(); // Agar keyListener langsung aktif
        });

        btnExit.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}