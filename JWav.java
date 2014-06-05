/**
 * 
 */
package classes;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.sound.sampled.*;

/**
 * @author Cory Larcade
 * Description: JWav is a class that will read, modify, and write .wav files.
 *              It will provide means to inject secret encrypted messages into
 *              new .wav files. It will also provide means to decrypt and display
 *              secret messages if the user provides the secret password. As such,
 *              the user will be able to provide a secret password when injecting
 *              a secret message into a new .wav file.
 */
public class JWav {
	// ------ MEMBER VARIABLES ------
	private JFileChooser fc;
	private File file;
	
	// AudioInputStream contains the following:
	// 1) The format of the audio data in the stream (format)
	// 2) The stream's length in sample frames (frameLength)
	// 3) The current position in the stream in sample frames (framePos)
	// 4) The size of each frame, in bytes (frameSize)
	private AudioInputStream audioStream;

	// AudioFormat contains the following:
	// 1) Big Endian or not (bigEndian)
	// 2) # of channels (channels)
	// 3) the type of audio encoding used (encoding)
	// 4) # of frames per second (frameRate)
	// 5) # of bytes in each frame (frameSize)
	// 6) # of samples per second (sampleRate)
	// 7) # of bits in each sample (sampleSizeInBits)
	private AudioFormat audioFormat;
	private DataLine.Info audioInfo;
	private Clip audioClip;
	private String message;
	private Boolean validWavFile;
	private Boolean hasInjectedMessage;
	
	// ------ CONSTRUCTORS ------
	// Constructor: JWav()
	public JWav () {
		// Set directory that Open File dialogue window will begin
		fc = new JFileChooser(new File("C:/Users/Larcade/Documents/Cory/School/CS499a_b/soundFiles"));
		
		// Create and use a filter to only accept .wav files
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "WAV Files", "wav");
		fc.setFileFilter(filter);
		validWavFile = false;

