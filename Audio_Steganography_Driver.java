/*
 * 
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
import classes.JWav;

/**
 * @author Larcade
 * Description: Audio_Steganography_Driver will utilize the JWav class to prompt the user
 *              for a .wav file, read in the .wav file, and create an identical .wav file with 
 *              an injected and encrypted message. It will give the user the option to play
 *              the original and newly created .wav files. It will allow the user to select
 *              new .wav files to read in and to create new .wav files from.
 */
public class Audio_Steganography_Driver {
	private static void createAndShowGUI() {
		/**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    //Create and set up the window.
	    JFrame frame = new JFrame("Audio_Steganography");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // Create all panels
	    JPanel mainPanel = new JPanel();        // Main panel where all content will be contained
		JPanel encryptPanel = new JPanel();     // All encrypt content will be here
		JPanel encryptSubPanel1 = new JPanel(); // First subcontainer inside encryptPanel
		JPanel encryptSubPanel2 = new JPanel(); // Second subcontainer inside encryptPanel
		JPanel encryptSubPanel3 = new JPanel(); // Third subcontainer inside encryptPanel
		JPanel decryptPanel = new JPanel();     // All decrypt content will be here
		JPanel decryptSubPanel1 = new JPanel(); // First subcontainer inside decryptPanel
		JPanel decryptSubPanel2 = new JPanel(); // Second subcontainer inside decryptPanel
		JPanel decryptSubPanel3 = new JPanel(); // Third subcontainer inside decryptPanel
		
		// Customize layout for all panels
		encryptPanel.setLayout(new BoxLayout(encryptPanel, BoxLayout.PAGE_AXIS));
		decryptPanel.setLayout(new BoxLayout(decryptPanel, BoxLayout.PAGE_AXIS));
		
		/*
		 *  Encrypt GUI - Section
		 */
		// Create content for encryptSubPanel1
		JLabel encryptLabel = new JLabel("Encrypt");
		encryptLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		encryptLabel.setForeground(new Color(0x4198d9));
		
		// Add sub-content to encryptSubPanel1
		encryptSubPanel1.add(encryptLabel);

		// Create content for encryptSubPanel2
		// TO-DO -> CREATE A JFileChooser HERE
		
		JPasswordField encryptPassword = new JPasswordField(10); // password field
		
		JButton encryptRunButton = new JButton("<html><b>GO!</b></html>"); // GO! button
		
		// Add sub-content to encryptSubPanel2
		// TO-DO -> ADD THE JFileChooser HERE
		encryptSubPanel2.add(encryptPassword);
		encryptSubPanel2.add(encryptRunButton);
		
		// Create sub-content for encryptSubPanel3
		// TO-DO -> CREATE A SOUND PLAYER HERE
		
		JTextArea encryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		encryptSecretMessage.setLineWrap(true);
		encryptSecretMessage.setWrapStyleWord(true);
		
		// Add sub-content to encryptSubPanel3
		// TO-DO -> ADD THE SOUND PLAYER HERE
		encryptSubPanel3.add(encryptSecretMessage);
		
		// Add all encryptSubPanel's to encryptPanel
		encryptPanel.add(encryptSubPanel1);
		encryptPanel.add(encryptSubPanel2);
		encryptPanel.add(encryptSubPanel3);
		
		// Add encryptPanel to mainPanel
		mainPanel.add(encryptPanel);
		
		/*
		 *  Decrypt GUI - Section
		 */
		// Create sub-content for decryptSubPanel1
		JLabel decryptLabel = new JLabel("Decrypt");
		decryptLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		decryptLabel.setForeground(new Color(0x4198d9));
		
		// Add sub-content to decryptSubPanel1
		decryptSubPanel1.add(decryptLabel); // "Decrypt" label
		
		// Create sub-content for decryptSubPanel2
		// TO-DO -> CREATE THE JFileChooser HERE
		
		JPasswordField decryptPassword = new JPasswordField(10); // password field
		
		JButton decryptRunButton = new JButton("<html><b>GO!</b></html>"); // GO! button
		
		// Add sub-content to decryptSubPanel2
		// TO-DO -> ADD THE JFileChooser HERE
		decryptSubPanel2.add(decryptPassword);
		decryptSubPanel2.add(decryptRunButton);
		
		// Create sub-content for decryptSubPanel3
		// TO-DO -> CREATE A SOUND PLAYER HERE
		
		JTextArea decryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		decryptSecretMessage.setLineWrap(true);
		decryptSecretMessage.setWrapStyleWord(true);
		
		// Add sub-content to decryptSubPanel3
		// TO-DO -> ADD SOUND PLAYER HERE
		decryptSubPanel3.add(decryptSecretMessage);
		
		// Add all decryptSubPanel's to decryptPanel
		decryptPanel.add(decryptSubPanel1);
		decryptPanel.add(decryptSubPanel2);
		decryptPanel.add(decryptSubPanel3);
		
		// Add decryptPanel to mainPanel
		mainPanel.add(decryptPanel);
		
        // Add all content to window
		frame.add(mainPanel);
        
	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
		JWav wav = new JWav("this will be a filepath");
		
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
}
