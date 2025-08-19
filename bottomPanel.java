import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class bottomPanel extends JPanel {
    private ArrayList<String> names = new ArrayList<>(); // Stores removed names for the current class
    private JPanel namesPanel; // Panel holding buttons for removed names
    private JButton restoreAllButton; // Button to restore all removed names
    private JScrollPane scrollPane; // Scroll pane for namesPanel
    private Classes curClass; // Reference to the current class

    /* Constructor for the bottomPanel */
    public bottomPanel(int panelWidth, int panelHeight) {
        this.setPreferredSize(new Dimension(panelWidth, panelHeight)); // Set panel size
        this.setBackground(new Color(66, 72, 79)); // Dark background color
        this.setLayout(new BorderLayout()); // Use BorderLayout for main panel
        //this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Optional border

        /* Top panel with Restore All button */
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Flow layout left-aligned
        topPanel.setOpaque(false); // Make top panel transparent
        restoreAllButton = new JButton("Restore All"); // Initialize button
        restoreAllButton.setBackground(new Color(86, 117, 170)); // Set button color
        restoreAllButton.addActionListener(e -> restoreAllNames()); // Add action listener
        topPanel.add(restoreAllButton); // Add button to top panel
        this.add(topPanel, BorderLayout.NORTH); // Add top panel to main panel

        /* Panel to display removed names as buttons */
        namesPanel = new JPanel();
        namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS)); // Vertical layout
        namesPanel.setBackground(new Color(66, 72, 79)); // Match background
        namesPanel.setOpaque(true); 
        scrollPane = new JScrollPane(namesPanel); // Scroll pane for removed names
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(66, 72, 79)); // Set viewport color
        this.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to center
    }

    /* Adds a removed name to the bottom panel */
    public void addRemovedName(String n) {
        curClass = Classes.getCurrentClass();
        curClass.putIfAbsent(new ArrayList<String>()); // Ensure removedNames exists
        names = curClass.getRemovedNames();
        // Add the name to the list and create a button
        names.add(n);
        JButton nameButton = createButton(n);
        nameButton.setBackground(new Color(163, 195, 250));
        nameButton.addActionListener(e -> Display.getActiveWheel().restoreName(n)); // Restore name on click
        namesPanel.add(nameButton);
        updateUIComponents(); // Refresh panel
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum()); // Scroll to bottom
        });
        curClass.setRemovedNames(names);
        names = null; // Clear local reference
    }

    /* Removes a removed name from the panel */
    public void removeRemovedName(String n) {
        names = curClass.getRemovedNames();
        if ((curClass == null) && !names.contains(n)) {
            return;
        }
        if (names != null) {
            SidePanel.getSidePanel().AddName(n); // Add back to side panel
            Component[] components = namesPanel.getComponents();
            for (Component c : components) {
                if (c instanceof JButton && ((JButton) c).getText().equals(n)) {
                    names.remove(n); // Remove from removedNames list
                    namesPanel.remove(c); // Remove button
                    Display.getSidePanel().revalidate();
                    Display.getSidePanel().repaint();
                    Display.updateDisplay(); // Refresh display
                    break;
                }
            }
            curClass.setRemovedNames(names); // Update removedNames
            updateUIComponents(); // Refresh panel
            names = null;
        }
    }

    /* Helper method to create a button with a given name */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(163, 195, 250)); // Set default button color
        //button.setAlignmentX(Component.LEFT_ALIGNMENT); // Optional alignment
        return button;
    }

    /* Refresh UI components */
    private void updateUIComponents() {
        namesPanel.revalidate();
        namesPanel.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    /* Restore all removed names back to the side panel */
    private void restoreAllNames() {
        names = curClass.getRemovedNames();
        if (names.size() != 0) {
            for (int i = 0; i < names.size(); i++) {
                SidePanel.getSidePanel().AddName(names.get(i)); // Add back all names
            }
            curClass.setRemovedNames(null); // Clear removed names
            names.clear();
            namesPanel.removeAll(); // Remove all buttons
            updateUIComponents();
        }
    }

    /* Set removed names for a given class and display them as buttons */
    public void setRemovedName(Classes c) {
        curClass = c;
        curClass.putIfAbsent(new ArrayList<String>()); // Ensure removedNames exists
        names = c.getRemovedNames();
        namesPanel.removeAll(); // Clear existing buttons
        for (int i = 0; i < names.size(); i++) {
            String n = names.get(i);
            JButton nameButton = createButton(n);
            nameButton.addActionListener(e -> Display.getActiveWheel().restoreName(n)); // Restore name
            namesPanel.add(nameButton);
            SwingUtilities.invokeLater(() -> {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getMaximum()); // Scroll to bottom
            });
        }
        updateUIComponents();
        names = null;
    }
}
