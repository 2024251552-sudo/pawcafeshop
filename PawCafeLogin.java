// PawCafeLogin.java - Login Page with Beautiful Design
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class PawCafeLogin extends JFrame {

    // ---- Colour Palette ----
    static final Color BG_CREAM      = new Color(0xFB, 0xF1, 0xE3);
    static final Color BROWN_DARK    = new Color(0x4A, 0x2C, 0x1D);
    static final Color BROWN_TEXT    = new Color(0x5C, 0x3A, 0x21);
    static final Color TAN_TEXT      = new Color(0xC8, 0x8A, 0x52);
    static final Color FIELD_BORDER  = new Color(0xE3, 0xD2, 0xBE);
    static final Color RESET_BG      = new Color(0xF3, 0xCF, 0xA6);
    static final Color RESET_BG_HOVER= new Color(0xEE, 0xC0, 0x90);
    static final Color CAT_BODY      = new Color(0xFD, 0xF3, 0xE4);
    static final Color CAT_STRIPE    = new Color(0xE3, 0xA8, 0x6B);
    static final Color CAT_BLUSH     = new Color(0xF6, 0xB6, 0x9A);
    static final Color PAW_DECOR     = new Color(0xE7, 0xC9, 0xAA);

    // Default credentials
    private static final String DEFAULT_USERNAME = "staff01";
    private static final String DEFAULT_PASSWORD = "12345";

    private RoundedField usernameField;
    private RoundedPasswordField passwordField;
    private RoundedButton loginButton; // Store reference to login button

    public PawCafeLogin() {
        super("Paw Café - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 660);
        setMinimumSize(new Dimension(420, 600));
        setLocationRelativeTo(null);

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        root.setBackground(BG_CREAM);
        root.setBorder(new EmptyBorder(28, 36, 0, 36));
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);

        CatPanel catPanel = new CatPanel();
        catPanel.setPreferredSize(new Dimension(100, 220));
        root.add(catPanel, BorderLayout.SOUTH);

        // Set default button
        if (loginButton != null) {
            getRootPane().setDefaultButton(loginButton);
        }

        setVisible(true);
    }

    /** Title + welcome banner */
    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel title = new JLabel("🐾 Paw Café");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(BROWN_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("→  Welcome!  ←");
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 18));
        welcome.setForeground(TAN_TEXT);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setBorder(new EmptyBorder(8, 0, 18, 0));

        header.add(title);
        header.add(welcome);
        return header;
    }

    /** Username / password fields + buttons */
    private JPanel buildForm() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 0, 8, 0);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = makeFieldLabel("Username :");
        JLabel passLabel = makeFieldLabel("Password :");

        usernameField = new RoundedField(16, 16);
        usernameField.setText(DEFAULT_USERNAME);
        passwordField = new RoundedPasswordField(16, 16);

        c.gridx = 0; c.gridy = 0; c.weightx = 0; c.gridwidth = 1;
        c.insets = new Insets(10, 0, 10, 12);
        form.add(userLabel, c);
        c.gridx = 1; c.weightx = 1;
        form.add(usernameField, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        form.add(passLabel, c);
        c.gridx = 1; c.weightx = 1;
        form.add(passwordField, c);

        // Buttons row
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        buttons.setOpaque(false);

        loginButton = new RoundedButton("🐾 LOGIN", BROWN_DARK, Color.WHITE, 18);
        RoundedButton resetBtn = new RoundedButton("RESET", RESET_BG, BROWN_TEXT, 18);
        resetBtn.setHoverColor(RESET_BG_HOVER);

        loginButton.addActionListener(e -> onLogin());
        resetBtn.addActionListener(e -> onReset());

        buttons.add(loginButton);
        buttons.add(resetBtn);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 1;
        c.insets = new Insets(20, 0, 10, 0);
        form.add(buttons, c);

        return form;
    }

    private JLabel makeFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(BROWN_TEXT);
        return label;
    }

    private void onLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        // Validate empty fields
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password.",
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check credentials
        if (user.equals(DEFAULT_USERNAME) && pass.equals(DEFAULT_PASSWORD)) {
            JOptionPane.showMessageDialog(this,
                "✅ Login Successful!\nWelcome, " + user + "! 🐾",
                "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new PawCafeHome(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Invalid username or password!\nPlease try again.",
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            onReset();
        }
    }

    private void onReset() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocusInWindow();
    }

    // =====================================================================
    //  Custom rounded text field
    // =====================================================================
    static class RoundedField extends JTextField {
        private final int radius;

        RoundedField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.WHITE);
            setFont(new Font("SansSerif", Font.PLAIN, 15));
            setBorder(new EmptyBorder(9, 14, 9, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(FIELD_BORDER);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    // =====================================================================
    //  Custom rounded password field
    // =====================================================================
    static class RoundedPasswordField extends JPasswordField {
        private final int radius;

        RoundedPasswordField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.WHITE);
            setFont(new Font("SansSerif", Font.PLAIN, 15));
            setBorder(new EmptyBorder(9, 14, 9, 14));
            setEchoChar('•');
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(FIELD_BORDER);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    // =====================================================================
    //  Custom rounded button
    // =====================================================================
    static class RoundedButton extends JButton {
        private final int radius;
        private final Color baseColor;
        private Color hoverColor;

        RoundedButton(String text, Color baseColor, Color textColor, int radius) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = baseColor.darker();
            this.radius = radius;
            setForeground(textColor);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(11, 26, 11, 26));
        }

        void setHoverColor(Color c) {
            this.hoverColor = c;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hoverColor : baseColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =====================================================================
    //  Cute cat illustration + paw print decorations at the bottom
    // =====================================================================
    static class CatPanel extends JPanel {
        CatPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Decorative paw prints, bottom corners
            drawPawPrint(g2, 14, h - 70, 46, withAlpha(PAW_DECOR, 160));
            drawPawPrint(g2, w - 60, h - 70, 46, withAlpha(PAW_DECOR, 160));

            // Cat geometry
            int headSize = Math.min(170, w / 2);
            int cx = w / 2;
            int headTop = 18;
            int headCy = headTop + headSize / 2;

            drawCat(g2, cx, headCy, headSize, w, h);

            g2.dispose();
        }

        private void drawCat(Graphics2D g2, int cx, int cy, int size, int panelW, int panelH) {
            int r = size / 2;

            // Ears
            Polygon earL = triangle(cx - r + 8, cy - r + 30, cx - r - 18, cy - r - 28, cx - r + 46, cy - r - 4);
            Polygon earR = triangle(cx + r - 8, cy - r + 30, cx + r + 18, cy - r - 28, cx + r - 46, cy - r - 4);
            g2.setColor(CAT_BODY);
            g2.fillPolygon(earL);
            g2.fillPolygon(earR);
            g2.setColor(CAT_STRIPE);
            g2.fillPolygon(scaleTriangle(earL, 0.55));
            g2.fillPolygon(scaleTriangle(earR, 0.55));

            // Head
            g2.setColor(CAT_BODY);
            g2.fillOval(cx - r, cy - r, size, size);

            // Tabby stripes on forehead
            g2.setColor(CAT_STRIPE);
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(cx - r + 18, cy - r + 6, 26, 30, 200, 120);
            g2.drawArc(cx - 13, cy - r - 4, 26, 30, 200, 120);
            g2.drawArc(cx + r - 44, cy - r + 6, 26, 30, 200, 120);

            // Blush cheeks
            g2.setColor(withAlpha(CAT_BLUSH, 140));
            g2.fillOval(cx - r + 6, cy + 6, 30, 18);
            g2.fillOval(cx + r - 36, cy + 6, 30, 18);

            // Eyes
            int eyeW = size / 9, eyeH = (int) (eyeW * 1.3);
            int eyeY = cy - 6;
            g2.setColor(Color.BLACK);
            g2.fillOval(cx - r / 2 - eyeW / 2, eyeY, eyeW, eyeH);
            g2.fillOval(cx + r / 2 - eyeW / 2, eyeY, eyeW, eyeH);
            g2.setColor(Color.WHITE);
            g2.fillOval(cx - r / 2 - eyeW / 2 + 2, eyeY + 2, eyeW / 3, eyeH / 3);
            g2.fillOval(cx + r / 2 - eyeW / 2 + 2, eyeY + 2, eyeW / 3, eyeH / 3);

            // Nose
            int noseW = 10, noseH = 7;
            g2.setColor(CAT_BLUSH);
            g2.fillOval(cx - noseW / 2, cy + eyeH, noseW, noseH);

            // Mouth (simple curved smile using two arcs)
            g2.setColor(new Color(0x8a, 0x5a, 0x3c));
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int mouthY = cy + eyeH + noseH;
            g2.drawArc(cx - 14, mouthY - 4, 14, 12, 200, 140);
            g2.drawArc(cx, mouthY - 4, 14, 12, 200, 140);

            // Whiskers
            g2.setStroke(new BasicStroke(1.4f));
            g2.setColor(new Color(0, 0, 0, 110));
            int wy = cy + eyeH + 2;
            g2.draw(new Line2D.Double(cx - r + 6, wy, cx - r - 26, wy - 6));
            g2.draw(new Line2D.Double(cx - r + 6, wy + 8, cx - r - 26, wy + 10));
            g2.draw(new Line2D.Double(cx + r - 6, wy, cx + r + 26, wy - 6));
            g2.draw(new Line2D.Double(cx + r - 6, wy + 8, cx + r + 26, wy + 10));

            // Paws peeking over the bottom edge of the panel
            int pawW = 46, pawH = 60;
            g2.setColor(CAT_BODY);
            g2.fillRoundRect(cx - pawW - 6, panelH - pawH + 18, pawW, pawH, 26, 26);
            g2.fillRoundRect(cx + 6, panelH - pawH + 18, pawW, pawH, 26, 26);
            g2.setColor(withAlpha(Color.BLACK, 30));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(cx - pawW - 6, panelH - pawH + 18, pawW, pawH, 26, 26);
            g2.drawRoundRect(cx + 6, panelH - pawH + 18, pawW, pawH, 26, 26);
        }

        private Polygon triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
            Polygon p = new Polygon();
            p.addPoint(x1, y1);
            p.addPoint(x2, y2);
            p.addPoint(x3, y3);
            return p;
        }

        private Polygon scaleTriangle(Polygon src, double scale) {
            long cx = 0, cy = 0;
            for (int i = 0; i < src.npoints; i++) {
                cx += src.xpoints[i];
                cy += src.ypoints[i];
            }
            cx /= src.npoints;
            cy /= src.npoints;
            Polygon out = new Polygon();
            for (int i = 0; i < src.npoints; i++) {
                int nx = (int) (cx + (src.xpoints[i] - cx) * scale);
                int ny = (int) (cy + (src.ypoints[i] - cy) * scale);
                out.addPoint(nx, ny);
            }
            return out;
        }

        private void drawPawPrint(Graphics2D g2, int x, int y, int size, Color color) {
            g2.setColor(color);
            int padW = size, padH = (int) (size * 0.8);
            g2.fillOval(x, y, padW, padH);
            int toe = size / 3;
            g2.fillOval(x - toe / 3, y - toe, toe, toe);
            g2.fillOval(x + size / 3 - toe / 4, y - toe - toe / 4, toe, toe);
            g2.fillOval(x + (size * 2) / 3 - toe / 3, y - toe, toe, toe);
            g2.fillOval(x + size - toe - toe / 6, y - toe / 2, toe, toe);
        }

        private Color withAlpha(Color c, int alpha) {
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new PawCafeLogin();
        });
    }
}