		// Initialize "hasInjectedMessage" to TRUE or FALSE
		checkForInjectedMessage();
	}

	// Copy Constructor: JWav(JWav original)
	public JWav (JWav originalJWav) throws Exception {
		System.out.println("Stub for JWav copy constructor.");

		file = originalJWav.getFile();

		if (!file.canExecute()) {
			throw new Exception("Error: File either does not exist or can not execute");
		}
		
		audioStream = AudioSystem.getAudioInputStream(file);
		
		audioFormat = audioStream.getFormat();
		audioInfo = new DataLine.Info(Clip.class, audioFormat);
		audioClip = (Clip) AudioSystem.getLine(audioInfo);
		audioClip.open(audioStream);
		
		// the WAV file is valid if it has reached this point
		validWavFile = true;
	}

	// ------ SETTERS ------
	// Function: setWavFileToNotValid
	// Description: A setter function to the "validWavFile" to false
	public void setWavFileToNotValid() {
		validWavFile = false;
	}
	
	// ------ GETTERS ------
	// Function: getJFileChooser
	// Description: A getter function that returns "fc"
	public JFileChooser getJFileChooser () {
		return fc;
	}
	
	// Function: getFile
	// Description: A getter function that returns the selected file
	public File getFileFromGUI() {
		return fc.getSelectedFile();
	}
	
	// Function: getMessage
	// Description: Returns "message"
	public String getMessage () throws Exception {
		// For unit testing purposes
		message = "This is a testing message!"; // DELETE LATER
		
		// Valid "message" has content
		if (isNullOrWhitespace(message)) {
			throw new Exception("Error: 'message' variable cannot be null/empty/all whitespace.");
		}
		
		return message;
	}
	
	// Function: getFileName
	// Description: Returns the name of the selected WAV file
	public String getFileName () throws Exception {
		if (file.exists()) {
			return file.getName();
		}
		
		throw new Exception("Error: file does not exist.");
	}
	
	// Function: getFile
	// Description: Returns the file of the selected WAV file
	public File getFile () throws Exception {
		if (file.canExecute()) {
			return file;
		}
		
		throw new Exception("Error: File either does not exist or cannot execute.");
	}
	
	// ------ DEBUG FUNCTIONS ------
	// Function: displayWavFileInfo
	// Description: Will display useful information about the WAV file.
	//              Useful for debugging purposes.
	public void displayWavFileInfo() {
		System.out.println("Audio frame length: " + audioClip.getFrameLength());
		System.out.println("Audio frame position: " + audioClip.getFramePosition());
	}
	
	// ------ HELPER FUNCTIONS ------
	// Function: isNullOrWhitespace
	// Description: Will return TRUE if "theString" is either null or only whitespace
	public Boolean isNullOrWhitespace (String theString) {
		return ( (theString == null) ||
				 (theString.isEmpty()) ||
				 (theString.trim().length() <= 0) );
	}

	// ------ OTHER FUNCTIONS ------
	// Function: initialize
	// Description: Will use the user-provided WAV file to setup
	//              being able to process and use the WAV file.
	public void initialize () throws Exception {
		file = fc.getSelectedFile();
		
		if (!file.canExecute()) {
			throw new Exception("Error: File either does not exist or can not execute");
		}
		
		audioStream = AudioSystem.getAudioInputStream(file);
		audioFormat = audioStream.getFormat();
		audioInfo = new DataLine.Info(Clip.class, audioFormat);
		audioClip = (Clip) AudioSystem.getLine(audioInfo);
		audioClip.open(audioStream);
		
		// the WAV file is valid if it has reached this point
		validWavFile = true;
	}
	
	// Function: encryptMessage
	// Description: Will use the user-provided password and encrypt the
	//              user-provided message.
	// TO-DO
	public void encryptMessage (String guiPassword, String guiMessage) {
		System.out.println("Called encryptMessage function.");
	}
	
	// Function: injectEncryptedMessage
	// Description: Will access the WAV file's data, modify it
	//              to contain the encrypted message's data.
	// TO-DO
	public void injectEncryptedMessage () {
		System.out.println("Called injectEncryptedMessage function.");
	}

	// Function: decryptMessage
	// Description: Will extract the WAV file's encrypted message,
	//              and then decrypt the message. Will use the
	//              password the user provided to decrypt the message
	//              whether it is the right password or not.
	// TO-DO
	public void decryptMessage (String password) {
		System.out.println("Called decryptMessage function.");
	}
	
	// Function: hasValidWavFile
	// Description: A getter function that returns "hasValidWavFile"
	public Boolean hasValidWavFile() {
		System.out.println("Called hasValidWavFile function.");
		return validWavFile;
	}

	// Function: hasHiddenMessage
	// Description: A getter function that returns "hasInjectedMessage"
	public Boolean hasHiddenMessage() {
		System.out.println("Called hasHiddenMessage function.");
		return hasInjectedMessage;
	}

	// Function: play
	// Description: Will play the WAV file
	public void play () {
		// Only attempt to play audio if there is a valid wav file selected
		if (validWavFile) {
			// If the WAV file has finished playing, play the file from the beginning
			if (audioClip.getFramePosition() == audioClip.getFrameLength()) {
				audioClip.setFramePosition(0);
			}
			
			// Play the audio file
			audioClip.start();
		}
	}

	// Function: stop
	// Description: Will stop the WAV file
	public void stop () {
		// Check for valid wav file
		if (validWavFile) {
			// Stop the audio file. Will do nothing if already stopped.
			audioClip.stop();
		}
	}

	// Function: checkforInjectedMessage
	// Description: Will check the WAV file's data and check if it
	//              contains an injected message. Then it will set
	//              "hasInjectedMessage" to TRUE or FALSE.
	public void checkForInjectedMessage () {
		System.out.println("Called hasValidWavFile function.");
	}
}
