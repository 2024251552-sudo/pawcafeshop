// PawCafeReceipt.java - Receipt Page
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PawCafeReceipt extends JFrame {
    private String cashierName;
    private ArrayList<OrderItem> orderItems;
    private double total, discount, finalTotal;
    private int totalItems;
    
    public PawCafeReceipt(String cashierName, ArrayList<OrderItem> orderItems, 
                         double total, double discount, double finalTotal, int totalItems) {
        this.cashierName = cashierName;
        this.orderItems = orderItems;
        this.total = total;
        this.discount = discount;
        this.finalTotal = finalTotal;
        this.totalItems = totalItems;
        
        setTitle("Paw Café - Receipt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeUI();
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(255, 248, 240));
        
        // Receipt content panel
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Header
        JLabel cafeName = new JLabel("🐾 Paw Café");
        cafeName.setFont(new Font("Serif", Font.BOLD, 24));
        cafeName.setForeground(new Color(139, 69, 19));
        cafeName.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(cafeName);
        
        receiptPanel.add(Box.createVerticalStrut(5));
        
        JLabel receiptTitle = new JLabel("RECEIPT");
        receiptTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        receiptTitle.setForeground(new Color(80, 50, 30));
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(receiptTitle);
        
        receiptPanel.add(Box.createVerticalStrut(10));
        receiptPanel.add(new JSeparator());
        receiptPanel.add(Box.createVerticalStrut(10));
        
        // Cashier Info
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        
        JLabel cashierInfo = new JLabel("Cashier Name: " + cashierName);
        cashierInfo.setFont(new Font("Monospaced", Font.BOLD, 14));
        cashierInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(cashierInfo);
        
        JLabel dateInfo = new JLabel("Date: " + dateFormat.format(new Date()));
        dateInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        dateInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(dateInfo);
        
        JLabel timeInfo = new JLabel("Time: " + timeFormat.format(new Date()));
        timeInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        timeInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(timeInfo);
        
        receiptPanel.add(Box.createVerticalStrut(10));
        receiptPanel.add(new JSeparator());
        receiptPanel.add(Box.createVerticalStrut(10));
        
        // Items header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(new JLabel("Item"), BorderLayout.WEST);
        JPanel qtyPricePanel = new JPanel(new GridLayout(1, 3, 10, 0));
        qtyPricePanel.setOpaque(false);
        qtyPricePanel.add(new JLabel("Qty", SwingConstants.CENTER));
        qtyPricePanel.add(new JLabel("Unit Price", SwingConstants.CENTER));
        qtyPricePanel.add(new JLabel("Subtotal", SwingConstants.RIGHT));
        headerPanel.add(qtyPricePanel, BorderLayout.EAST);
        headerPanel.setFont(new Font("Monospaced", Font.BOLD, 12));
        receiptPanel.add(headerPanel);
        
        receiptPanel.add(Box.createVerticalStrut(5));
        receiptPanel.add(new JSeparator());
        receiptPanel.add(Box.createVerticalStrut(5));
        
        // Items
        for (OrderItem item : orderItems) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(item.getName());
            nameLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
            itemPanel.add(nameLabel, BorderLayout.WEST);
            
            JPanel detailsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            detailsPanel.setOpaque(false);
            detailsPanel.add(new JLabel(String.valueOf(item.getQuantity()), SwingConstants.CENTER));
            detailsPanel.add(new JLabel(String.format("RM %.2f", item.getPrice()), SwingConstants.CENTER));
            detailsPanel.add(new JLabel(String.format("RM %.2f", item.getSubtotal()), SwingConstants.RIGHT));
            
            itemPanel.add(detailsPanel, BorderLayout.EAST);
            receiptPanel.add(itemPanel);
        }
        
        receiptPanel.add(Box.createVerticalStrut(10));
        receiptPanel.add(new JSeparator());
        receiptPanel.add(Box.createVerticalStrut(10));
        
        // Totals
        JPanel totalsPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        totalsPanel.setOpaque(false);
        totalsPanel.setMaximumSize(new Dimension(500, 80));
        
        totalsPanel.add(new JLabel("Total Items:"));
        totalsPanel.add(new JLabel(String.valueOf(totalItems), SwingConstants.RIGHT));
        
        totalsPanel.add(new JLabel("Total (RM):"));
        totalsPanel.add(new JLabel(String.format("RM %.2f", total), SwingConstants.RIGHT));
        
        if (discount > 0) {
            totalsPanel.add(new JLabel("Discount (10%):"));
            totalsPanel.add(new JLabel(String.format("-RM %.2f", discount), SwingConstants.RIGHT));
        }
        
        receiptPanel.add(totalsPanel);
        
        receiptPanel.add(Box.createVerticalStrut(10));
        receiptPanel.add(new JSeparator());
        receiptPanel.add(Box.createVerticalStrut(10));
        
        // Final Total
        JLabel finalLabel = new JLabel("FINAL TOTAL: " + String.format("RM %.2f", finalTotal));
        finalLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        finalLabel.setForeground(new Color(139, 69, 19));
        finalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(finalLabel);
        
        receiptPanel.add(Box.createVerticalStrut(10));
        receiptPanel.add(new JSeparator());
        receiptPanel.add(Box.createVerticalStrut(10));
        
        // Payment Method
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paymentPanel.setOpaque(false);
        paymentPanel.add(new JLabel("Payment Method:"));
        String[] methods = {"Cash", "Credit Card", "Debit Card", "E-Wallet"};
        JComboBox<String> paymentCombo = new JComboBox<>(methods);
        paymentCombo.setPreferredSize(new Dimension(120, 25));
        paymentPanel.add(paymentCombo);
        receiptPanel.add(paymentPanel);
        
        receiptPanel.add(Box.createVerticalStrut(10));
        
        // Footer
        JLabel footer = new JLabel("\"Simple Orders, Happy Hearts at Paw Café.\"");
        footer.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footer.setForeground(new Color(101, 67, 33));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptPanel.add(footer);
        
        // Scroll pane for receipt
        JScrollPane scrollPane = new JScrollPane(receiptPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton backBtn = new JButton("🔙 Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        backBtn.setBackground(new Color(180, 150, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new PawCafeHome(cashierName).setVisible(true);
        });
        
        JButton saveBtn = new JButton("💾 SAVE TO FILE");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setBackground(new Color(139, 69, 19));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            dispose();
            new PawCafeSave(cashierName, orderItems, total, discount, finalTotal, totalItems).setVisible(true);
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(saveBtn);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}