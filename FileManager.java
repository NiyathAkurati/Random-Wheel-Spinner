import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {
    private static JFrame fileChooserPanel = new JFrame("panel"); // Panel for file chooser dialogs
    private static File startingSaveFolder; // Folder where saved classes are stored
    private static ArrayList<String> newRemovedNames = new ArrayList<String>(); // Temporary store for removed names

    // Load a class from a file chosen by the user
    public static void load() {
        ArrayList<String> namesFromFile = new ArrayList<String>(); // Stores all names loaded from CSV
        String loadedClassName = "default"; // Default name if no file loaded
        final JFileChooser fc = new JFileChooser(); // File chooser window

        // File filter to allow only CSV files
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "csv's please"; // Description shown in file chooser
            }
        };

        fc.setFileFilter(filter); // Apply the filter
        fc.setAcceptAllFileFilterUsed(false); // Disable other file types
        fc.showOpenDialog(fileChooserPanel); // Show the dialog

        try {
            // Open the selected file for reading
            Scanner reader = new Scanner(fc.getSelectedFile());
            int emptyCount = 0; // Counter for empty lines

            while (reader.hasNextLine() || !(reader.hasNextLine()) && emptyCount < 5) {
                if (reader.hasNextLine()) {
                    String[] temp = reader.nextLine().split(","); // Split CSV line
                    String x = temp[2].substring(1, temp[2].length()).trim(); // Extract first name
                    int y = x.indexOf("\"");
                    x = x.substring(0, y).trim() + " " + x.substring(y + 1).trim(); // Remove quotes
                    x = x + temp[1].substring(1).trim(); // Combine with last name
                    namesFromFile.add(x.trim()); // Add full name to list
                    loadedClassName = fc.getSelectedFile().getName(); // Set loaded class name
                    try {
                        newRemovedNames.add(temp[3]); // Add removed name if exists
                    } catch (Exception e) {}
                }
                if (!reader.hasNextLine()) {
                    emptyCount++; // Increment empty line count
                }
            }

        } catch (Exception e) {
            System.out.println(e); // Print error if file cannot be read
        }

        if (loadedClassName != "default") { // If a file was loaded
            Classes c = new Classes(loadedClassName, namesFromFile); // Create a new class
            Classes.setCurrentClass(c); // Set it as current
            Display.setActiveWheel(Classes.getCurrentClass().getClassWheel()); // Update wheel
            Classes.getCurrentClass().setRemovedNames(newRemovedNames); // Update removed names
            Display.getBottomPanel().setRemovedName(Classes.getCurrentClass()); // Update bottom panel
            Display.getSidePanel().setNamesInPanel(Classes.getCurrentClass().getNames()); // Update side panel
            newRemovedNames = new ArrayList<String>(); // Reset temporary list
        }
    }

    // Load a class from a specific file
    public static void load(File f) {
        ArrayList<String> namesFromFile = new ArrayList<String>();
        String loadedClassName = "default";

        try {
            Scanner reader = new Scanner(f); // Open file for reading
            while (reader.hasNextLine()) {
                String[] temp = reader.nextLine().split(","); // Split CSV line
                String x = temp[2].substring(1, temp[2].length()).trim(); // Extract first name

                if (x.indexOf("*") == -1) { // Skip removed names
                    int y = x.indexOf("\"");
                    x = x.substring(0, y).trim() + " " + x.substring(y + 1).trim(); // Remove quotes
                    x = x + temp[1].substring(1).trim(); // Combine with last name
                    if (x.indexOf("*") == -1) {
                        namesFromFile.add(x.trim()); // Add full name to list
                        loadedClassName = f.getName(); // Set class name
                    }
                }
                try {
                    if (!temp[3].equals("*")) {
                        newRemovedNames.add(temp[3]); // Add removed names
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        } catch (Exception e) {
            System.out.println("NOOOOOOOOO"); // Error reading file
        }

        if (loadedClassName != "default") { // If file loaded successfully
            Classes c = new Classes(loadedClassName, namesFromFile); // Create new class
            Classes.setCurrentClass(c); // Set as current
            Classes.getCurrentClass().setRemovedNames(newRemovedNames); // Set removed names
            Display.getBottomPanel().setRemovedName(Classes.getCurrentClass()); // Update bottom panel
            newRemovedNames = new ArrayList<String>(); // Reset temporary list
        }
    }

    // Save current class to file
    public static void save() {
        boolean doesFileExist = false;
        boolean shouldSave = true;
        ArrayList<String> rNames = new ArrayList<String>();

        if (Classes.getCurrentClass().getRemovedNames() != null) {
            rNames = Classes.getCurrentClass().getRemovedNames(); // Get removed names
        } else {
            rNames.add("*"); // If none, mark as empty
        }

        String savedClassName = Classes.getCurrentClass().getClassName(); // Get class name
        File savedFile = new File(startingSaveFolder.getAbsolutePath() + "\\" + savedClassName); // File path
        Path savedFilePath = Paths.get(savedFile.getPath());

        // Check if file already exists
        for (int x = 0; x < Classes.getClassesNameList().size(); x++) {
            if (Classes.getClassesNameList().get(x).equals(savedClassName)) {
                doesFileExist = true;
            }
        }

        // Check if file is set to be deleted
        for (int x = 0; x < Control_Panel.getPathsToBeDeleted().size(); x++) {
            if (savedFilePath.equals(Control_Panel.getPathsToBeDeleted().get(x))) {
                shouldSave = false;
            }
        }

        if (shouldSave) { // Only save if allowed
            ArrayList<String> classBeingSaved = Classes.getClassFromName(savedClassName).getNames();

            try {
                FileWriter w = new FileWriter(savedFile);
                BufferedWriter myWriter = new BufferedWriter(w); // Writer to write data to file
                int loop = 0;

                if (Classes.getClassFromName(savedClassName).getNames().size() > rNames.size()) {
                    loop = Classes.getClassFromName(savedClassName).getNames().size();
                } else {
                    loop = rNames.size();
                }

                for (int x = 0; x < loop; x++) {
                    // Fill missing removed names with "*"
                    if (x + 1 > rNames.size() && rNames != null) {
                        rNames.add(rNames.size(), "*");
                    }
                    // Fill missing class names with "*"
                    if (x + 1 > classBeingSaved.size() && classBeingSaved != null) {
                        classBeingSaved.add(classBeingSaved.size(), "*");
                    }

                    // Write each line in CSV format
                    myWriter.write("1,\"" + classBeingSaved.get(x).substring(classBeingSaved.get(x).indexOf(" ") + 1) + ", "
                            + classBeingSaved.get(x).substring(0, classBeingSaved.get(x).indexOf(" ") + 1) + "\"" + "," + rNames.get(x));
                    myWriter.newLine(); // Move to next line
                }
                myWriter.close();

                JOptionPane.showMessageDialog(null, "Saved Successfully", "Your wheel thanks you",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (shouldSave = false) {
            JOptionPane.showMessageDialog(null, "This file is set to be deleted", "Your wheel thanks you",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Called on program start to load saved classes automatically
    public static void onStart() {
        if (startingSaveFolder == null) {
            String folderName = "savedClasses";
            startingSaveFolder = new File(folderName); // Default folder
        }
        if (!startingSaveFolder.exists()) {
            String folderName = "savedClasses";
            startingSaveFolder = new File(folderName); // Create folder if missing
        }

        // Load all CSV files in folder
        if (startingSaveFolder != null && startingSaveFolder.listFiles() != null) {
            File[] listOfFiles = startingSaveFolder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    FileManager.load(file); // Load each file
                }
            }
        }
    }

    // Get the folder used for saving classes
    public static File getStartingSaveFolder() {
        return startingSaveFolder;
    }
}
