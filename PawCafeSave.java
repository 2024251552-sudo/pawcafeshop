// PawCafeSave.java - Save to File Page
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PawCafeSave extends JFrame {
    private String cashierName;
    private ArrayList<OrderItem> orderItems;
    private double total, discount, finalTotal;
    private int totalItems;
    
    public PawCafeSave(String cashierName, ArrayList<OrderItem> orderItems, 
                      double total, double discount, double finalTotal, int totalItems) {
        this.cashierName = cashierName;
        this.orderItems = orderItems;
        this.total = total;
        this.discount = discount;
        this.finalTotal = finalTotal;
        this.totalItems = totalItems;
        
        setTitle("Paw Café - Save File");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeUI();
        saveToFile();
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        mainPanel.setBackground(new Color(255, 248, 240));
        
        // Success icon
        JLabel iconLabel = new JLabel("✅", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        
        // Message
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("SAVED SUCCESSFULLY!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 150, 0));
        
        JLabel messageLabel = new JLabel("Data has been saved to receipt.txt", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(80, 50, 30));
        
        messagePanel.add(titleLabel, BorderLayout.CENTER);
        messagePanel.add(messageLabel, BorderLayout.SOUTH);
        
        // Receipt preview
        JTextArea previewArea = new JTextArea(10, 30);
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        previewArea.setBackground(new Color(255, 255, 255));
        previewArea.setBorder(BorderFactory.createLineBorder(new Color(180, 150, 120)));
        previewArea.setText(generateReceiptText());
        
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton homeBtn = new JButton("🏠 HOME");
        homeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        homeBtn.setBackground(new Color(139, 69, 19));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.addActionListener(e -> {
            dispose();
            new PawCafeHome(cashierName).setVisible(true);
        });
        
        JButton exitBtn = new JButton("🚪 EXIT");
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitBtn.setBackground(new Color(200, 50, 50));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(homeBtn);
        buttonPanel.add(exitBtn);
        
        mainPanel.add(iconLabel, BorderLayout.NORTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("receipt.txt", true))) {
            writer.println(generateReceiptText());
            writer.println("=".repeat(50));
            writer.println();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error saving file: " + e.getMessage(),
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generateReceiptText() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Date now = new Date();
        
        sb.append("🐾 Paw Café\n");
        sb.append("=".repeat(40)).append("\n");
        sb.append("RECEIPT\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Cashier Name: ").append(cashierName).append("\n");
        sb.append("Date: ").append(dateFormat.format(now)).append("\n");
        sb.append("Time: ").append(timeFormat.format(now)).append("\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("%-20s %4s %8s %10s\n", "Item", "Qty", "Price", "Subtotal"));
        sb.append("-".repeat(40)).append("\n");
        
        for (OrderItem item : orderItems) {
            sb.append(String.format("%-20s %4d %8.2f %10.2f\n", 
                item.getName(), 
                item.getQuantity(), 
                item.getPrice(), 
                item.getSubtotal()));
        }
        
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("Total Items: %d\n", totalItems));
        sb.append(String.format("Total (RM): %.2f\n", total));
        if (discount > 0) {
            sb.append(String.format("Discount (10%%): -RM %.2f\n", discount));
        }
        sb.append("=".repeat(40)).append("\n");
        sb.append(String.format("FINAL TOTAL: RM %.2f\n", finalTotal));
        sb.append("=".repeat(40)).append("\n");
        sb.append("\"Simple Orders, Happy Hearts at Paw Café.\"\n");
        
        return sb.toString();
    }
}