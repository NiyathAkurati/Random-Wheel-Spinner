import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class Runner {
    // Reference to the GUI display
    private static Display gui;

    public static void main (String[] args) {
        // Initialize the main GUI
        gui = new Display();

        // Perform any startup tasks like loading files
        Filemanager.onStart();

        // Add a shutdown hook to clean up temporary files when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Spinner is shutting down. Performing cleanup...");
            
            // Check if there are any paths marked for deletion
            if(Control_Panel.getPathsToBeDeleted().size() > 0) {
                for(int x = 0; x < Control_Panel.getPathsToBeDeleted().size(); x++) {
                    try {
                        // Suggest garbage collection before deleting files
                        System.gc();
                        Thread.sleep(2000); // Wait a bit before deleting

                        // Delete the file if it exists
                        Files.deleteIfExists(Control_Panel.getPathsToBeDeleted().get(x));
                    } catch (IOException | InterruptedException e) {
                        // Print stack trace in case of error
                        ((Throwable) e).printStackTrace();
                    }
                }
            }
        }));
    }

    // Getter for the GUI reference
    public static Display getGui() {
        return gui;
    }
}
