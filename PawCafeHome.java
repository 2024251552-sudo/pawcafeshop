// PawCafeHome.java - Home Page with Navigation Bar (Larger Version)
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class PawCafeHome extends JFrame {
    private String cashierName;
    
    // Menu items
    private String[][] foodMenu = {
        {"Chicken Sandwich", "12.00"},
        {"Beef Burger", "14.00"},
        {"Spaghetti Bolognese", "13.00"},
        {"Fish & Chips", "15.00"},
        {"Grilled Chicken", "14.00"},
        {"Caesar Salad", "10.00"},
        {"French Fries", "6.00"},
        {"Garlic Bread", "5.00"}
    };
    
    private String[][] beverageMenu = {
        {"Iced Latte", "9.00"},
        {"Hot Chocolate", "8.00"},
        {"Lemon Tea", "6.00"},
        {"Matcha Latte", "10.00"},
        {"Americano", "7.00"},
        {"Cappuccino", "8.00"}
    };
    
    public PawCafeHome(String cashierName) {
        this.cashierName = cashierName;
        setTitle("Paw Café - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750); // BESARKAN
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
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(255, 248, 240));
        
        // ========================================
        // NAVIGATION BAR - BESARKAN
        // ========================================
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(139, 69, 19));
        navBar.setBorder(new EmptyBorder(15, 30, 15, 30));
        navBar.setPreferredSize(new Dimension(getWidth(), 70));
        
        // Left side - Logo
        JLabel logoLabel = new JLabel("🐾 Paw Café");
        logoLabel.setFont(new Font("Serif", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        navBar.add(logoLabel, BorderLayout.WEST);
        
        // Right side - User info and Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + cashierName);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userLabel.setForeground(Color.WHITE);
        
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
        rightPanel.add(logoutBtn);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(navBar, BorderLayout.NORTH);
        
        // ========================================
        // CONTENT PANEL - BESARKAN
        // ========================================
        JPanel contentPanel = new JPanel(new BorderLayout(25, 25));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setBackground(new Color(255, 248, 240));
        
        // Welcome Header - BESARKAN
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Paw Café");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(new Color(139, 69, 19));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + cashierName + "! 🐾");
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        welcomeLabel.setForeground(new Color(101, 67, 33));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.SOUTH);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ========================================
        // MENU PANEL (Food & Beverage) - BESARKAN
        // ========================================
        JPanel menuWrapper = new JPanel(new GridLayout(1, 2, 30, 0));
        menuWrapper.setOpaque(false);
        
        // Food Menu
        JPanel foodPanel = createMenuPanel("🍽️ FOOD MENU", foodMenu);
        menuWrapper.add(foodPanel);
        
        // Beverage Menu
        JPanel beveragePanel = createMenuPanel("☕ BEVERAGE MENU", beverageMenu);
        menuWrapper.add(beveragePanel);
        
        contentPanel.add(menuWrapper, BorderLayout.CENTER);
        
        // ========================================
        // START ORDER BUTTON - BESARKAN
        // ========================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonPanel.setOpaque(false);
        
        JButton startOrderBtn = new JButton("📝 START NEW ORDER");
        startOrderBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        startOrderBtn.setBackground(new Color(139, 69, 19));
        startOrderBtn.setForeground(Color.WHITE);
        startOrderBtn.setFocusPainted(false);
        startOrderBtn.setBorder(BorderFactory.createEmptyBorder(18, 60, 18, 60));
        startOrderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startOrderBtn.addActionListener(e -> {
            dispose();
            new PawCafeOrder(cashierName).setVisible(true);
        });
        
        buttonPanel.add(startOrderBtn);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createMenuPanel(String title, String[][] items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title - BESARKAN
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 69, 19));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(180, 150, 120));
        sep.setMaximumSize(new Dimension(350, 3));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(15));
        
        // Menu items - BESARKAN
        for (int i = 0; i < items.length; i++) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(false);
            itemPanel.setMaximumSize(new Dimension(400, 35));
            
            String num = String.format("%2d.", i + 1);
            JLabel nameLabel = new JLabel(num + " " + items[i][0]);
            nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            nameLabel.setForeground(new Color(60, 40, 20));
            
            JLabel priceLabel = new JLabel("RM" + items[i][1]);
            priceLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            priceLabel.setForeground(new Color(139, 69, 19));
            
            itemPanel.add(nameLabel, BorderLayout.WEST);
            itemPanel.add(priceLabel, BorderLayout.EAST);
            
            panel.add(itemPanel);
            panel.add(Box.createVerticalStrut(6));
        }
        
        return panel;
    }
}