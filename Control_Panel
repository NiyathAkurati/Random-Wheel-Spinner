import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.text.JTextComponent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Control_Panel {
    private static JComboBox<String> laBox = new JComboBox<String>(); // ComboBox for selecting classes
    private static JButton optionsButton; // Options button
    private static final int wheelPanelSideLength = 740; // Size for wheel panel
    private static final int buttonsPanelHeightLength = 40; // Height for buttons panel
    private static ArrayList<Path> pathsToBeDeleted = new ArrayList<Path>(); // List of file paths to delete on exit

    /**
     * Make the options button and its JPanel
     */
    public static JButton makeOptionsButton() {
        optionsButton = new JButton("Options"); // Create button
        optionsButton.setFont((new Font("Poppins", Font.BOLD, 15)));
        optionsButton.setBackground(new Color(67, 98, 151));

        /* Drop down the Panel on click */
        optionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Only show options if wheel is not spinning
                if (!Display.getActiveWheel().wheelIsSpinning()) {
                    if (Display.getOptionsPane() == null) {
                        Runner.getGui().openOptionsPane(); // Open options panel
                    } else {
                        Display.getWheelPanel().remove(Display.getOptionsPane()); // Remove if already open
                        Display.setOptionsPane(null);
                    }
                    Display.getWheelPanel().revalidate(); // Refresh UI
                    Display.getWheelPanel().repaint();
                }
            }
        });
        return optionsButton;
    }

    /**
     * Make Option-Dropdown buttons
     */
    public static JButton makeNewClassButton() {
        JButton classButton = new JButton("<html>New<br>Class</html>");
        classButton.setBackground(new Color(163, 195, 250));

        // Action to create a new class
        classButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Classes(); // Create default new class
                Display.updateDisplay(); // Refresh display
            }
        });
        return classButton;
    }

    /**
     * Returns a JButton that can delete the current class
     */
    public static JButton makeRemoveClassButton() {
        JButton removeClassButton = new JButton("<html>Delete<br>Class</html>");
        removeClassButton.setBackground(new Color(163, 195, 250));

        removeClassButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Classes.getClassesList().size() > 1) { // Only delete if more than 1 class exists
                    for (int i = 0; i < Classes.getClassesList().size(); i++) {
                        if (Classes.getClassesList().get(i) == Classes.getCurrentClass()) {

                            /* Remove Class from List and set new Current Class */
                            Classes.getClassesList().remove(i);

                            /* Remove the Class Name from the ClassesNameList */
                            for (int c = 0; c < Classes.getClassesNameList().size(); c++) {
                                if (Classes.getClassesNameList().get(c).equals(Classes.getCurrentClass().getClassName())) {
                                    Classes.getClassesNameList().remove(c);
                                }
                            }

                            Classes.setCurrentClass(Classes.getClassesList().get(0)); // Set first class as current

                            /* Remove Class from ComboBox */
                            laBox.removeItemAt(laBox.getSelectedIndex());
                            laBox.setSelectedIndex(0);

                            Display.updateDisplay(); // Refresh UI
                        }
                    }
                }
            }
        });
        return removeClassButton;
    }

    /**
     * Returns a button to mark a file for deletion
     */
    public static JButton makeUnsavesButton() {
        JButton UnsaveButton = new JButton("Delete File");
        UnsaveButton.setBackground(new Color(163, 195, 250));

        UnsaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame deleteChoosenFilePanel = new JFrame("panel"); // Temporary frame
                final JFileChooser dc = new JFileChooser(FileManager.getStartingSaveFolder()); // File chooser starting in save folder
                dc.setApproveButtonText("Delete File");

                // File filter to select files
                FileFilter filter = new FileFilter() {
                    public boolean accept(File file) {
                        return FileManager.getStartingSaveFolder() == file;
                    }

                    public String getDescription() {
                        return "select files you wish to delete";
                    }
                };

                //dc.setFileFilter(filter); // Commented out
                dc.setAcceptAllFileFilterUsed(false); // Disable all files option
                dc.showOpenDialog(deleteChoosenFilePanel); // Show chooser

                if (dc.getSelectedFile() != null) {
                    Path filePath = Paths.get(dc.getSelectedFile().getPath());
                    // Ensure file is inside savedClasses folder
                    if (dc.getSelectedFile().getPath().indexOf("savedClasses") != -1) {
                        pathsToBeDeleted.add(filePath); // Add to delete list
                        JOptionPane.showMessageDialog(null, "Your file will be deleted after you close the program", "Your wheel thanks you", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please choose another file", "Your wheel thanks you", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        return UnsaveButton;
    }

    /**
     * Return the load button that calls the FileManager.load() method
     */
    public static JButton makeLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setFont((new Font("Poppins", Font.BOLD, 15)));
        loadButton.setBackground(new Color(67, 98, 151));

        /* ActionListener */
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileManager.load(); // Call load method
            }
        });
        return loadButton;
    }

    /**
     * Return a button to save the current class
     */
    public static JButton makeSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.setFont((new Font("Poppins", Font.BOLD, 15)));
        saveButton.setBackground(new Color(67, 98, 151));

        /* ActionsListener */
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileManager.save(); // Call save method
            }
        });
        return saveButton;
    }

    /**
     * Makes the ComboBox for the Classes drop down
     */
    public static JComboBox<String> makeClassesBox() {
        laBox.setEditable(true); // Allow editing
        laBox.setFont((new Font("Poppins", Font.BOLD, 15)));
        laBox.getEditor().getEditorComponent().setBackground(new Color(163, 195, 250));
        laBox.setBackground(new Color(61, 92, 146));
        laBox.setBorder(BorderFactory.createCompoundBorder());
        ((JComponent) laBox.getEditor().getEditorComponent()).setBorder(BorderFactory.createCompoundBorder());

        /* Change class Name on typing */
        laBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                changeComboBoxName();
            }

            public void keyReleased(KeyEvent e) {
                changeComboBoxName();
            }
        });

        /* Change Current Class on selection */
        laBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!(Display.getActiveWheel().wheelIsSpinning())) {
                    if (!(null == (Classes.getClassFromName(laBox.getEditor().getItem().toString())))) {
                        Classes.setCurrentClass(Classes.getClassFromName(laBox.getEditor().getItem().toString()));
                        Display.updateDisplay(); // Refresh display
                    }
                }
            }
        });

        return laBox;
    }

    /* Add a new class to the ComboBox */
    public static void addClassToBox(Classes c) {
        laBox.addItem(c.getClassName());
    }

    /* Getter for laBox */
    public static JComboBox<String> getLaBox() {
        return laBox;
    }

    /* When the laBox is edited, update the text and ComboBox */
    public static void changeComboBoxName() {
        String changedItem = laBox.getEditor().getItem().toString();
        Classes.getCurrentClass().setClassName(changedItem); // Update current class name
        changedItem = Classes.getCurrentClass().getClassName();

        /* Preserve cursor position */
        JTextComponent editorComponent = (JTextComponent) laBox.getEditor().getEditorComponent();
        int initialCursorPos = editorComponent.getCaretPosition();

        /* Remove old item and insert new name */
        int selectedIndex = laBox.getSelectedIndex();
        laBox.removeItemAt(laBox.getSelectedIndex());
        laBox.insertItemAt(changedItem, selectedIndex);
        laBox.setSelectedIndex(selectedIndex);
        editorComponent.setCaretPosition(initialCursorPos);
    }

    /* Getter for pathsToBeDeleted */
    public static ArrayList<Path> getPathsToBeDeleted() {
        return pathsToBeDeleted;
    }
}
