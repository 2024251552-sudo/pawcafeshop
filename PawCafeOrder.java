// PawCafeOrder.java - Order Form Page with Navigation Bar
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PawCafeOrder extends JFrame {
    private String cashierName;
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> itemComboBox;
    private JSpinner quantitySpinner;
    private JLabel totalItemsLabel;
    private JLabel totalPriceLabel;
    private JRadioButton cashRadio, cardRadio, ewalletRadio;
    private ButtonGroup paymentGroup;
    private ArrayList<OrderItem> orderItems = new ArrayList<>();
    
    // Food Menu
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
    
    // Beverage Menu
    private String[][] beverageMenu = {
        {"Iced Latte", "9.00"},
        {"Hot Chocolate", "8.00"},
        {"Lemon Tea", "6.00"},
        {"Matcha Latte", "10.00"},
        {"Americano", "7.00"},
        {"Cappuccino", "8.00"}
    };
    
    public PawCafeOrder(String cashierName) {
        this.cashierName = cashierName;
        setTitle("Paw Café - Order Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 750);
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
            dispose();
            new PawCafeHome(cashierName).setVisible(true);
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
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(new Color(255, 248, 240));
        
        // ========================================
        // TOP SECTION - Cashier Info & Date
        // ========================================
        JPanel topInfoPanel = new JPanel(new BorderLayout());
        topInfoPanel.setOpaque(false);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
        infoPanel.setOpaque(false);
        
        JLabel cashierLabel = new JLabel("Cashier Name : " + cashierName);
        cashierLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        cashierLabel.setForeground(new Color(80, 50, 30));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel dateLabel = new JLabel("Date : " + dateFormat.format(new Date()));
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dateLabel.setForeground(new Color(80, 50, 30));
        
        infoPanel.add(cashierLabel);
        infoPanel.add(dateLabel);
        topInfoPanel.add(infoPanel, BorderLayout.WEST);
        
        contentPanel.add(topInfoPanel, BorderLayout.NORTH);
        
        // ========================================
        // CENTER - Order Form & Order List
        // ========================================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        c.fill = GridBagConstraints.BOTH;
        
        // LEFT - Add Item Panel
        JPanel addItemPanel = new JPanel();
        addItemPanel.setLayout(new BoxLayout(addItemPanel, BoxLayout.Y_AXIS));
        addItemPanel.setBackground(Color.WHITE);
        addItemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        addItemPanel.setPreferredSize(new Dimension(300, 400));
        
        JLabel addTitle = new JLabel("📋 SELECT ITEM");
        addTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        addTitle.setForeground(new Color(139, 69, 19));
        addTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemPanel.add(addTitle);
        addItemPanel.add(Box.createVerticalStrut(15));
        
        // Category
        JPanel catPanel = new JPanel(new BorderLayout(10, 5));
        catPanel.setOpaque(false);
        JLabel catLabel = new JLabel("Category :");
        catLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        catPanel.add(catLabel, BorderLayout.WEST);
        
        categoryCombo = new JComboBox<>(new String[]{"Food", "Beverage"});
        categoryCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        categoryCombo.addActionListener(e -> updateItems());
        catPanel.add(categoryCombo, BorderLayout.CENTER);
        addItemPanel.add(catPanel);
        addItemPanel.add(Box.createVerticalStrut(10));
        
        // Item
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setOpaque(false);
        JLabel itemLabel = new JLabel("Item :");
        itemLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        itemPanel.add(itemLabel, BorderLayout.WEST);
        
        itemComboBox = new JComboBox<>();
        itemComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        itemPanel.add(itemComboBox, BorderLayout.CENTER);
        addItemPanel.add(itemPanel);
        addItemPanel.add(Box.createVerticalStrut(10));
        
        // Quantity
        JPanel qtyPanel = new JPanel(new BorderLayout(10, 5));
        qtyPanel.setOpaque(false);
        JLabel qtyLabel = new JLabel("Quantity :");
        qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        qtyPanel.add(qtyLabel, BorderLayout.WEST);
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        quantitySpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        qtyPanel.add(quantitySpinner, BorderLayout.CENTER);
        addItemPanel.add(qtyPanel);
        addItemPanel.add(Box.createVerticalStrut(15));
        
        // Add Button
        JButton addBtn = new JButton("➕ ADD ITEM");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        addBtn.setBackground(new Color(139, 69, 19));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(e -> addItem());
        addItemPanel.add(addBtn);
        addItemPanel.add(Box.createVerticalStrut(20));
        
        // Payment Method
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(250, 2));
        addItemPanel.add(sep);
        addItemPanel.add(Box.createVerticalStrut(15));
        
        JLabel paymentLabel = new JLabel("💳 Payment Method :");
        paymentLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        paymentLabel.setForeground(new Color(80, 50, 30));
        paymentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemPanel.add(paymentLabel);
        addItemPanel.add(Box.createVerticalStrut(10));
        
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        paymentPanel.setOpaque(false);
        
        paymentGroup = new ButtonGroup();
        cashRadio = new JRadioButton("Cash", true);
        cardRadio = new JRadioButton("Card");
        ewalletRadio = new JRadioButton("E-Wallet");
        
        cashRadio.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cardRadio.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ewalletRadio.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        paymentGroup.add(cashRadio);
        paymentGroup.add(cardRadio);
        paymentGroup.add(ewalletRadio);
        
        paymentPanel.add(cashRadio);
        paymentPanel.add(cardRadio);
        paymentPanel.add(ewalletRadio);
        addItemPanel.add(paymentPanel);
        
        // RIGHT - Order List
        JPanel orderListPanel = new JPanel(new BorderLayout());
        orderListPanel.setBackground(Color.WHITE);
        orderListPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel orderTitle = new JLabel("📋 ORDER LIST");
        orderTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        orderTitle.setForeground(new Color(139, 69, 19));
        orderTitle.setHorizontalAlignment(SwingConstants.CENTER);
        orderListPanel.add(orderTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"No.", "Item", "Qty", "Price (RM)", "Total (RM)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        orderTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        orderTable.getTableHeader().setBackground(new Color(139, 69, 19));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.setRowHeight(30);
        orderTable.setSelectionBackground(new Color(255, 248, 240));
        orderTable.setSelectionForeground(new Color(80, 50, 30));
        
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        orderListPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom of order list - Remove button & totals
        JPanel bottomOrderPanel = new JPanel(new BorderLayout());
        bottomOrderPanel.setOpaque(false);
        
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removePanel.setOpaque(false);
        
        JButton removeBtn = new JButton("🗑️ REMOVE SELECTED");
        removeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        removeBtn.setBackground(new Color(200, 50, 50));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFocusPainted(false);
        removeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.addActionListener(e -> removeSelectedItem());
        removePanel.add(removeBtn);
        
        JLabel hintLabel = new JLabel("Select item in the order list, then click REMOVE SELECTED");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(150, 150, 150));
        removePanel.add(hintLabel);
        
        bottomOrderPanel.add(removePanel, BorderLayout.WEST);
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        totalPanel.setOpaque(false);
        
        totalItemsLabel = new JLabel("Total Items: 0");
        totalItemsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalItemsLabel.setForeground(new Color(80, 50, 30));
        
        totalPriceLabel = new JLabel("Total Price: RM 0.00");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalPriceLabel.setForeground(new Color(139, 69, 19));
        
        totalPanel.add(totalItemsLabel);
        totalPanel.add(totalPriceLabel);
        
        bottomOrderPanel.add(totalPanel, BorderLayout.EAST);
        
        orderListPanel.add(bottomOrderPanel, BorderLayout.SOUTH);
        
        // Add to center panel
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.35;
        c.weighty = 1.0;
        centerPanel.add(addItemPanel, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.65;
        c.weighty = 1.0;
        centerPanel.add(orderListPanel, c);
        
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        
        // ========================================
        // BOTTOM BUTTONS
        // ========================================
        JPanel bottomBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottomBtnPanel.setOpaque(false);
        
        JButton backBtn = new JButton("🔙 Back to Home");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setBackground(new Color(180, 150, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dispose();
            new PawCafeHome(cashierName).setVisible(true);
        });
        
        JButton calculateBtn = new JButton("🧮 CALCULATE");
        calculateBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        calculateBtn.setBackground(new Color(139, 69, 19));
        calculateBtn.setForeground(Color.WHITE);
        calculateBtn.setFocusPainted(false);
        calculateBtn.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        calculateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calculateBtn.addActionListener(e -> {
            if (orderItems.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please add at least one item!",
                    "Empty Order", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            dispose();
            new PawCafeCalculate(cashierName, orderItems).setVisible(true);
        });
        
        bottomBtnPanel.add(backBtn);
        bottomBtnPanel.add(calculateBtn);
        
        contentPanel.add(bottomBtnPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Initialize items
        updateItems();
    }
    
    private void updateItems() {
        itemComboBox.removeAllItems();
        String category = (String) categoryCombo.getSelectedItem();
        String[][] menu = category.equals("Food") ? foodMenu : beverageMenu;
        
        for (String[] item : menu) {
            itemComboBox.addItem(item[0] + " (RM" + item[1] + ")");
        }
    }
    
    private void addItem() {
        int selectedIndex = itemComboBox.getSelectedIndex();
        String category = (String) categoryCombo.getSelectedItem();
        String[][] menu = category.equals("Food") ? foodMenu : beverageMenu;
        
        String itemName = menu[selectedIndex][0];
        double price = Double.parseDouble(menu[selectedIndex][1]);
        int qty = (int) quantitySpinner.getValue();
        
        OrderItem item = new OrderItem(itemName, qty, price);
        orderItems.add(item);
        
        // Update table
        int row = tableModel.getRowCount() + 1;
        double subtotal = qty * price;
        tableModel.addRow(new Object[]{
            row,
            itemName,
            qty,
            String.format("%.2f", price),
            String.format("%.2f", subtotal)
        });
        
        updateTotals();
        JOptionPane.showMessageDialog(this,
            "✅ " + itemName + " added successfully!",
            "Item Added", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void removeSelectedItem() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to remove!",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        orderItems.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        
        // Renumber rows
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
        
        updateTotals();
        JOptionPane.showMessageDialog(this,
            "🗑️ Item removed successfully!",
            "Item Removed", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateTotals() {
        int totalQty = 0;
        double totalPrice = 0;
        for (OrderItem item : orderItems) {
            totalQty += item.getQuantity();
            totalPrice += item.getSubtotal();
        }
        totalItemsLabel.setText("Total Items: " + totalQty);
        totalPriceLabel.setText("Total Price: RM " + String.format("%.2f", totalPrice));
    }
}