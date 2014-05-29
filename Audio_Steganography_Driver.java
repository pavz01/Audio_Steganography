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
	JPanel encryptPanel, encryptSubPanel1, encryptSubPanel2; /*, encryptSubPanel3;*/
	JPanel decryptPanel, decryptSubPanel1, decryptSubPanel2; /*, decryptSubPanel3;*/
	JLabel encryptHeaderLabel, decryptHeaderLabel, passwordLabel;
	JLabel encryptFileLabel, decryptFileLabel, secretMessageLabel;
	JButton encryptFCButton, decryptFCButton, encryptRunButton, decryptRunButton;
	JButton playButton, stopButton;
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
//		encryptSubPanel3 = new JPanel(); // Third subcontainer inside encryptPanel
		decryptPanel = new JPanel();     // All decrypt content will be here
		decryptSubPanel1 = new JPanel(); // First subcontainer inside decryptPanel
		decryptSubPanel2 = new JPanel(); // Second subcontainer inside decryptPanel
//		decryptSubPanel3 = new JPanel(); // Third subcontainer inside decryptPanel

		// Customize layout for all panels
		encryptPanel.setBackground(Color.GRAY);
		
		encryptSubPanel1.setLayout(new BoxLayout(encryptSubPanel1, BoxLayout.PAGE_AXIS));
		encryptSubPanel1.setBackground(Color.BLUE);
		
		encryptSubPanel2.setBackground(Color.CYAN);
		
		decryptSubPanel1.setLayout(new BoxLayout(decryptSubPanel2, BoxLayout.PAGE_AXIS));

		// ------ Encrypt GUI Section ------
		// Create "Encrypt" label
		encryptHeaderLabel = new JLabel("Encrypt");
		encryptHeaderLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		encryptHeaderLabel.setForeground(new Color(0x4198d9));

		// Create "Choose .WAV File" button
		encryptFCButton = new JButton("<html><b>Choose .WAV File</b></html>");
		encryptFCButton.addActionListener(this);
//		encryptFCButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Create label for the name of the chosen file
		encryptFileLabel = new JLabel("File:"); // Default to empty

		// Create "Play" button
		playButton = new JButton("<html><b>Play</b></html>");
		playButton.addActionListener(this);
		
		// Create "Stop" button
		stopButton = new JButton("<html><b>Stop</b></html>");
		stopButton.addActionListener(this);
		
		// Create "Password:" label
		passwordLabel = new JLabel("Enter Password");
		
		// Create Password Field
		encryptPassword = new JPasswordField(10);
		
		// Create "Secret Message" label
		secretMessageLabel = new JLabel("Secret Message");
		
		// Create Secret Message text area
		encryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
		encryptSecretMessage.setLineWrap(true);
		encryptSecretMessage.setWrapStyleWord(true);

		// Create Secret Message WAV File
		encryptRunButton = new JButton("<html><b>Create Secret Message WAV File</b></html>"); // GO! button
		encryptRunButton.addActionListener(this);

		// Add content to encryptSubPanel2
		encryptSubPanel2.add(playButton);
		encryptSubPanel2.add(stopButton);

		// Add content to encryptSubPanel1
		encryptSubPanel1.add(encryptHeaderLabel);
		encryptSubPanel1.add(encryptFCButton);
		encryptSubPanel1.add(encryptFileLabel);
		encryptSubPanel1.add(encryptSubPanel2);
		encryptSubPanel1.add(passwordLabel);
		encryptSubPanel1.add(encryptPassword);
		encryptSubPanel1.add(secretMessageLabel);
		encryptSubPanel1.add(encryptSecretMessage);
		encryptSubPanel1.add(encryptRunButton);
		
		// Align content
//		encryptHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Add content to encryptPanel
		encryptPanel.add(encryptSubPanel1);

		// Add encryptPanel to main panel
		add(encryptPanel);
