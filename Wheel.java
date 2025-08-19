import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Wheel extends JPanel {
    private static final int WHEEL_RADIUS = 300; // Radius of the wheel
    private static final int POINTER_SIZE = 30;  // Size of the top pointer triangle

    private static ArrayList<String> names; // Names on the wheel
    private ArrayList<Color> colors;         // Colors for each wheel section
    private int currentAngle = 0;            // Current rotation angle
    private Timer timer;                     // Timer for smooth spinning
    private Random random = new Random();    // Random number generator
    private String selectedName = "";        // The name selected after spin
    private boolean spinning = false;        // Is wheel spinning?
    private double speed = 15;               // Initial speed
    private double deceleration = 0.98;      // Slowdown factor per timer tick
    private boolean donespinning = false;    // Not used currently
    private Object[] options = {"Keep", "Remove"}; // Dialog options after spin
    private double pointerAngle = 90;        // Fixed pointer at 90° (top)
    Classes c2;                              // Reference to the current class
    private static boolean soundOnOff = true; // Sound toggle
    private static JButton soundButton = new JButton("Sound"); // Sound button

    /** Constructor: initializes the wheel with a class */
    public Wheel(Classes c) {
        names = c.getNames();
        c2 = c;
        generateColors(); // Generate colors for sections
        setPreferredSize(new Dimension(600, 600));
        this.setBackground(new Color(54, 58, 64));

        // Mouse click to start spinning
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                rotate();
            }
        });
    }

    /** Generates unique colors for each section based on the index */
    private void generateColors() {
        colors = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            colors.add(Color.getHSBColor((float) i / names.size(), 0.85f, 0.85f));
        }
    }

    /** Paints the wheel and pointer */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        names = c2.getNames();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int numSections = names.size();

        if (numSections == 0) return;

        double sectionAngle = 360.0 / numSections;

        // Draw each wheel section
        for (int i = 0; i < numSections; i++) {
            g2.setColor(colors.get(i));
            int startAngle = (int) (currentAngle + i * sectionAngle);
            int arcAngle = (int) sectionAngle + 1;
            g2.fillArc(centerX - WHEEL_RADIUS, centerY - WHEEL_RADIUS,
                        2 * WHEEL_RADIUS, 2 * WHEEL_RADIUS, startAngle, arcAngle);
        }

        // Draw names on the wheel
        int baseFontSize = (numSections > 20) ? Math.max(10, 16 - (numSections - 20) / 2) : 16;
        Font textFont = new Font("Arial", Font.BOLD, baseFontSize);
        g2.setFont(textFont);
        FontMetrics fm = g2.getFontMetrics();
        int textRadius = (int) (WHEEL_RADIUS * 0.6);

        for (int i = 0; i < numSections; i++) {
            double textAngleDeg = currentAngle + i * sectionAngle + sectionAngle / 2;
            double textAngleRad = Math.toRadians(textAngleDeg);

            int xPos = centerX + (int) (Math.cos(textAngleRad) * textRadius);
            int yPos = centerY - (int) (Math.sin(textAngleRad) * textRadius);

            int availableWidth = (int) (2 * Math.PI * textRadius / numSections) - 4;
            String name = names.get(i);
            String displayName = getTruncatedText(name, fm, availableWidth);

            AffineTransform old = g2.getTransform();
            g2.translate(xPos, yPos);
            g2.rotate(-textAngleRad - Math.toRadians(180));
            g2.setColor(Color.WHITE);
            int textWidth = fm.stringWidth(displayName);
            g2.drawString(displayName, -textWidth / 2, 0);
            g2.setTransform(old);
        }

        // Draw the top pointer triangle
        g2.setColor(Color.RED);
        int[] xPoints = {centerX - POINTER_SIZE / 2, centerX, centerX + POINTER_SIZE / 2};
        int[] yPoints = {centerY - WHEEL_RADIUS, centerY - WHEEL_RADIUS + 40, centerY - WHEEL_RADIUS};
        g2.fillPolygon(xPoints, yPoints, 3);
    }

    /** Truncates text if it exceeds available width */
    private String getTruncatedText(String text, FontMetrics fm, int maxWidth) {
        if (fm.stringWidth(text) <= maxWidth) return text;

        String ellipsis = "...";
        int availableWidth = maxWidth - fm.stringWidth(ellipsis);
        StringBuilder truncated = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (fm.stringWidth(truncated.toString() + c) > availableWidth) break;
            truncated.append(c);
        }
        truncated.append(ellipsis);
        return truncated.toString();
    }

    /** Starts spinning the wheel with smooth deceleration */
    public void rotate() {
        // Close options pane if open
        if (Display.getOptionsPane() != null) {
            Display.getWheelPanel().remove(Display.getOptionsPane());
            Display.setOptionsPane(null);
            Display.getWheelPanel().revalidate();
            Display.getWheelPanel().repaint();
        }

        int randomOffset = (int) (Math.random() * 4);

        if (spinning) return; // Prevent multiple spins
        spinning = true;
        speed = 20;

        timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (speed > 0.1) {
                    currentAngle += speed + randomOffset;
                    speed *= deceleration;
                    repaint();
                } else {
                    selectWinner();
                    spinning = false;
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    /** Determines winning section based on pointer angle */
    private void selectWinner() {
        int numSections = names.size();
        if (numSections == 0) return;

        double sectionAngle = 360.0 / numSections;
        double effectiveAngle = ((pointerAngle - currentAngle) % 360 + 360) % 360;
        int winningIndex = (int) (effectiveAngle / sectionAngle);

        selectedName = names.get(winningIndex);

        Jingle(); // Play sound if enabled

        int result = JOptionPane.showOptionDialog(this,
                selectedName,
                "SELECTED NAME", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (result == JOptionPane.NO_OPTION) {
            removeName(selectedName);  // Remove name from wheel
            Display.addNamebPanel(selectedName); // Add to removed panel
        }
        repaint();
    }

    /** Adds a name to the wheel if not spinning */
    public void addName(String name) {
        if (!spinning && !name.trim().isEmpty()) {
            names.add(name.trim());
            generateColors();
            repaint();
        }
    }

    /** Removes a name from the wheel and side panel */
    public void removeName(String name) {
        // Update side panel
        ArrayList<String> updatedSidePanel = Display.getSidePanel().getNamesFromPanel();
        updatedSidePanel.removeIf(n -> n.equals(name));
        Display.getSidePanel().setNamesInPanel(updatedSidePanel);

        // Remove from wheel
        if (!spinning) {
            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).equalsIgnoreCase(name)) {
                    names.remove(i);
                    Display.getSidePanel().revalidate();
                    Display.getSidePanel().repaint();
                    generateColors();
                    repaint();
                    break;
                }
            }
        }
    }

    /** Get all names on wheel */
    public static ArrayList<String> getNames() {
        return names;
    }

    /** Set the wheel's names */
    public void setNames(ArrayList<String> jaBooty) {
        names = jaBooty;
        generateColors();
        repaint();
    }

    /** Repaint wheel */
    public void updateWheel() {
        repaint();
    }

    /** Check if wheel is spinning */
    public boolean wheelIsSpinning() {
        return spinning;
    }

    /** Restore a removed name */
    public void restoreName(String name) {
        Display.removeNamebPanel(name);
    }

    /** Play sound if enabled */
    public void Jingle() {
        if (soundOnOff) new TaDa(); // TaDa class plays the jingle
    }

    /** Toggle sound on/off */
    private static void turnSoundOnOff() {
        if (soundOnOff) {
            soundOnOff = false;
            soundButton.setBackground(Color.red);
        } else {
            soundOnOff = true;
            soundButton.setBackground(Color.green);
        }
    }

    /** Returns a button to toggle sound */
    public static JButton makeSoundButton() {
        soundButton.setBackground(soundOnOff ? Color.green : Color.red);
        soundButton.addActionListener(e -> turnSoundOnOff());
        return soundButton;
    }
}
