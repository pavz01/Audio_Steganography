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
import javax.sound.sampled.*;

/**
 * @author Larcade
 * 
 *         Description: Audio_Steganography_Driver will utilize the JWav
 *         class to prompt the user for a .wav file, read in the .wav file, and
 *         create an identical .wav file with an injected and encrypted message.
 *         It will give the user the option to play the original and newly
 *         created .wav files. It will allow the user to select new .wav files
 *         to read in and to create new .wav files from.
 */
public class Audio_Steganography_Driver extends JPanel
                                        implements ActionListener {
	JPanel encryptPanel, encryptSubPanel1, encryptSubPanel2, encryptSubPanel3, encryptSubPanel4, encryptSubPanel5;
	JPanel decryptPanel, decryptSubPanel1, decryptSubPanel2, decryptSubPanel3, decryptSubPanel4, decryptSubPanel5;
	JLabel encryptLabel, decryptLabel, encryptPasswordLabel, decryptPasswordLabel;
	JButton encryptFCButton, decryptFCButton, encryptRunButton, decryptRunButton;
	JButton encryptPlayButton, encryptStopButton, decryptPlayButton, decryptStopButton;
	JPasswordField encryptPassword, decryptPassword;
	JTextArea encryptSecretMessage, decryptSecretMessage;
	JFileChooser fc;
	File encryptFile, decryptFile;

	public Audio_Steganography_Driver() {
		fc = new JFileChooser();
		
		// Create all panels
		encryptPanel = new JPanel();     // All encrypt content will be here
		encryptSubPanel1 = new JPanel(); // First subcontainer inside encryptPanel
		encryptSubPanel2 = new JPanel(); // Second subcontainer inside encryptPanel
		encryptSubPanel3 = new JPanel(); // Third subcontainer inside encryptPanel
		encryptSubPanel4 = new JPanel(); // Fourth subcontainer inside encryptPanel
		encryptSubPanel5 = new JPanel(); // Fifth subcontainer inside encryptPanel
		decryptPanel = new JPanel();     // All decrypt content will be here
		decryptSubPanel1 = new JPanel(); // First subcontainer inside decryptPanel
		decryptSubPanel2 = new JPanel(); // Second subcontainer inside decryptPanel
		decryptSubPanel3 = new JPanel(); // Third subcontainer inside decryptPanel
		decryptSubPanel4 = new JPanel(); // Fourth subcontainer inside decryptPanel
		decryptSubPanel5 = new JPanel(); // Fifth subcontainer inside decryptPanel

		// Customize layout for all panels
		encryptPanel.setLayout(new BoxLayout(encryptPanel, BoxLayout.PAGE_AXIS));
		decryptPanel.setLayout(new BoxLayout(decryptPanel, BoxLayout.PAGE_AXIS));

		/*
		 * Encrypt GUI - Section
		 */
		// Create "Encrypt" label
		encryptLabel = new JLabel("Encrypt");
		encryptLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		encryptLabel.setForeground(new Color(0x4198d9));

		// Create "Choose .WAV File" button
		encryptFCButton = new JButton("<html><b>Choose a .WAV File</b></html>");
		encryptFCButton.addActionListener(this);

		// Create "Play" button
		encryptPlayButton = new JButton("<html><b>Play</b></html>");
		encryptPlayButton.addActionListener(this);
		
		// Create "Stop" button
		encryptStopButton = new JButton("<html><b>Stop</b></html>");
		encryptStopButton.addActionListener(this);
		
		// Create "Password" Label
		encryptPasswordLabel = new JLabel("Enter Password:");
		
		// Create Password Field
		encryptPassword = new JPasswordField(10); // password field

		// Create "Make Secret Message WAV File" button
		encryptRunButton = new JButton("<html><b>Make Secret Message WAV File</b></html>"); // GO! button
		encryptRunButton.addActionListener(this);

		// Create Secret Message text area
		encryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		encryptSecretMessage.setLineWrap(true);
		encryptSecretMessage.setWrapStyleWord(true);

		// Add content to sub-panels
		encryptSubPanel1.add(encryptLabel);

		encryptSubPanel2.add(encryptFCButton);
		
		encryptSubPanel3.add(encryptPasswordLabel);
		encryptSubPanel3.add(encryptPassword);
		
		encryptSubPanel4.add(encryptSecretMessage);
		
		encryptSubPanel5.add(encryptPlayButton);
		encryptSubPanel5.add(encryptStopButton);
		encryptSubPanel5.add(encryptRunButton);

		// Add all encrypt sub-panels to encryptPanel
		encryptPanel.add(encryptSubPanel1);
		encryptPanel.add(encryptSubPanel2);
		encryptPanel.add(encryptSubPanel3);
		encryptPanel.add(encryptSubPanel4);
		encryptPanel.add(encryptSubPanel5);

		// Add encryptPanel to mainPanel
		add(encryptPanel);

		/*
		 * Decrypt GUI - Section
		 */
		// Create "Decrypt" label
		decryptLabel = new JLabel("Decrypt");
		decryptLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		decryptLabel.setForeground(new Color(0x4198d9));

		// Create "Choose .WAV File" button
		decryptFCButton = new JButton("<html><b>Choose a .WAV File</b></html>");
		decryptFCButton.addActionListener(this);

		// Create "Play" button
		decryptPlayButton = new JButton("<html><b>Play</b></html>");
		decryptPlayButton.addActionListener(this);
		
		// Create "Stop" button
		decryptStopButton = new JButton("<html><b>Stop</b></html>");
		decryptStopButton.addActionListener(this);
		
		// Create "Password" Label
		decryptPasswordLabel = new JLabel("Enter Password:");
		
		// Create Password Field
		decryptPassword = new JPasswordField(10); // password field

		// Create Secret Message WAV File
		decryptRunButton = new JButton("<html><b>Decrypt Secret Message in WAV File</b></html>"); // GO! button
		decryptRunButton.addActionListener(this);

		// Display Area for Secret Message
		decryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		decryptSecretMessage.setLineWrap(true);
		decryptSecretMessage.setWrapStyleWord(true);

		// Add content to sub-panels
		decryptSubPanel1.add(decryptLabel);

		decryptSubPanel2.add(decryptFCButton);
		
		decryptSubPanel3.add(decryptPasswordLabel);
		decryptSubPanel3.add(decryptPassword);
		
		decryptSubPanel4.add(decryptSecretMessage);
		
		decryptSubPanel5.add(decryptPlayButton);
		decryptSubPanel5.add(decryptStopButton);
		decryptSubPanel5.add(decryptRunButton);

		// Add all encrypt sub-panels to encryptPanel
		decryptPanel.add(decryptSubPanel1);
		decryptPanel.add(decryptSubPanel2);
		decryptPanel.add(decryptSubPanel3);
		decryptPanel.add(decryptSubPanel4);
		decryptPanel.add(decryptSubPanel5);

		// Add encryptPanel to mainPanel
		add(decryptPanel);
	}

	public void actionPerformed(ActionEvent event) {
		// Handle encrypt open file action.
		Object eventSource = event.getSource();
		
		if ( (eventSource == encryptFCButton) ||
			 (eventSource == decryptFCButton) ) {
			int returnVal = fc.showOpenDialog(Audio_Steganography_Driver.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File tempFile = fc.getSelectedFile();
				
				if (eventSource == encryptFCButton)
				{
					encryptFile = tempFile;
					System.out.println("encryptFCButton");
				}
				
				else
				{
					decryptFile = tempFile;
					System.out.println("decryptFCButton");
				}
				
				// This is where a real application would open the file.
				System.out.println("Opening: " + tempFile.getName() + "." + "\n");
				
				tempFile = null;
			}
			
			else {
				System.out.println("Open encrypt file command cancelled by user." + "\n");
			}
		}
		
		else if (eventSource == encryptRunButton) {
			if (encryptFile != null){
				System.out.println("Open the .wav file: " + encryptFile
						+ ".\n Then encrypt message, and inject into new .wav file.");
			}
			else {
				System.out.println("Encrypt Error: You must select a file first.");
			}
		}
		
		else if (eventSource == decryptRunButton) {
			if (decryptFile != null){
				System.out.println("Open the .wav file: " + decryptFile
						+ ".\n Then decrypt message, and display the hidden message.");
			}
			else {
				System.out.println("Decrypt Error: You must select a file first.");
			}
		}
		
		else if (eventSource == encryptPlayButton) {
			if (encryptFile != null){
				System.out.println("Play encrypt .wav file.");
				try {
				    AudioInputStream stream;
				    AudioFormat format;
				    DataLine.Info info;
				    Clip clip;

				    stream = AudioSystem.getAudioInputStream(encryptFile);
				    format = stream.getFormat();
				    info = new DataLine.Info(Clip.class, format);
				    clip = (Clip) AudioSystem.getLine(info);
				    clip.open(stream);
				    clip.start();
				}
				catch (Exception e) {
				    System.out.println("Error: " + e.getMessage());
				}
			}
			else {
				System.out.println("Encrypt Error: You must select a file first.");
			}
		}
		
		else if (eventSource == decryptPlayButton) {
			if (decryptFile != null){
				System.out.println("Play decrypt .wav file.");
				try {
				    AudioInputStream stream;
				    AudioFormat format;
				    DataLine.Info info;
				    Clip clip;

				    stream = AudioSystem.getAudioInputStream(decryptFile);
				    format = stream.getFormat();
				    info = new DataLine.Info(Clip.class, format);
				    clip = (Clip) AudioSystem.getLine(info);
				    clip.open(stream);
				    clip.start();
				}
				catch (Exception e) {
				    System.out.println("Error: " + e.getMessage());
				}
			}
			else {
				System.out.println("Decrypt Error: You must select a file first.");
			}
		}
		
		else if (eventSource == encryptStopButton) {
			if (encryptFile != null){
				System.out.println("Stop playing encrypt .wav file.");
			}
			else {
				System.out.println("Encrypt Error: You must select a file first.");
			}
		}
		
		else if (eventSource == decryptStopButton) {
			if (decryptFile != null){
				System.out.println("Stop playing decrypt .wav file.");
			}
			else {
				System.out.println("Decrypt Error: You must select a file first.");
			}
		}
	}

	private static void createAndShowGUI() {
		/**
		 * Create the GUI and show it. For thread safety, this method should be
		 * invoked from the event-dispatching thread.
		 */
		// Create and set up the window.
		JFrame frame = new JFrame("Audio_Steganography");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new Audio_Steganography_Driver());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * Create the GUI and show it. For thread safety, this method should be
		 * invoked from the event-dispatching thread.
		 */
		JWav wav = new JWav("this will be a filepath");

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
