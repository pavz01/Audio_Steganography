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
	// GUI Components
	JPanel encryptPanel, encryptSubPanel1, encryptSubPanel2, encryptSubPanel3, encryptSubPanel4, encryptSubPanel5;
	JPanel decryptPanel, decryptSubPanel1, decryptSubPanel2, decryptSubPanel3, decryptSubPanel4, decryptSubPanel5;
	JLabel encryptLabel, decryptLabel, encryptPasswordLabel, decryptPasswordLabel;
	JButton encryptFCButton, decryptFCButton, encryptRunButton, decryptRunButton;
	JButton encryptPlayButton, encryptStopButton, decryptPlayButton, decryptStopButton;
	JPasswordField encryptPassword, decryptPassword;
	JTextArea encryptSecretMessage, decryptSecretMessage;
	
	// WAV elements
	JWav encryptJWav, decryptJWav;
	
	// Will place "message" in decrypt's GUI message field
	public void guiDecryptDisplayMessage(String message) {
		System.out.println("Called guiDecryptDisplayMessage function.");
		return;
	}
	
	// Will validate encrypt's GUI message is valid
	public Boolean guiEncryptHasValidMessage() {
		System.out.println("Called guiEncryptHasValidMessage function.");
		return true;
	}

	// Will validate encrypt's GUI password is valid
	public Boolean guiEncryptHasValidPassword() {
		System.out.println("Called guiEncryptHasValidPassword function.");
		return true;
	}

	// Will validate decrypts GUI password is valid
	public Boolean guiDecryptHasValidPassword() {
		System.out.println("Called guiDecryptHasValidPassword function.");
		return true;
	}

	// Constructor
	public Audio_Steganography_Driver() {
		encryptJWav = new JWav();
		decryptJWav = new JWav();
		
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

	// Handle all user initiated events
	public void actionPerformed(ActionEvent event) {
		// Handle encrypt open file action.
		Object eventSource = event.getSource();
		
		// Handle when the user wants to choose a file
		if ( (eventSource == encryptFCButton) ||
			 (eventSource == decryptFCButton) ) {
			// Pop open an "Open File" file chooser dialog.
			int returnVal;
			
			if (eventSource == encryptFCButton) {
				returnVal = encryptJWav.getJFileChooser().showOpenDialog(Audio_Steganography_Driver.this);
			}
			else {
				returnVal = decryptJWav.getJFileChooser().showOpenDialog(Audio_Steganography_Driver.this);
			}

			// If a WAV file has been selected, initialize it.
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (eventSource == encryptFCButton)
				{
					System.out.println("encryptFCButton");
					try {
						encryptJWav.initialize();
						System.out.println("encryptJWav is initialized.");
					}
					catch (Exception e) {
						System.out.println("encryptFCButton Error: " + e.getMessage());
					}
				}
				else
				{
					System.out.println("decryptFCButton");
					try {
						decryptJWav.initialize();
						System.out.println("decryptJWav is initialized.");
					}
					catch (Exception e) {
						System.out.println("decryptFCButton Error: " + e.getMessage());
					}
				}
			}
			
			else {
				System.out.println("Open file command cancelled by user.");
			}
		}

		// Handle when the user wants to create a new file that
		// has a hidden, encrypted message
		else if (eventSource == encryptRunButton) {
			// Validate the user has provided a valid password,
			// message, and WAV file
			if (guiEncryptHasValidPassword() && // TO-DO
				guiEncryptHasValidMessage() && // TO-DO
				encryptJWav.hasValidWavFile() ) { // TO-DO
				// Encrypt the message
				encryptJWav.encryptMessage("Stub for guiEncryptPassword", "Stub for guiEncryptMessage"); // TO-DO
				
				// Create a new WAV file with the secret message injected into it
				encryptJWav.createWavFileAndInjectMessage(); // TO-DO
			}
		}
		
		// Handle when the user wants to obtain a hidden message
		// from a WAV file that contains a hidden, encrypted message
		else if (eventSource == decryptRunButton) {
			// Validate the user has provided a valid WAV file,
			// and a password
			if (decryptJWav.hasValidWavFile() && // TO-DO
				guiDecryptHasValidPassword()) { // TO-DO
				// Decrypt the message using the user-provided password
				decryptJWav.decryptMessage("Stub for guiDecryptPassword"); // TO-DO
				
				// Display the result in the decrypt's GUI message area
				guiDecryptDisplayMessage(decryptJWav.getMessage()); // TO-DO
			}
		}

		else if (eventSource == encryptPlayButton) {
			if (encryptJWav.hasValidWavFile()) { // TO-DO
				try {
					encryptJWav.play(); // TO-DO
				}
				catch (Exception e) {
					System.out.println("encryptPlayButton Error: " + e.getMessage());
				}
			}
		}
		
		else if (eventSource == decryptPlayButton) {
			if (decryptJWav.hasValidWavFile()) { // TO-DO
				try {
					decryptJWav.play(); // TO-DO
				}
				catch (Exception e) {
					System.out.println("decryptPlayButton Error: " + e.getMessage());
				}
			}
		}
		
		else if (eventSource == encryptStopButton) {
			if (encryptJWav.hasValidWavFile()) { // TO-DO
				try {
					encryptJWav.stop(); // TO-DO
				}
				catch (Exception e) {
					System.out.println("encryptStopButton Error: " + e.getMessage());
				}
			}
		}
		
		else if (eventSource == decryptStopButton) {
			if (decryptJWav.hasValidWavFile()) { // TO-DO
				try {
					decryptJWav.stop(); // TO-DO
				}
				catch (Exception e) {
					System.out.println("decryptStopButton Error: " + e.getMessage());
				}
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
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
