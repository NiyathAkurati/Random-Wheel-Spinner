import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.FlowLayout;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

// TaDa class extends JFrame to allow audio playback in a Swing context
public class TaDa extends JFrame {

    // Constructor for TaDa
    public TaDa() {
        try {
            // Load audio file from resources folder
            URL url = this.getClass().getClassLoader().getResource("win31.wav");
            
            // Create an AudioInputStream from the URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            
            // Get a Clip object to play the sound
            Clip clip = AudioSystem.getClip();
            
            // Open the clip with the audio stream
            clip.open(audioIn);
            
            // Start playing the audio clip
            clip.start();
        } 
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace(); // Handle unsupported audio file
        } 
        catch (IOException e) {
            e.printStackTrace(); // Handle input/output exceptions
        } 
        catch (LineUnavailableException e) {
            e.printStackTrace(); // Handle unavailable audio line
        }
    }

    // Main method to run the TaDa application
    public static void main(String[] args) {
        new TaDa(); // Instantiate TaDa to play the sound
    }
}
