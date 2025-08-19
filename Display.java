import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Display extends JLayeredPane {
    /* Class Variables */
    private JFrame frame; // Main application frame
    private static JLayeredPane wheelPanel; // Panel containing the spinning wheel
    private static JPanel buttonsPanel; // Panel for control buttons at top
    private static JTextArea nameListPanel; // Text area to show list of names
    private GridBagConstraints formatConstraints; // Layout helper
    private GridBagLayout layout; // Layout manager for complex layouts
    private static Wheel activeWheel; // Currently active wheel
    private static SidePanel sideP; // Panel on the side showing names
    private static JPanel optionsPane; // Panel for dropdown options
    private static JPanel optionsButtonPanel; // Panel for option buttons
    private static JPanel eastPanel; // Panel on the right side
    private static bottomPanel bPanel; // Panel at bottom for removed names or info
    private static JScrollPane bottomScrollPane; // Scroll pane for bottom panel

    /* Final Variables */
    private final Color frameBackgroundColor = new Color(54, 58, 64); // Background color
    private final int frameWidth = 1100; // Width of the frame
    private final int frameHeight = 780; // Height of the frame
    private final int wheelPanelSideLength = 740; // Size of wheel panel
    private final int buttonsPanelHeightLength = 40; // Height of buttons panel
    private final int nameListHeight = 780; // Height of side name list
    private final int bottomPanelHeight = 180; // Height of bottom panel

    /**
     * Constructs the GUI
     */
    public Display()  {

        /* Initialize Components */
        this.frame = new JFrame("Wheel of Names");
        wheelPanel = new JLayeredPane();
        this.buttonsPanel = new JPanel();
        sideP = new SidePanel();
        bPanel = new bottomPanel(this.frameWidth - this.wheelPanelSideLength - 15, this.bottomPanelHeight);
        sideP.setAutoscrolls(true);
        this.setLayout(null);

        /* WheelPanel */
        wheelPanel.setLayout(new BorderLayout());
        wheelPanel.setBackground(frameBackgroundColor);
        wheelPanel.setPreferredSize(new Dimension(this.wheelPanelSideLength, this.wheelPanelSideLength));
        JPanel wheelNorthBuffer = new JPanel(); // Invisible buffer at top of wheel
        wheelNorthBuffer.setPreferredSize(new Dimension(100, 40));
        wheelNorthBuffer.setVisible(false);

        /* Makes the starting Class */
        Classes.setCurrentClass(new Classes()); // Set initial class
        Display.getSidePanel().setNamesInPanel(Classes.getCurrentClass().getNames()); // Show names
        activeWheel = Classes.getCurrentClass().getClassWheel(); // Load wheel for current class
        wheelPanel.add(wheelNorthBuffer, BorderLayout.NORTH);
        wheelPanel.add(activeWheel);

        /* Formatting for combining panels */
        JPanel buttonPanelFormat = new JPanel();
        buttonPanelFormat.setLayout(new BorderLayout());
        buttonPanelFormat.add(buttonsPanel, BorderLayout.NORTH);
        buttonPanelFormat.add(wheelPanel);
        this.setLayout(new BorderLayout());

        /* Buttons Panel */
        this.buttonsPanel.setBackground(Color.BLUE);
        this.buttonsPanel.setPreferredSize(new Dimension(this.wheelPanelSideLength, this.buttonsPanelHeightLength));
        this.buttonsPanel.setLayout(new GridLayout(1, 4));

        /* BottomPanel */
        JScrollPane nameListScrollPane = new JScrollPane(sideP);
        nameListScrollPane.setWheelScrollingEnabled(true);
        nameListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        nameListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        nameListScrollPane.setPreferredSize(new Dimension(frameWidth - wheelPanelSideLength - 15, nameListHeight));
        nameListScrollPane.setBorder(BorderFactory.createEmptyBorder());
        bPanel.setBackground(new Color(66, 72, 79));
        bottomScrollPane = new JScrollPane(bPanel);
        bottomScrollPane.setWheelScrollingEnabled(true);
        bottomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bottomScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bottomScrollPane.setPreferredSize(new Dimension(frameWidth - wheelPanelSideLength - 15, bottomPanelHeight));
        bottomScrollPane.setBackground(new Color(70, 75, 82));
        bottomScrollPane.setBorder(BorderFactory.createEmptyBorder());

        /* East Panel (Right Side) */
        eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.setPreferredSize(new Dimension(frameWidth - wheelPanelSideLength - 15, frameHeight));
        eastPanel.add(nameListScrollPane, BorderLayout.CENTER);
        eastPanel.add(bottomScrollPane, BorderLayout.SOUTH);
        this.setLayout(new BorderLayout());
        this.add(eastPanel, BorderLayout.EAST);
        this.add(buttonPanelFormat, BorderLayout.WEST);

        /* Panel Buttons */
        this.buttonsPanel.add(Control_Panel.makeOptionsButton());
        this.buttonsPanel.add(Control_Panel.makeClassesBox());
        this.buttonsPanel.add(Control_Panel.makeSaveButton());
        this.buttonsPanel.add(Control_Panel.makeLoadButton());

        /* Frame Icon */
        ImageIcon icon = new ImageIcon("potato.png");
        frame.setIconImage(icon.getImage());

        /* Frame Settings */
        this.setBackground(frameBackgroundColor);
        this.frame.setSize(frameWidth, frameHeight);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);

        /* Add to Frame */
        frame.add(this);
    }

    /* Updates the active wheel panel in a thread-safe way */
    public static void setActiveWheel(Wheel w) {
        SwingUtilities.invokeLater(() -> {
            wheelPanel.remove(activeWheel);
            activeWheel = w;
            wheelPanel.add(activeWheel);
            activeWheel.updateWheel();
            wheelPanel.revalidate();
            wheelPanel.repaint();
        });
    }

    public static Wheel getActiveWheel() {
        return activeWheel;
    }

    public static JPanel getOptionsPane() {
        return optionsPane;
    }

    public static void setOptionsPane(JPanel s) {
        optionsPane = s;
    }

    public static SidePanel getSidePanel() {
        return sideP;
    }

    /* Update the wheel and side panel when class changes */
    public static void updateDisplay() {
        setActiveWheel(Classes.getCurrentClass().getClassWheel());
        getSidePanel().setNamesInPanel(Classes.getCurrentClass().getNames());
        bottomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bPanel.setRemovedName(Classes.getCurrentClass());
        try {
            Display.getOptionsPane().revalidate();
            Display.getOptionsPane().repaint();
        } catch (Exception q) {
            // Ignore if optionsPane is null
        }
    }

    public static JLayeredPane getWheelPanel() {
        return wheelPanel;
    }

    public static JPanel getButtonsPanel() {
        return buttonsPanel;
    }

    /* Methods to add/remove names in bottom panel */
    public static void addNamebPanel(String name) {
        bPanel.addRemovedName(name);
    }

    public static void removeNamebPanel(String name) {
        bPanel.removeRemovedName(name);
    }

    /* Opens the options panel/dropdown */
    public void openOptionsPane() {
        /* Create the dropdown */
        optionsPane = new JPanel();
        optionsPane.setBounds(0, 0, (int)(this.wheelPanelSideLength / 4), 100);
        optionsPane.setLayout(new GridLayout(2, 2));

        /* Options Buttons */
        optionsPane.add(Control_Panel.makeNewClassButton());
        optionsPane.add(Control_Panel.makeRemoveClassButton());
        optionsPane.add(Wheel.makeSoundButton());
        optionsPane.add(Control_Panel.makeUnsavesButton());

        /* Add to wheel panel */
        try {
            wheelPanel.add(optionsPane, JLayeredPane.MODAL_LAYER);
        } catch (Exception e) {
            // Ignore exceptions
        }
        this.revalidate();
        this.repaint();
    }

    public static bottomPanel getBottomPanel() {
        return bPanel;
    }
}
