// PawCafeCalculate.java - Calculate Page with Navigation Bar
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private String paymentMethod;
    
    public PawCafeCalculate(String cashierName, ArrayList<OrderItem> orderItems, String paymentMethod) {
        this.cashierName = cashierName;
        this.orderItems = orderItems;
        this.paymentMethod = paymentMethod;
        setTitle("Paw Café - Calculation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 600);
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
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(255, 248, 240));
        
        // ========================================
        // NAVIGATION BAR
        // ========================================
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(139, 69, 19));
        navBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        navBar.setPreferredSize(new Dimension(getWidth(), 70));
        
        JLabel logoLabel = new JLabel("🐾 Paw Café");
        logoLabel.setFont(new Font("Serif", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        navBar.add(logoLabel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + cashierName);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        
        JButton homeBtn = new JButton("🏠 Home");
        homeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        homeBtn.setBackground(new Color(52, 152, 219));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Your current order will be lost. Continue?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new PawCafeHome(cashierName).setVisible(true);
            }
        });
        
        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(200, 50, 50));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new PawCafeLogin().setVisible(true);
            }
        });
        
        rightPanel.add(userLabel);
        rightPanel.add(homeBtn);
        rightPanel.add(logoutBtn);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(navBar, BorderLayout.NORTH);
        
        // ========================================
        // CONTENT PANEL
        // ========================================
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        contentPanel.setBackground(new Color(255, 248, 240));
        
        // ========================================
        // HEADER
        // ========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Paw Café - Calculation");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(139, 69, 19));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ========================================
        // INFO PANEL (Cashier, Date, Time)
        // ========================================
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Date now = new Date();
        
        JPanel cashierInfo = createInfoBox("👤 Cashier", cashierName);
        JPanel dateInfo = createInfoBox("📅 Date", dateFormat.format(now));
        JPanel timeInfo = createInfoBox("🕐 Time", timeFormat.format(now));
        
        infoPanel.add(cashierInfo);
        infoPanel.add(dateInfo);
        infoPanel.add(timeInfo);
        
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        
        // ========================================
        // CALCULATION PANEL
        // ========================================
        JPanel calcPanel = new JPanel();
        calcPanel.setLayout(new BoxLayout(calcPanel, BoxLayout.Y_AXIS));
        calcPanel.setBackground(Color.WHITE);
        calcPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 2),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        calcPanel.setPreferredSize(new Dimension(500, 320));
        calcPanel.setMaximumSize(new Dimension(500, 320));
        
        // Total Amount (Before Discount)
        JPanel totalPanel = createCalcRow("Total Amount (Before Discount):", 
            String.format("RM %.2f", total), new Color(80, 50, 30));
        calcPanel.add(totalPanel);
        calcPanel.add(Box.createVerticalStrut(10));
        
        // Separator
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(450, 2));
        calcPanel.add(sep1);
        calcPanel.add(Box.createVerticalStrut(10));
        
        // Total Items
        JPanel itemsPanel = createCalcRow("Total Items (Quantity):", 
            String.valueOf(totalItems), new Color(80, 50, 30));
        calcPanel.add(itemsPanel);
        calcPanel.add(Box.createVerticalStrut(10));
        
        // Discount
        Color discountColor = discount > 0 ? new Color(200, 50, 50) : new Color(150, 150, 150);
        JPanel discountPanel = createCalcRow("Discount (10%):", 
            String.format("-RM %.2f", discount), discountColor);
        calcPanel.add(discountPanel);
        calcPanel.add(Box.createVerticalStrut(10));
        
        // Separator
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(450, 2));
        calcPanel.add(sep2);
        calcPanel.add(Box.createVerticalStrut(15));
        
        // Final Total
        JPanel finalPanel = new JPanel(new BorderLayout(15, 0));
        finalPanel.setOpaque(false);
        finalPanel.setMaximumSize(new Dimension(450, 50));
        
        JLabel finalLabel = new JLabel("FINAL TOTAL:");
        finalLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        finalLabel.setForeground(new Color(139, 69, 19));
        
        JLabel finalValue = new JLabel(String.format("RM %.2f", finalTotal));
        finalValue.setFont(new Font("SansSerif", Font.BOLD, 28));
        finalValue.setForeground(new Color(139, 69, 19));
        finalValue.setHorizontalAlignment(SwingConstants.RIGHT);
        
        finalPanel.add(finalLabel, BorderLayout.WEST);
        finalPanel.add(finalValue, BorderLayout.EAST);
        calcPanel.add(finalPanel);
        calcPanel.add(Box.createVerticalStrut(15));
        
        // Discount rule info
        JLabel ruleLabel;
        if (totalItems > 5) {
            ruleLabel = new JLabel("✅ 10% discount applied (Items: " + totalItems + " > 5)");
            ruleLabel.setForeground(new Color(0, 150, 0));
        } else {
            ruleLabel = new JLabel("ℹ️ No discount applied (Need > 5 items)");
            ruleLabel.setForeground(new Color(150, 150, 0));
        }
        ruleLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        ruleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcPanel.add(ruleLabel);
        
        // Center the calc panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(calcPanel);
        
        contentPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // ========================================
        // BOTTOM BUTTONS
        // ========================================
        JPanel bottomBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottomBtnPanel.setOpaque(false);
        
        JButton backBtn = new JButton("🔙 Back to Order");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        backBtn.setBackground(new Color(180, 150, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            // Kembali ke Order Form dengan order yang sama
            dispose();
            PawCafeOrder orderFrame = new PawCafeOrder(cashierName);
            // Pass the existing order items to the new order frame
            for (OrderItem item : orderItems) {
                orderFrame.addExistingItem(item);
            }
            orderFrame.setVisible(true);
        });
        
        JButton receiptBtn = new JButton("📄 VIEW RECEIPT");
        receiptBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        receiptBtn.setBackground(new Color(139, 69, 19));
        receiptBtn.setForeground(Color.WHITE);
        receiptBtn.setFocusPainted(false);
        receiptBtn.setBorder(BorderFactory.createEmptyBorder(12, 50, 12, 50));
        receiptBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        receiptBtn.addActionListener(e -> {
        dispose();
        new PawCafeReceipt(cashierName, orderItems, total, discount, finalTotal, totalItems, paymentMethod).setVisible(true);
        });
        
        bottomBtnPanel.add(backBtn);
        bottomBtnPanel.add(receiptBtn);
        
        contentPanel.add(bottomBtnPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createInfoBox(String label, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 248, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelText.setForeground(new Color(150, 130, 110));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("SansSerif", Font.BOLD, 16));
        valueText.setForeground(new Color(80, 50, 30));
        valueText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(labelText);
        panel.add(Box.createVerticalStrut(3));
        panel.add(valueText);
        
        return panel;
    }
    
    private JPanel createCalcRow(String label, String value, Color valueColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(450, 35));
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("SansSerif", Font.BOLD, 16));
        labelText.setForeground(new Color(80, 50, 30));
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("SansSerif", Font.BOLD, 18));
        valueText.setForeground(valueColor);
        valueText.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(labelText, BorderLayout.WEST);
        panel.add(valueText, BorderLayout.EAST);
        
        return panel;
    }
    
    // Method to pass existing order items back to Order Form
    public void addExistingItem(OrderItem item) {
        // This method is called from PawCafeOrder to add existing items
        // The implementation is in PawCafeOrder
    }
}
