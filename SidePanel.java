import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SidePanel extends JTextArea {
    private JTextArea textField; // Optional reference for the text area (not used here)
    private String panelText; // Stores panel text (currently unused)
    private ArrayList<String> names; // Stores names displayed in the panel
    private static SidePanel sp; // Static reference to the SidePanel instance

    /* Constructs a SidePanel */
    public SidePanel() {
        /* Configure JTextArea properties */
        this.setFont(new Font("Poppins", Font.PLAIN, 20)); // Set font
        this.setMargin(new Insets(2, 5, 1, 1)); // Set padding/margin inside the text area
        //this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Optional border
        this.setBackground(new Color(70, 75, 82)); // Dark background color
        this.setLineWrap(true);  // Wrap long lines
        this.setWrapStyleWord(true); // Wrap at word boundaries
        this.setForeground(Color.WHITE); // Text color
        //this.setPreferredSize(new Dimension(panelWidth, panelHeight)); // Optional size

        /* Add listener to detect changes in the text area */
        this.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                // Update current class names whenever text changes
                Classes.getCurrentClass().setClassNames(getNamesFromPanel());

                // Refresh wheel display if options pane is open
                if (!(Display.getOptionsPane() == null)) {
                    Display.getWheelPanel().remove(Display.getOptionsPane());
                    Display.setOptionsPane(null);
                    Display.getWheelPanel().revalidate();
                    Display.getWheelPanel().repaint();
                }
            }

            public void removeUpdate(DocumentEvent e) {
                // Same logic when text is removed
                Classes.getCurrentClass().setClassNames(getNamesFromPanel());

                if (!(Display.getOptionsPane() == null)) {
                    Display.getWheelPanel().remove(Display.getOptionsPane());
                    Display.setOptionsPane(null);
                    Display.getWheelPanel().revalidate();
                    Display.getWheelPanel().repaint();
                }
            }

            public void insertUpdate(DocumentEvent e) {
                // Same logic when text is inserted
                Classes.getCurrentClass().setClassNames(getNamesFromPanel());

                if (!(Display.getOptionsPane() == null)) {
                    Display.getWheelPanel().remove(Display.getOptionsPane());
                    Display.setOptionsPane(null);
                    Display.getWheelPanel().revalidate();
                    Display.getWheelPanel().repaint();
                }
            }
        });

        sp = this; // Set the static reference to this instance
    }

    /* Returns all the names in the textfield as an ArrayList */
    public ArrayList<String> getNamesFromPanel() {
        names = new ArrayList<String>();
        String fieldText = this.getText(); // Get all text
        for (String s : fieldText.split("\n")) { // Split by lines
            names.add(s); // Add each line as a name
        }
        return names;
    }

    /* Sets the text in the textArea from a list of names */
    public void setNamesInPanel(ArrayList<String> list) {
        String text = "";
        for (String s : list) {
            text += s + "\n"; // Append each name with newline
        }
        this.setText(text); // Set the text in the JTextArea
    }

    /* Adds a single name to the text area */
    public void AddName(String n) {
        ArrayList<String> name = this.getNamesFromPanel(); // Get current names
        name.add(n); // Add the new name
        this.setNamesInPanel(name); // Update the panel text
    }

    /* Returns the static SidePanel instance */
    public static SidePanel getSidePanel() {
        return sp;
    }
}
