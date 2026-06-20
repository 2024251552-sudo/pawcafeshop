// Main.java - Entry point
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PawCafeLogin().setVisible(true);
        });
    }
}