/*
		// ------ Decrypt GUI - Section ------ NEW
		// Create sub-content for decryptSubPanel1
		decryptHeaderLabel = new JLabel("Decrypt");
		decryptHeaderLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		decryptHeaderLabel.setForeground(new Color(0x4198d9));

		// Add sub-content to decryptSubPanel1
		decryptSubPanel1.add(decryptHeaderLabel); // "Decrypt" label

		// Create sub-content for decryptSubPanel2
		decryptFCButton = new JButton("<html><b>Choose a .WAV File</b></html>");
		decryptFCButton.addActionListener(this);

		decryptPassword = new JPasswordField(10); // password field

		decryptRunButton = new JButton("<html><b>GO!</b></html>"); // GO! button
		decryptRunButton.addActionListener(this);

		// Add sub-content to decryptSubPanel2
		decryptSubPanel2.add(decryptFCButton);
		decryptSubPanel2.add(decryptPassword);
		decryptSubPanel2.add(decryptRunButton);

		// Create sub-content for decryptSubPanel3
		// TO-DO -> CREATE A SOUND PLAYER HERE

		decryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
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
		add(decryptPanel);
*/
		
/*
		// Create all panels
		encryptPanel = new JPanel();     // All encrypt content will be here
		encryptSubPanel1 = new JPanel(); // First subcontainer inside encryptPanel
		encryptSubPanel2 = new JPanel(); // Second subcontainer inside encryptPanel
		encryptSubPanel3 = new JPanel(); // Third subcontainer inside encryptPanel
		decryptPanel = new JPanel();     // All decrypt content will be here
		decryptSubPanel1 = new JPanel(); // First subcontainer inside decryptPanel
		decryptSubPanel2 = new JPanel(); // Second subcontainer inside decryptPanel
		decryptSubPanel3 = new JPanel(); // Third subcontainer inside decryptPanel

		// Customize layout for all panels
		encryptPanel.setLayout(new BoxLayout(encryptPanel, BoxLayout.PAGE_AXIS));
		decryptPanel.setLayout(new BoxLayout(decryptPanel, BoxLayout.PAGE_AXIS));

		// ------ Encrypt GUI Section ------
		// Create content for encryptSubPanel1
		encryptLabel = new JLabel("Encrypt");
		encryptLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		encryptLabel.setForeground(new Color(0x4198d9));

		// Add sub-content to encryptSubPanel1
		encryptSubPanel1.add(encryptLabel);

		// Create content for encryptSubPanel2
		encryptFCButton = new JButton("<html><b>Choose a .WAV File</b></html>");
		encryptFCButton.addActionListener(this);

		encryptPassword = new JPasswordField(10); // password field

		encryptRunButton = new JButton("<html><b>GO!</b></html>"); // GO! button
		encryptRunButton.addActionListener(this);

		// Add sub-content to encryptSubPanel2
		encryptSubPanel2.add(encryptFCButton);
		encryptSubPanel2.add(encryptPassword);
		encryptSubPanel2.add(encryptRunButton);

		// Create sub-content for encryptSubPanel3
		// TO-DO -> CREATE A SOUND PLAYER HERE
		encryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
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
		add(encryptPanel);

		// ------ Decrypt GUI - Section ------
		// Create sub-content for decryptSubPanel1
		decryptLabel = new JLabel("Decrypt");
		decryptLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		decryptLabel.setForeground(new Color(0x4198d9));

		// Add sub-content to decryptSubPanel1
		decryptSubPanel1.add(decryptLabel); // "Decrypt" label

		// Create sub-content for decryptSubPanel2
		decryptFCButton = new JButton("<html><b>Choose a .WAV File</b></html>");
		decryptFCButton.addActionListener(this);

		decryptPassword = new JPasswordField(10); // password field

		decryptRunButton = new JButton("<html><b>GO!</b></html>"); // GO! button
		decryptRunButton.addActionListener(this);

		// Add sub-content to decryptSubPanel2
		decryptSubPanel2.add(decryptFCButton);
		decryptSubPanel2.add(decryptPassword);
		decryptSubPanel2.add(decryptRunButton);

		// Create sub-content for decryptSubPanel3
		// TO-DO -> CREATE A SOUND PLAYER HERE

		decryptSecretMessage = new JTextArea(6, 50); // Large enough for 300 characters to be displayed.
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
		add(decryptPanel);
*/
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
		
		else if (eventSource == decryptRunButton) {
			if (decryptFile != null){
				System.out.println("Open the .wav file: " + decryptFile
						+ ".\n Then decrypt message, and display the hidden message.");
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
