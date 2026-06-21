// PawCafeOrder.java - Order Form Page with Edit Function
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private String selectedPaymentMethod = "Cash";
    private int editingRow = -1; // Track which row is being edited (-1 = add mode)
    
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
        setSize(1100, 850);
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
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(new Color(255, 248, 240));
        
        // ========================================
        // TOP SECTION - Cashier Info & Date
        // ========================================
        JPanel topInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
        topInfoPanel.setOpaque(false);
        
        JLabel cashierLabel = new JLabel("Cashier Name : " + cashierName);
        cashierLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        cashierLabel.setForeground(new Color(80, 50, 30));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel dateLabel = new JLabel("Date : " + dateFormat.format(new Date()));
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dateLabel.setForeground(new Color(80, 50, 30));
        
        topInfoPanel.add(cashierLabel);
        topInfoPanel.add(dateLabel);
        contentPanel.add(topInfoPanel, BorderLayout.NORTH);
        
        // ========================================
        // CENTER - Order Form & Order List
        // ========================================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 10, 5, 10);
        c.fill = GridBagConstraints.BOTH;
        
        // LEFT - Add/Edit Item Panel
        JPanel addItemPanel = new JPanel();
        addItemPanel.setLayout(new BoxLayout(addItemPanel, BoxLayout.Y_AXIS));
        addItemPanel.setBackground(Color.WHITE);
        addItemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        addItemPanel.setPreferredSize(new Dimension(320, 550));
        addItemPanel.setMaximumSize(new Dimension(320, 550));
        
        JLabel addTitle = new JLabel("📋 SELECT ITEM");
        addTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        addTitle.setForeground(new Color(139, 69, 19));
        addTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemPanel.add(addTitle);
        addItemPanel.add(Box.createVerticalStrut(15));
        
        // Category
        JPanel catPanel = new JPanel(new BorderLayout(10, 5));
        catPanel.setOpaque(false);
        catPanel.setMaximumSize(new Dimension(280, 35));
        JLabel catLabel = new JLabel("Category :");
        catLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        catPanel.add(catLabel, BorderLayout.WEST);
        
        categoryCombo = new JComboBox<>(new String[]{"Food", "Beverage"});
        categoryCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        categoryCombo.setPreferredSize(new Dimension(180, 30));
        categoryCombo.addActionListener(e -> updateItems());
        catPanel.add(categoryCombo, BorderLayout.CENTER);
        addItemPanel.add(catPanel);
        addItemPanel.add(Box.createVerticalStrut(10));
        
        // Item
        JPanel itemPanel = new JPanel(new BorderLayout(10, 5));
        itemPanel.setOpaque(false);
        itemPanel.setMaximumSize(new Dimension(280, 35));
        JLabel itemLabel = new JLabel("Item :");
        itemLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        itemPanel.add(itemLabel, BorderLayout.WEST);
        
        itemComboBox = new JComboBox<>();
        itemComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        itemComboBox.setPreferredSize(new Dimension(180, 30));
        itemPanel.add(itemComboBox, BorderLayout.CENTER);
        addItemPanel.add(itemPanel);
        addItemPanel.add(Box.createVerticalStrut(10));
        
        // Quantity
        JPanel qtyPanel = new JPanel(new BorderLayout(10, 5));
        qtyPanel.setOpaque(false);
        qtyPanel.setMaximumSize(new Dimension(280, 35));
        JLabel qtyLabel = new JLabel("Quantity :");
        qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        qtyPanel.add(qtyLabel, BorderLayout.WEST);
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        quantitySpinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quantitySpinner.setPreferredSize(new Dimension(80, 30));
        qtyPanel.add(quantitySpinner, BorderLayout.CENTER);
        addItemPanel.add(qtyPanel);
        addItemPanel.add(Box.createVerticalStrut(15));
        
        // Add/Update Button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(280, 50));
        
        JButton addBtn = new JButton("➕ ADD ITEM");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        addBtn.setBackground(new Color(139, 69, 19));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> {
            if (editingRow == -1) {
                addItem();
            } else {
                updateItem();
            }
        });
        
        JButton cancelEditBtn = new JButton("✖ Cancel");
        cancelEditBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cancelEditBtn.setBackground(new Color(180, 150, 120));
        cancelEditBtn.setForeground(Color.WHITE);
        cancelEditBtn.setFocusPainted(false);
        cancelEditBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        cancelEditBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelEditBtn.setVisible(false);
        cancelEditBtn.addActionListener(e -> cancelEdit());
        
        btnPanel.add(addBtn);
        btnPanel.add(cancelEditBtn);
        addItemPanel.add(btnPanel);
        
        // Store references for later
        addBtn.setName("addBtn");
        cancelEditBtn.setName("cancelEditBtn");
        
        addItemPanel.add(Box.createVerticalStrut(15));
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(280, 2));
        addItemPanel.add(sep);
        addItemPanel.add(Box.createVerticalStrut(15));
        
        // Payment Method
        JLabel paymentLabel = new JLabel("💳 Payment Method :");
        paymentLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        paymentLabel.setForeground(new Color(80, 50, 30));
        paymentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addItemPanel.add(paymentLabel);
        addItemPanel.add(Box.createVerticalStrut(10));
        
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        paymentPanel.setOpaque(false);
        paymentPanel.setMaximumSize(new Dimension(280, 35));
        
        paymentGroup = new ButtonGroup();
        cashRadio = new JRadioButton("Cash", true);
        cardRadio = new JRadioButton("Card");
        ewalletRadio = new JRadioButton("E-Wallet");
        
        cashRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cardRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ewalletRadio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        paymentGroup.add(cashRadio);
        paymentGroup.add(cardRadio);
        paymentGroup.add(ewalletRadio);
        
        paymentPanel.add(cashRadio);
        paymentPanel.add(cardRadio);
        paymentPanel.add(ewalletRadio);
        addItemPanel.add(paymentPanel);
        
        addItemPanel.add(Box.createVerticalGlue());
        
        // RIGHT - Order List Panel
        JPanel orderListPanel = new JPanel(new BorderLayout(10, 10));
        orderListPanel.setBackground(Color.WHITE);
        orderListPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 120), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        orderListPanel.setPreferredSize(new Dimension(600, 500));
        
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
        scrollPane.setPreferredSize(new Dimension(550, 280));
        scrollPane.setMinimumSize(new Dimension(400, 200));
        orderListPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom of order list - Remove, Edit buttons & totals
        JPanel bottomOrderPanel = new JPanel(new BorderLayout(10, 5));
        bottomOrderPanel.setOpaque(false);
        bottomOrderPanel.setPreferredSize(new Dimension(550, 120));
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        JPanel actionBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionBtnPanel.setOpaque(false);
        
        JButton editBtn = new JButton("✏️ EDIT SELECTED");
        editBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        editBtn.setBackground(new Color(52, 152, 219));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> editSelectedItem());
        
        JButton removeBtn = new JButton("🗑️ REMOVE SELECTED");
        removeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        removeBtn.setBackground(new Color(200, 50, 50));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFocusPainted(false);
        removeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.addActionListener(e -> removeSelectedItem());
        
        actionBtnPanel.add(editBtn);
        actionBtnPanel.add(removeBtn);
        buttonPanel.add(actionBtnPanel);
        
        JLabel hintLabel = new JLabel("Select item in the order list, then click EDIT or REMOVE");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(new Color(150, 150, 150));
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(hintLabel);
        
        bottomOrderPanel.add(buttonPanel, BorderLayout.WEST);
        
        // Total Panel
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setOpaque(false);
        totalPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        totalItemsLabel = new JLabel("Total Items : 0");
        totalItemsLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        totalItemsLabel.setForeground(new Color(80, 50, 30));
        totalItemsLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        totalPriceLabel = new JLabel("Total Price : RM 0.00");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalPriceLabel.setForeground(new Color(139, 69, 19));
        totalPriceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        totalPanel.add(totalItemsLabel);
        totalPanel.add(Box.createVerticalStrut(5));
        totalPanel.add(totalPriceLabel);
        
        bottomOrderPanel.add(totalPanel, BorderLayout.EAST);
        
        orderListPanel.add(bottomOrderPanel, BorderLayout.SOUTH);
        
        // Add to center panel
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        centerPanel.add(addItemPanel, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.7;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        centerPanel.add(orderListPanel, c);
        
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        
        // ========================================
        // BOTTOM BUTTONS
        // ========================================
        JPanel bottomBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottomBtnPanel.setOpaque(false);
        
        JButton backBtn = new JButton("🔙 Back to Home");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        backBtn.setBackground(new Color(180, 150, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Your current order will be lost. Continue?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new PawCafeHome(cashierName).setVisible(true);
            }
        });
        
            JButton calculateBtn = new JButton("🧮 CALCULATE");
    calculateBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
    calculateBtn.setBackground(new Color(139, 69, 19));
    calculateBtn.setForeground(Color.WHITE);
    calculateBtn.setFocusPainted(false);
    calculateBtn.setBorder(BorderFactory.createEmptyBorder(12, 50, 12, 50));
    calculateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    calculateBtn.addActionListener(e -> {
        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please add at least one item!",
                "Empty Order", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String paymentMethod = getSelectedPaymentMethod();
        dispose();
        new PawCafeCalculate(cashierName, orderItems, paymentMethod).setVisible(true);
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
    
    private void editSelectedItem() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to edit!",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the item from orderItems list
        OrderItem item = orderItems.get(selectedRow);
        editingRow = selectedRow;
        
        // Find the item in menu to set category and combo box
        String itemName = item.getName();
        boolean found = false;
        
        // Check in food menu
        for (int i = 0; i < foodMenu.length; i++) {
            if (foodMenu[i][0].equals(itemName)) {
                categoryCombo.setSelectedItem("Food");
                updateItems();
                itemComboBox.setSelectedIndex(i);
                found = true;
                break;
            }
        }
        
        // Check in beverage menu if not found
        if (!found) {
            for (int i = 0; i < beverageMenu.length; i++) {
                if (beverageMenu[i][0].equals(itemName)) {
                    categoryCombo.setSelectedItem("Beverage");
                    updateItems();
                    itemComboBox.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
        }
        
        // Set quantity
        quantitySpinner.setValue(item.getQuantity());
        
        // Change button text
        Component[] components = ((JPanel) ((JPanel) getContentPane().getComponent(1)).getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                for (Component btn : ((JPanel) comp).getComponents()) {
                    if (btn instanceof JButton && ((JButton) btn).getName() != null && ((JButton) btn).getName().equals("addBtn")) {
                        ((JButton) btn).setText("✏️ UPDATE ITEM");
                        ((JButton) btn).setBackground(new Color(52, 152, 219));
                    }
                    if (btn instanceof JButton && ((JButton) btn).getName() != null && ((JButton) btn).getName().equals("cancelEditBtn")) {
                        btn.setVisible(true);
                    }
                }
            }
        }
        
        // Highlight the selected row
        orderTable.setSelectionBackground(new Color(255, 200, 100));
    }
    
    private void updateItem() {
        if (editingRow == -1) return;
        
        int selectedIndex = itemComboBox.getSelectedIndex();
        String category = (String) categoryCombo.getSelectedItem();
        String[][] menu = category.equals("Food") ? foodMenu : beverageMenu;
        
        String itemName = menu[selectedIndex][0];
        double price = Double.parseDouble(menu[selectedIndex][1]);
        int qty = (int) quantitySpinner.getValue();
        
        // Update the order item
        OrderItem item = orderItems.get(editingRow);
        item.setName(itemName);
        item.setPrice(price);
        item.setQuantity(qty);
        
        // Update table
        double subtotal = qty * price;
        tableModel.setValueAt(itemName, editingRow, 1);
        tableModel.setValueAt(qty, editingRow, 2);
        tableModel.setValueAt(String.format("%.2f", price), editingRow, 3);
        tableModel.setValueAt(String.format("%.2f", subtotal), editingRow, 4);
        
        // Reset editing mode
        cancelEdit();
        
        updateTotals();
        JOptionPane.showMessageDialog(this,
            "✅ " + itemName + " updated successfully!",
            "Item Updated", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cancelEdit() {
        editingRow = -1;
        
        // Reset button text
        Component[] components = ((JPanel) ((JPanel) getContentPane().getComponent(1)).getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                for (Component btn : ((JPanel) comp).getComponents()) {
                    if (btn instanceof JButton && ((JButton) btn).getName() != null && ((JButton) btn).getName().equals("addBtn")) {
                        ((JButton) btn).setText("➕ ADD ITEM");
                        ((JButton) btn).setBackground(new Color(139, 69, 19));
                    }
                    if (btn instanceof JButton && ((JButton) btn).getName() != null && ((JButton) btn).getName().equals("cancelEditBtn")) {
                        btn.setVisible(false);
                    }
                }
            }
        }
        
        // Reset selection color
        orderTable.setSelectionBackground(new Color(255, 248, 240));
        
        // Clear selection
        orderTable.clearSelection();
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
        
        // If currently editing, cancel first
        if (editingRow != -1) {
            cancelEdit();
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
        totalItemsLabel.setText("Total Items : " + totalQty);
        totalPriceLabel.setText("Total Price : RM " + String.format("%.2f", totalPrice));
    }
    
    // Add this method to get payment method
    public String getSelectedPaymentMethod() {
    if (cashRadio.isSelected()) 
    return "Cash";
    if (cardRadio.isSelected()) 
    return "Card";
    if (ewalletRadio.isSelected()) 
    return "E-Wallet";
    return "Cash";
    }
    
    public void addExistingItem(OrderItem item) {
        // Add to order items list
        orderItems.add(item);
        
        // Update table
        int row = tableModel.getRowCount() + 1;
        double subtotal = item.getQuantity() * item.getPrice();
        tableModel.addRow(new Object[]{
            row,
            item.getName(),
            item.getQuantity(),
            String.format("%.2f", item.getPrice()),
            String.format("%.2f", subtotal)
        });
        
        updateTotals();
    }
}
