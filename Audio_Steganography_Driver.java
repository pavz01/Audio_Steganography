/*
 * Imports
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import classes.JWav;
import components.LimitedPlainDocument;

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
	static final int MESSAGE_MAX_CHARACTERS = 300;
	static final int PASSWORD_MAX_CHARACTERS = 10;
	JButton testButton;
	
	// WAV elements
	JWav encryptJWav, decryptJWav;
	
	// ------ HELPER FUNCTIONS ------
	// Function: isNullOrWhitespace
	// Description: Will return TRUE if "theString" is either null or only whitespace
	public Boolean isNullOrWhitespace (String theString) {
		return ( (theString == null) ||
				 (theString.isEmpty()) ||
				 (theString.trim().length() <= 0) );
	}

	// ------ OTHER FUNCTIONS ------
	// Will place "message" in decrypt's GUI message field
	public void guiDecryptDisplayMessage(String message) throws Exception {
		System.out.println("Called guiDecryptDisplayMessage function.");
		
		if (isNullOrWhitespace(message)) {
			throw new Exception("Error: 'message' is empty.");
		}
		
		// place message in "decryptSecretMessage"
		decryptSecretMessage.setText(message);
	}
	
	// Will validate encrypt's GUI message is valid
	public Boolean guiEncryptHasValidMessage() {
		System.out.println("Called guiEncryptHasValidMessage function.");
		
		// Validate encrypt's message contains content
		if (encryptSecretMessage.getDocument().getLength() > 0) {
			System.out.println("Has valid message.");
			return true;
		}
		
		System.out.println("Does not have valid message.");
		return false;
	}

	// Will validate encrypt's GUI password is valid
	public Boolean guiEncryptHasValidPassword() {
		// Validate encrypt's message contains content
		if (encryptPassword.getDocument().getLength() > 0) {
			System.out.println("Has valid password.");
			return true;
		}
		
		System.out.println("Does not have valid password.");
		return false;
	}

	// Will validate decrypt's GUI password is valid
	public Boolean guiDecryptHasValidPassword() {
		// Validate encrypt's message contains content
		if (decryptPassword.getDocument().getLength() > 0) {
			System.out.println("Has valid password.");
			return true;
		}
		
		System.out.println("Does not have valid password.");
		return false;
	}
	
	// Getter function to return the value of encrypt's message
	public String getGuiEncryptMessage() throws Exception {
		return encryptSecretMessage.getDocument().getText(0, encryptSecretMessage.getDocument().getLength());
	}
	
	public char[] getGuiEncryptPassword() throws Exception {
		return encryptPassword.getPassword();
	}

	// Constructor
	public Audio_Steganography_Driver() {
		encryptJWav = new JWav();
		decryptJWav = new JWav();
		
		// -- GUI SECTION --
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
		encryptPassword.setDocument(new LimitedPlainDocument(PASSWORD_MAX_CHARACTERS));

		// Create "Make Secret Message WAV File" button
		encryptRunButton = new JButton("<html><b>Make Secret Message WAV File</b></html>"); // GO! button
		encryptRunButton.addActionListener(this);

		// Create Secret Message text area
		encryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		encryptSecretMessage.setLineWrap(true);
		encryptSecretMessage.setWrapStyleWord(true);
		encryptSecretMessage.setDocument(new LimitedPlainDocument(MESSAGE_MAX_CHARACTERS));
		
		// Create Test Button which will be useful for testing stuff
		testButton = new JButton("<html><b>Test</b></html>"); // DELETE LATER
		testButton.addActionListener(this);
		
		// Add content to sub-panels
		encryptSubPanel1.add(encryptLabel);

		encryptSubPanel2.add(encryptFCButton);
		
		encryptSubPanel3.add(encryptPasswordLabel);
		encryptSubPanel3.add(encryptPassword);
		
		encryptSubPanel4.add(encryptSecretMessage);
		
		encryptSubPanel5.add(encryptPlayButton);
		encryptSubPanel5.add(encryptStopButton);
		encryptSubPanel5.add(encryptRunButton);
		encryptSubPanel5.add(testButton);

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
		decryptPassword.setDocument(new LimitedPlainDocument(PASSWORD_MAX_CHARACTERS)); // Limit to 10 characters

		// Create Secret Message WAV File
		decryptRunButton = new JButton("<html><b>Decrypt Secret Message in WAV File</b></html>"); // GO! button
		decryptRunButton.addActionListener(this);

		// Display Area for Secret Message
		decryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		decryptSecretMessage.setLineWrap(true);
		decryptSecretMessage.setWrapStyleWord(true);
		decryptSecretMessage.setEditable(false);
		decryptSecretMessage.setBackground(Color.LIGHT_GRAY);
		decryptSecretMessage.setDocument(new LimitedPlainDocument(MESSAGE_MAX_CHARACTERS));

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
//			int returnVal;

			try {
				if (eventSource == encryptFCButton) {
					System.out.println("encryptFCButton");
					
					// Pop open an "Open File" file chooser dialog.
					// If a WAV file has been selected, initialize it.
					if (encryptJWav.getJFileChooser().showOpenDialog(Audio_Steganography_Driver.this) == JFileChooser.APPROVE_OPTION) {
						// If the Encrypt Side of the GUI is currently using an Audio Stream, release the file's resources
						if (encryptJWav.isUsingAudioResources()) {
							encryptJWav.releaseAudioResources();
						}
						
						encryptJWav.initialize();
						System.out.println("encryptJWav is initialized.");
					}
					else {
						System.out.println("Open file command cancelled by user.");
					}
				}
				else {
					System.out.println("decryptFCButton");
					
					// Pop open an "Open File" file chooser dialog.
					// If a WAV file has been selected, initialize it.
					if (decryptJWav.getJFileChooser().showOpenDialog(Audio_Steganography_Driver.this) == JFileChooser.APPROVE_OPTION) {
						// If the Decrypt Side of the GUI is currently using an Audio Stream, release the file's resources
						if (encryptJWav.isUsingAudioResources()) {
							encryptJWav.releaseAudioResources();
						}
						
						decryptJWav.initialize();
						System.out.println("decryptJWav is initialized.");
					}
					else {
						System.out.println("Open file command cancelled by user.");
					}
				}
			}
			// If there was an error initializing the WAV file, mark it
			// as not valid.
			catch (Exception e) {
				if (eventSource == encryptFCButton) {
					System.out.println("encryptFCButton " + e.getMessage());
					
					// If there was an error initializing the WAV file, mark it
					// as not valid.
					encryptJWav.setWavFileToNotValid();
				}
				else {
					System.out.println("decryptFCButton " + e.getMessage());
					
					decryptJWav.setWavFileToNotValid();
				}
			}

		}

		// Handle when the user wants to create a new file that
		// has a hidden, encrypted message
		else if (eventSource == encryptRunButton) {
			// Validate the user has provided a valid password,
			// message, and WAV file
			if (guiEncryptHasValidPassword() &&
				guiEncryptHasValidMessage() &&
				encryptJWav.hasValidWavFile() ) {
				System.out.println("Hurray, has valid password/message/wav file.");

				try {
					// Create a copy of the JWav
					System.out.println("Creating copy of JWav");
					JWav tempJWav = new JWav(encryptJWav);
					
					// Pop open a "Save File" file chooser dialog.
					int returnVal;
					
					returnVal = tempJWav.getJFileChooser().showSaveDialog(Audio_Steganography_Driver.this);

					// If a WAV file has been selected, proceed to process
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						// Save the copied WAV file to the hard drive
						tempJWav.copyWavFile(); // DONE :)
						
						tempJWav.setMessage(getGuiEncryptMessage());
						
						// Encrypt the message
//						tempJWav.encryptMessage(getGuiEncryptPassword()); // TO-DO
						
						// Insert the encrypted message into the copied WAV file
						tempJWav.injectEncryptedMessage();
					}

					// Cancel processing due to user not choosing a file to save to
					else {
						System.out.println("Save file command cancelled by user.");
					}
					
					tempJWav = null;
				}
				catch (Exception e) {
					System.out.println("encryptRunButton Error: " + e.getMessage());
				}
			}
			else {
				System.out.println("encryptRunButton Error: Fail! Password/Message/wav file was not valid.");
			}
		}
		
		// Handle when the user wants to obtain a hidden message
		// from a WAV file that contains a hidden, encrypted message
		else if (eventSource == decryptRunButton) {
			// Validate the user has provided a valid WAV file,
			// and a password
			System.out.println("decryptRunButton");
			
			if (decryptJWav.hasValidWavFile() &&
				guiDecryptHasValidPassword() ) {
				System.out.println("Hurray, has valid wav/password file.");
				
				try {
					// Decrypt the message using the user-provided password
					// THIS SHOULD ACTUAL COME FROM THE DECRYPT MESSAGE FIELD
					decryptJWav.decryptMessage(new String("TESTING").toCharArray()); // TO-DO
					
					// Display the result in the decrypt's GUI message area
					guiDecryptDisplayMessage(decryptJWav.getMessage());
				}
				catch (Exception e) {
					System.out.println("decryptRunButton " + e.getMessage());
				}
			}
			else {
				System.out.println("decryptRunButton Error: Fail! wav file/password was not valid.");
			}
		}

		// Handle when the user wants to play the encrypt WAV file
		else if (eventSource == encryptPlayButton) {
			// Validate if a valid WAV file has been provided
			if (encryptJWav.hasValidWavFile()) {
				// Play the audio file
				try {
					encryptJWav.play();
				}
				catch (Exception e) {
					System.out.println("encryptPlayButton " + e.getMessage());
				}
			}
			else {
				System.out.println("encryptPlayButton Error: wav file is not valid.");
			}
		}
		
		// Handle when the user wants to play the decrypt WAV file
		else if (eventSource == decryptPlayButton) {
			// Validate if a valid WAV file has been provided
			if (decryptJWav.hasValidWavFile()) {
				// Play the audio file
				try {
					decryptJWav.play();
				}
				catch (Exception e) {
					System.out.println("decryptPlayButton " + e.getMessage());
				}
			}
			else {
				System.out.println("decryptPlayButton Error: wav file is not valid.");
			}
		}
		
		// Handle when the user wants to stop the encrypt WAV file
		else if (eventSource == encryptStopButton) {
			// Validate if a valid WAV file has been provided
			if (encryptJWav.hasValidWavFile()) {
				// Stop the audio file
				try {
					encryptJWav.stop();
				}
				catch (Exception e) {
					System.out.println("encryptStopButton " + e.getMessage());
				}
			}
			else {
				System.out.println("encryptStopButton Error: wav file is not valid.");
			}
		}
		
		// Handle when the user wants to stop the decrypt WAV file
		else if (eventSource == decryptStopButton) {
			// Validate if a valid WAV file has been provided
			if (decryptJWav.hasValidWavFile()) {
				// Stop the audio file
				try {
					decryptJWav.stop();
				}
				catch (Exception e) {
					System.out.println("decryptStopButton " + e.getMessage());
				}
			}
			else {
				System.out.println("decryptStopButton Error: wav file is not valid.");
			}
		}
		
		// Otherwise the user is a dev testing stuff out
		else if (eventSource == testButton) {
			try {
				encryptJWav.testStuff();
			}
			catch (Exception e) {
				System.out.println("Error testing: " + e.getMessage());
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
