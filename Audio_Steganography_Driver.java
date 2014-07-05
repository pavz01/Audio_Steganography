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
		System.out.println("Called guiDecryptDisplayMessage function.");
		
		if (isNullOrWhitespace(message)) {
			throw new Exception("Error: 'message' is empty.");
		}
		
		// place message in "decryptSecretMessage"
		messageTextArea.setText(message);
	}
	
	// Will validate encrypt's GUI message is valid
	public Boolean guiHasValidMessage() {
		System.out.println("Called guiEncryptHasValidMessage function.");
		
		// Validate encrypt's message contains content
		if (messageTextArea.getDocument().getLength() > 0) {
			System.out.println("Has valid message.");
			return true;
		}
		
		System.out.println("Does not have valid message.");
		return false;
	}

	// Will validate encrypt's GUI password is valid
	public Boolean guiHasValidPassword() {
		// Validate encrypt's message contains content
		if (passwordField.getDocument().getLength() > 0) {
			System.out.println("Has valid password.");
			return true;
		}
		
		System.out.println("Does not have valid password.");
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
	
	public void resetForm() throws Exception {
		if (myJWav.isUsingAudioResources()) {
			myJWav.releaseAudioResources();
		}
		
		playPauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		goButton.setEnabled(false);
		myJWav = new JWav();
		passwordField.setText(null);
		messageTextArea.setText(null);
	}
	
	public void validateGoButton() {
		System.out.println("called validateGoButton");
		
		boolean validPassword = false;
		boolean validWavFile = false;
		
		if (!isNullOrWhitespace(new String(passwordField.getPassword())) &&
			passwordField.getPassword().length > 0) {
			validPassword = true;
			System.out.println("Has valid password.");
		}
		else {
			System.out.println("Doesn't have valid password.");
		}
		
		if (myJWav.hasValidWavFile()) {
			validWavFile = true;
			System.out.println("Has valid wav file.");
		}
		else {
			System.out.println("Doesn't have valid wav file.");
		}
		
		if (modeComboBox.getSelectedItem().equals("Encrypt")) {
			boolean validMessage = false;
			
			if (!isNullOrWhitespace(messageTextArea.getText()) &&
				messageTextArea.getText().length() > 0) {
				validMessage = true;
				System.out.println("Has valid message.");
			}
			else {
				System.out.println("Doesn't have valid message.");
			}
			
			if (validPassword && validWavFile && validMessage) {
				goButton.setEnabled(true);
			}
			else {
				goButton.setEnabled(false);
			}
		}
		else {
			if (validPassword && validWavFile) {
				goButton.setEnabled(true);
			}
			else {
				goButton.setEnabled(false);
			}
		}
	}
	
	// Constructor
	public Audio_Steganography_Driver() {
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
				System.out.println("resetButton Error: " + e.getMessage());
			}
		}

		else if (eventSource == modeComboBox) {
			try {
				resetForm();
			}
			catch (Exception e) {
				System.out.println("modeComboBox Error: " + e.getMessage());
			}
			
			String currentMode = (String) modeComboBox.getSelectedItem();
			
			if (currentMode.equals("Encrypt")) {
				messageTextArea.setEditable(true);
				messageTextArea.setBackground(Color.WHITE);
			}
			else {
				messageTextArea.setEditable(false);
				messageTextArea.setBackground(Color.LIGHT_GRAY);
			}
		}
		
		// Handle when the user wants to choose a file
		else if (eventSource == fcButton) {
			try {
				System.out.println("fcButton");
				
				// Pop open an "Open File" file chooser dialog.
				// If a WAV file has been selected, initialize it.
				if (myJWav.getJFileChooser().showOpenDialog(Audio_Steganography_Driver.this) == JFileChooser.APPROVE_OPTION) {
					// If the Encrypt Side of the GUI is currently using an Audio Stream, release the file's resources
					if (myJWav.isUsingAudioResources()) {
						myJWav.releaseAudioResources();
					}
					
					myJWav.initialize();
					validateGoButton();
					playPauseButton.setEnabled(true);
					stopButton.setEnabled(true);
					System.out.println("myJWav is initialized.");
				}
				else {
					System.out.println("Open file command cancelled by user.");
				}
			}
			// If there was an error initializing the WAV file, mark it
			// as not valid.
			catch (Exception e) {
				System.out.println("fcButton " + e.getMessage());
				
				// If there was an error initializing the WAV file, mark it
				// as not valid.
				myJWav.setWavFileToNotValid();
				goButton.setEnabled(false);
				playPauseButton.setEnabled(false);
				stopButton.setEnabled(false);
			}
		}

		// Handle when the user wants to create a new file that
		// has a hidden, encrypted message
		else if (eventSource == goButton) {
			String currentMode = (String) modeComboBox.getSelectedItem();
			if (currentMode.equals("Encrypt")) {
				// Validate the user has provided a valid password,
				// message, and WAV file
				if (guiHasValidPassword() &&
					guiHasValidMessage() &&
					myJWav.hasValidWavFile() ) {
					System.out.println("Hurray, has valid password/message/wav file.");

					try {
						// Create a copy of the JWav
						System.out.println("Creating copy of JWav");
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
							tempJWav.displayWavFileInfo(); // for debug purposes
							
							// Encrypt the message
							tempJWav.encryptMessage(getGuiMessage(), getGuiPassword());
							
							// Insert the encrypted message into the copied WAV file
							tempJWav.injectMessage();
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
			else {
				// Validate the user has provided a valid WAV file,
				// and a password
				System.out.println("decryptRunButton");
				
				if (myJWav.hasValidWavFile() &&
					guiHasValidPassword() ) {
					System.out.println("Hurray, has valid wav/password file.");
					
					try {
						// Read WAV entire WAV file into byte array
						myJWav.readWavBytes();
						myJWav.displayWavFileInfo(); // for debug purposes
						
						// Decrypt the message using the user-provided password
						myJWav.decryptMessage(getGuiPassword());
						
						// Display the result in the decrypt's GUI message area
						guiDisplayMessage(myJWav.getMessage());
					}
					catch (Exception e) {
						System.out.println("goButton " + e.getMessage());
					}
				}
				else {
					System.out.println("goButton Error: Fail! wav file/password was not valid.");
				}
			}
		}
		
		// Handle when the user wants to play the WAV file
		else if (eventSource == playPauseButton) {
			// Validate if a valid WAV file has been provided
			if (myJWav.hasValidWavFile()) {
				// Play the audio file
				try {
					myJWav.play();
				}
				catch (Exception e) {
					System.out.println("playButton Error: " + e.getMessage());
				}
			}
			else {
				System.out.println("playButton Error: wav file is not valid.");
			}
		}
		
		// Handle when the user wants to stop the WAV file
		else if (eventSource == stopButton) {
			// Validate if a valid WAV file has been provided
			if (myJWav.hasValidWavFile()) {
				// Stop the audio file
				try {
					myJWav.stop();
				}
				catch (Exception e) {
					System.out.println("stopButton Error: " + e.getMessage());
				}
			}
			else {
				System.out.println("stopButton Error: wav file is not valid.");
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
	
	class MyDocumentListener implements DocumentListener {
		String newline = "\n";
		
		public void insertUpdate(DocumentEvent e) {
			updateForm(e);
	    }
	    public void removeUpdate(DocumentEvent e) {
			updateForm(e);
	    }
	    public void changedUpdate(DocumentEvent e) {
	        //Plain text components do not fire these events
	    }
	    public void updateForm(DocumentEvent e) {
			Document doc = (Document) e.getDocument();
			int length = doc.getLength();
			System.out.println("Document's length is: " + length);
			
			if (length == 0) {
				goButton.setEnabled(false);
			}
			else {
				validateGoButton();
			}
	    }
	}
}
