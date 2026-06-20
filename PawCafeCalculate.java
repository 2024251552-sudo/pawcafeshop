// PawCafeCalculate.java - Calculate Page
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PawCafeCalculate extends JFrame {
    private String cashierName;
    private ArrayList<OrderItem> orderItems;
    private double total = 0;
    private int totalItems = 0;
    private double discount = 0;
    private double finalTotal = 0;
    
    public PawCafeCalculate(String cashierName, ArrayList<OrderItem> orderItems) {
        this.cashierName = cashierName;
        this.orderItems = orderItems;
        setTitle("Paw Café - Calculation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        calculateTotals();
        initializeUI();
    }
    
    private void calculateTotals() {
        totalItems = 0;
        total = 0;
        for (OrderItem item : orderItems) {
            totalItems += item.getQuantity();
            total += item.getSubtotal();
        }
        
        // 10% discount if total items > 5
        if (totalItems > 5) {
            discount = total * 0.10;
        }
        finalTotal = total - discount;
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(255, 248, 240));
        
        // Header
        JLabel titleLabel = new JLabel("🧮 Order Summary");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(139, 69, 19));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Details Panel - FIXED VERSION
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120)),
            "Calculation Details",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 14),
            new Color(80, 50, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Cashier Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel cashierLabel = new JLabel("Cashier Name:");
        cashierLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailsPanel.add(cashierLabel, gbc);
        
        gbc.gridx = 1;
        JLabel cashierValue = new JLabel(cashierName);
        cashierValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cashierValue.setForeground(new Color(139, 69, 19));
        detailsPanel.add(cashierValue, gbc);
        
        // Date & Time
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailsPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel dateValue = new JLabel(dateFormat.format(new Date()));
        dateValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsPanel.add(dateValue, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailsPanel.add(timeLabel, gbc);
        
        gbc.gridx = 1;
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        JLabel timeValue = new JLabel(timeFormat.format(new Date()));
        timeValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsPanel.add(timeValue, gbc);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        detailsPanel.add(new JSeparator(), gbc);
        
        // Total Items
        gbc.gridy = 4;
        JLabel itemsLabel = new JLabel("Total Items:");
        itemsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailsPanel.add(itemsLabel, gbc);
        
        // Subtotal
        gbc.gridy = 5;
        JLabel subtotalLabel = new JLabel("Subtotal:");
        subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailsPanel.add(subtotalLabel, gbc);
        
        gbc.gridx = 1;
        JLabel subtotalValue = new JLabel(String.format("RM %.2f", total));
        subtotalValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsPanel.add(subtotalValue, gbc);
        
        // Discount
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel discountLabel = new JLabel("Discount (10%):");
        discountLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        discountLabel.setForeground(new Color(200, 50, 50));
        detailsPanel.add(discountLabel, gbc);
        
        gbc.gridx = 1;
        JLabel discountValue = new JLabel(String.format("-RM %.2f", discount));
        discountValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        discountValue.setForeground(new Color(200, 50, 50));
        detailsPanel.add(discountValue, gbc);
        
        // Discount rule info
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        if (totalItems > 5) {
            JLabel discountInfo = new JLabel("✅ 10% discount applied (Items: " + totalItems + " > 5)");
            discountInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
            discountInfo.setForeground(new Color(0, 150, 0));
            detailsPanel.add(discountInfo, gbc);
        } else {
            JLabel discountInfo = new JLabel("ℹ️ No discount (Need > 5 items)");
            discountInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
            discountInfo.setForeground(new Color(150, 150, 0));
            detailsPanel.add(discountInfo, gbc);
        }
        
        // Final Total
        gbc.gridy = 8;
        JLabel finalLabel = new JLabel("FINAL TOTAL:");
        finalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        finalLabel.setForeground(new Color(139, 69, 19));
        detailsPanel.add(finalLabel, gbc);
        
        gbc.gridx = 1;
        JLabel finalValue = new JLabel(String.format("RM %.2f", finalTotal));
        finalValue.setFont(new Font("SansSerif", Font.BOLD, 20));
        finalValue.setForeground(new Color(139, 69, 19));
        detailsPanel.add(finalValue, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton backBtn = new JButton("🔙 Back to Order");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        backBtn.setBackground(new Color(180, 150, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new PawCafeOrder(cashierName).setVisible(true);
        });
        
        JButton receiptBtn = new JButton("📄 VIEW RECEIPT");
        receiptBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        receiptBtn.setBackground(new Color(139, 69, 19));
        receiptBtn.setForeground(Color.WHITE);
        receiptBtn.setFocusPainted(false);
        receiptBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        receiptBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        receiptBtn.addActionListener(e -> {
            dispose();
            new PawCafeReceipt(cashierName, orderItems, total, discount, finalTotal, totalItems).setVisible(true);
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(receiptBtn);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}