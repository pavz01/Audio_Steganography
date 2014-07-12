/*
 * Imports
 */
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

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
	JFrame frame;
	JPanel mainPanel, subPanel1, subPanel2, subPanel3, subPanel4, subPanel5, subPanel6;
	JLabel modeLabel, fileLabel, filePathLabel, passwordLabel, audioLabel, actionsLabel;
	JButton fcButton, goButton, resetButton, playPauseButton, stopButton;
	JPasswordField passwordField;
	JTextArea messageTextArea;
	JComboBox modeComboBox;
	
	static final int MESSAGE_MAX_CHARACTERS = 300;
	static final int PASSWORD_MAX_CHARACTERS = 10;
	
	// WAV elements
	JWav myJWav;
	
	// Keep track of mode setting
	String currentMode;
	
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
	public void guiDisplayMessage(String message) throws Exception {
		// KEEP THIS FOR DEBUGGING
//		System.out.println("Called guiDecryptDisplayMessage function.");
		
		if (isNullOrWhitespace(message)) {
			throw new Exception("Error: 'message' is empty.");
		}
		
		// place message in "decryptSecretMessage"
		messageTextArea.setText(message);
	}
	
	// Will validate encrypt's GUI message is valid
	public Boolean guiHasValidMessage() {
		// KEEP THIS FOR DEBUGGING
//		System.out.println("Called guiEncryptHasValidMessage function.");
		
		// Validate encrypt's message contains content
		if (messageTextArea.getDocument().getLength() > 0) {
			// KEEP THIS FOR DEBUGGING
//			System.out.println("Has valid message.");
			return true;
		}
		
		// KEEP THIS FOR DEBUGGING
//		System.out.println("Does not have valid message.");
		return false;
	}

	// Will validate encrypt's GUI password is valid
	public Boolean guiHasValidPassword() {
		// Validate encrypt's message contains content
		if (passwordField.getDocument().getLength() > 0) {
			// KEEP THIS FOR DEBUGGING
//			System.out.println("Has valid password.");
			return true;
		}
		
		// KEEP THIS FOR DEBUGGING
//		System.out.println("Does not have valid password.");
		return false;
	}

	// Getter function to return the value of encrypt's message
	public String getGuiMessage() throws Exception {
		return messageTextArea.getDocument().getText(0, messageTextArea.getDocument().getLength());
	}
	
	// Getter function that returns encrypt's password
	public char[] getGuiPassword() throws Exception {
		return passwordField.getPassword();
	}
	
	public void resetMessageAndPassword() {
		goButton.setEnabled(false);
		passwordField.setText(null);
		messageTextArea.setText(null);
	}
	
	public void resetForm() throws Exception {
		if (myJWav == null) {
			throw new Exception("Error: No WAV file has been selected.");
		}
		
		if (myJWav.isUsingAudioResources()) {
			myJWav.releaseAudioResources();
		}
		
		myJWav.closeStreams();
		
		playPauseButton.setEnabled(false);
		playPauseButton.setText("<html><b>Play</b></html>");
		stopButton.setEnabled(false);
		myJWav = new JWav();
		resetMessageAndPassword();
	}
	
	public boolean goButtonIsOkToEnable() {
		if (isNullOrWhitespace(new String(passwordField.getPassword())) ||
			passwordField.getPassword().length <= 0 ||
			!myJWav.hasValidWavFile()) {
			return false;
		}
		
		if (modeComboBox.getSelectedItem().equals("Encrypt")) {
			if (isNullOrWhitespace(messageTextArea.getText()) ||
				messageTextArea.getText().length() <= 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public void togglePlayPause() throws Exception {
		// User wants to play the WAV file
		if (playPauseButton.getText().equals("<html><b>Play</b></html>")) {
			playPauseButton.setText("<html><b>Pause</b></html>");
			myJWav.play();
		}
		// User wants to pause the WAV file
		else {
			playPauseButton.setText("<html><b>Play</b></html>");
			myJWav.pause();
		}
	}
	
	public void displaySuccess() throws Exception {
		String successMessage = currentMode + "ion process was successful.";
		JOptionPane.showMessageDialog(frame, successMessage, "Encryption Success", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void displayError(String message) {
		JOptionPane.showMessageDialog(frame, message, "Error Occurred", JOptionPane.ERROR_MESSAGE);
	}
	
	// Constructor
	public Audio_Steganography_Driver() {
		frame = new JFrame("Audio_Steganography");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);

		currentMode = "Encrypt";
		myJWav = new JWav();
		
		// Create all panels
		mainPanel = new JPanel(); // All content will be here
		subPanel1 = new JPanel(); // First subcontainer
		subPanel2 = new JPanel(); // Second subcontainer
		subPanel3 = new JPanel(); // Third subcontainer
		subPanel4 = new JPanel(); // Fourth subcontainer
		subPanel5 = new JPanel(); // Fifth subcontainer
		subPanel6 = new JPanel(); // Sixth subcontainer

		// Customize layout for all panels
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// Create "Encrypt" label
		modeLabel = new JLabel("Mode:");
		modeLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		modeLabel.setForeground(new Color(0x4198d9));
		
		// Create mode combo box
		String[] modeOptions = { "Encrypt", "Decrypt" };
		modeComboBox = new JComboBox(modeOptions);
		modeComboBox.addActionListener(this);
		
		// Create "File:" label
		fileLabel = new JLabel("File:");
		fileLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		fileLabel.setForeground(new Color(0x4198d9));

		// Create "Choose .WAV File" button
		fcButton = new JButton("<html><b>Choose a .WAV File</b></html>");
		fcButton.addActionListener(this);

		// Create "filePath" label
		filePathLabel = new JLabel("");
		filePathLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		filePathLabel.setForeground(new Color(0x4198d9));

		// Create "Password" Label
		passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		passwordLabel.setForeground(new Color(0x4198d9));
		
		// Create Password Field
		passwordField = new JPasswordField(10); // password field
		passwordField.setDocument(new LimitedPlainDocument(PASSWORD_MAX_CHARACTERS));
		passwordField.getDocument().addDocumentListener(new MyDocumentListener());

		// Create Message text area
		messageTextArea = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setDocument(new LimitedPlainDocument(MESSAGE_MAX_CHARACTERS));
		messageTextArea.getDocument().addDocumentListener(new MyDocumentListener());
		
		// Create "Audio Controls:" Label
		audioLabel = new JLabel("Audio Controls:");
		audioLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		audioLabel.setForeground(new Color(0x4198d9));
		
		// Create "Play" button
		playPauseButton = new JButton("<html><b>Play</b></html>");
		playPauseButton.setEnabled(false);
		playPauseButton.addActionListener(this);
		
		// Create "Stop" button
		stopButton = new JButton("<html><b>Stop</b></html>");
		stopButton.setEnabled(false);
		stopButton.addActionListener(this);
		
		// Create "Actions:" Label
		actionsLabel = new JLabel("Actions:");
		actionsLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		actionsLabel.setForeground(new Color(0x4198d9));
		
		// Create "GO!" button
		goButton = new JButton("<html><b>GO!</b></html>");
		goButton.setEnabled(false);
		goButton.addActionListener(this);

		// Create "Reset" button
		resetButton = new JButton("<html><b>Reset</b></html>");
		resetButton.addActionListener(this);

		// Add content to sub-panels
		subPanel1.add(modeLabel);
		subPanel1.add(modeComboBox);
		
		subPanel2.add(fcButton);
		
		subPanel3.add(playPauseButton);
		subPanel3.add(stopButton);
		
		subPanel4.add(messageTextArea);
		
		subPanel5.add(passwordLabel);
		subPanel5.add(passwordField);
		
		subPanel6.add(actionsLabel);
		subPanel6.add(goButton);
		subPanel6.add(resetButton);

		// Add all encrypt sub-panels to encryptPanel
		mainPanel.add(subPanel1);
		mainPanel.add(subPanel2);
		mainPanel.add(subPanel3);
		mainPanel.add(subPanel4);
		mainPanel.add(subPanel5);
		mainPanel.add(subPanel6);

		// Add encryptPanel to mainPanel
		add(mainPanel);
		
		// Display the window.
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	// Handle all user initiated events
	public void actionPerformed(ActionEvent event) {
		// Handle encrypt open file action.
		Object eventSource = event.getSource();
		
		if (eventSource == resetButton) {
			try {
				resetForm();
			}
			catch (Exception e) {
				// KEEP THIS FOR DEBUGGING
//				System.out.println("resetButton Error: " + e.getMessage());
				displayError(e.getMessage());
			}
		}

		else if (eventSource == modeComboBox) {
			String nextMode = (String) modeComboBox.getSelectedItem();
			
			if (!currentMode.equals(nextMode)) {
				try {
					resetForm();
					
					currentMode = nextMode;
					
					if (nextMode.equals("Encrypt")) {
						messageTextArea.setEditable(true);
						messageTextArea.setBackground(Color.WHITE);
					}
					else {
						messageTextArea.setEditable(false);
						messageTextArea.setBackground(Color.LIGHT_GRAY);
					}
				}
				catch (Exception e) {
					// KEEP THIS FOR DEBUGGING
//					System.out.println("modeComboBox Error: " + e.getMessage());
					displayError(e.getMessage());
				}
			}
		}
		
		// Handle when the user wants to choose a file
		else if (eventSource == fcButton) {
			try {
				// KEEP THIS FOR DEBUGGING
//				System.out.println("fcButton");
				
				// Pop open an "Open File" file chooser dialog.
				// If a WAV file has been selected, initialize it.
				if (myJWav.getJFileChooser().showOpenDialog(Audio_Steganography_Driver.this) == JFileChooser.APPROVE_OPTION) {
					// If the current mode is Decrypt, reset Decrypt's GUI
					if (currentMode.equals("Decrypt")) {
						resetMessageAndPassword();
					}
					// Otherwise the mode is Encrypt and we need to check if we need to release the WAV file's resources
					else {
						if(myJWav.isUsingAudioResources()) {
							myJWav.releaseAudioResources();
						}
					}
					
					myJWav.initialize();
					playPauseButton.setEnabled(true);
					stopButton.setEnabled(true);
					
					if (goButtonIsOkToEnable()) {
						goButton.setEnabled(true);
					}
					else {
						goButton.setEnabled(false);
					}
					
					// KEEP THIS FOR DEBUGGING
//					System.out.println("myJWav is initialized.");
				}
				else {
					// KEEP THIS FOR DEBUGGING
//					System.out.println("Open file command cancelled by user.");
				}
			}
			// If there was an error initializing the WAV file, mark it
			// as not valid.
			catch (Exception e) {
				// KEEP THIS FOR DEBUGGING
//				System.out.println("fcButton " + e.getMessage());
				
				// If there was an error initializing the WAV file, mark it
				// as not valid.
				myJWav.setWavFileToNotValid();
				goButton.setEnabled(false);
				playPauseButton.setEnabled(false);
				stopButton.setEnabled(false);
				displayError(e.getMessage());
			}
		}

		// Handle when the user wants to create a new file that
		// has a hidden, encrypted message
		else if (eventSource == goButton) {
			// KEEP THIS FOR DEBUGGING
//			System.out.println("GO! Button");
			
//			currentMode = (String) modeComboBox.getSelectedItem();
			
			if (goButtonIsOkToEnable()) {
				if (currentMode.equals("Encrypt")) {
					try {
						// Create a copy of the JWav
						// KEEP THIS FOR DEBUGGING
//							System.out.println("Creating copy of JWav");
						JWav tempJWav = new JWav(myJWav);
						
						// Pop open a "Save File" file chooser dialog.
						int returnVal;
						
						returnVal = tempJWav.getJFileChooser().showSaveDialog(Audio_Steganography_Driver.this);

						// If a WAV file has been selected, proceed to process
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							// Save the copied WAV file to the hard drive
							tempJWav.copyWavFile();
							
							// Read WAV file's data
							tempJWav.readWavBytes();
//								tempJWav.displayWavFileInfo(); // for debug purposes
							
							// Encrypt the message
							tempJWav.encryptMessage(getGuiMessage(), getGuiPassword());
							
							// Insert the encrypted message into the copied WAV file
							tempJWav.injectMessage();
							
							// Clear the message and password fields on the GUI
							resetMessageAndPassword();
							
							displaySuccess();
						}

						// Cancel processing due to user not choosing a file to save to
						else {
							// KEEP THIS FOR DEBUGGING
//								System.out.println("Save file command cancelled by user.");
						}
						
						tempJWav = null;
					}
					catch (Exception e) {
						// KEEP THIS FOR DEBUGGING
//							System.out.println("encryptRunButton Error: " + e.getMessage());
						displayError(e.getMessage());
					}
				}
				else {
					// KEEP THIS FOR DEBUGGING
//						System.out.println("Hurray, has valid wav/password file.");
					
					try {
						// Read WAV entire WAV file into byte array
						myJWav.readWavBytes();
//							myJWav.displayWavFileInfo(); // for debug purposes
						
						// Decrypt the message using the user-provided password
						myJWav.decryptMessage(getGuiPassword());
						
						// Display the result in the decrypt's GUI message area
						guiDisplayMessage(myJWav.getMessage());
					}
					catch (Exception e) {
						// KEEP THIS FOR DEBUGGING
//							System.out.println("goButton " + e.getMessage());
						displayError(e.getMessage());
					}
				}
			}
			else {
				displayError("Error: One or more of the form elements are invalid.");
			}
		}
		
		// Handle when the user wants to play the WAV file
		else if (eventSource == playPauseButton) {
			// Validate if a valid WAV file has been provided
			if (myJWav.hasValidWavFile()) {
				// Play the audio file
				try {
					togglePlayPause();
				}
				catch (Exception e) {
					// KEEP THIS FOR DEBUGGING
//					System.out.println("playPauseButton Error: " + e.getMessage());
					displayError(e.getMessage());
				}
			}
			else {
				// KEEP THIS FOR DEBUGGING
//				System.out.println("playPauseButton Error: WAV file is not valid.");
			}
		}
		
		// Handle when the user wants to stop the WAV file
		else if (eventSource == stopButton) {
			// Validate if a valid WAV file has been provided
			if (myJWav.hasValidWavFile()) {
				// Stop the audio file
				try {
					myJWav.stop();
					playPauseButton.setText("<html><b>Play</b></html>");
				}
				catch (Exception e) {
					// KEEP THIS FOR DEBUGGING
//					System.out.println("stopButton Error: " + e.getMessage());
					displayError(e.getMessage());
				}
			}
			else {
				// KEEP THIS FOR DEBUGGING
//				System.out.println("stopButton Error: WAV file is not valid.");
			}
		}
	}

	private static void createAndShowGUI() {
		new Audio_Steganography_Driver();
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
	
	class MyDocumentListener implements DocumentListener {
		String newline = "\n";
		
		public void insertUpdate(DocumentEvent docEvent) {
			updateForm(docEvent);
	    }
	    public void removeUpdate(DocumentEvent docEvent) {
			updateForm(docEvent);
	    }
	    public void changedUpdate(DocumentEvent docEvent) {
	        //Plain text components do not fire these events
	    }
	    public void updateForm(DocumentEvent docEvent) {
			Document doc = (Document) docEvent.getDocument();
			int length = doc.getLength();
			// KEEP THIS FOR DEBUGGING
//			System.out.println("Document's length is: " + length);
			
			if (length == 0) {
				goButton.setEnabled(false);
			}
			else {
				try {
					if (goButtonIsOkToEnable()) {
						goButton.setEnabled(true);
					}
					else {
						goButton.setEnabled(false);
					}
				}
				catch (Exception e) {
					displayError(e.getMessage());
				}
			}
	    }
	}
}
