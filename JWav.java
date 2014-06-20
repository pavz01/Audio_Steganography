/**
 * 
 */
package classes;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
	private WavHeader wavHeader;
	private WavFormatChunk wavFormat;
	private WavDataChunk wavData;
	private File file;
	private String message;
	private byte[] allWavData;
	private Boolean validWavFile;
	private Boolean hasInjectedMessage;
	
	private InputStream inStream;
	private BufferedInputStream buffInStream;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private DataLine.Info audioInfo;
	private Clip audioClip;
	
	
	// ------ CONSTRUCTORS ------
	// Constructor: JWav()
	public JWav () {
		// Set directory that Open File dialogue window will begin
		fc = new JFileChooser(new File("C:/Users/Larcade/Documents/Cory/School/CS499a_b/soundFiles"));
		
		// Create and use a filter to only accept .wav files
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "WAV Files", "wav");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		validWavFile = false;

		// Initialize "hasInjectedMessage" to TRUE or FALSE
		checkForInjectedMessage();
	}

	// Copy Constructor: JWav(JWav original)
	// TO-DO: May need updating. Compare to changes made to initialize() and fit accordingly
	public JWav (JWav originalJWav) throws Exception {
		System.out.println("Stub for JWav copy constructor.");
		
		file = originalJWav.getFile();
		
		if (!file.canExecute()) {
			throw new Exception("Error: File either does not exist or can not execute");
		}
		
		fc = new JFileChooser(new File("C:/Users/Larcade/Documents/Cory/School/CS499a_b/soundFiles"));
		
		// Create and use a filter to only accept .wav files
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "WAV Files", "wav");
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		
//		allWavData = originalJWav.getAllWavData();
		
/*
		inStream = new FileInputStream(file);
		buffInStream = new BufferedInputStream(inStream);
		audioStream = AudioSystem.getAudioInputStream(buffInStream);
		audioFormat = audioStream.getFormat();
		audioInfo = new DataLine.Info(Clip.class, audioFormat);
		audioClip = (Clip) AudioSystem.getLine(audioInfo);
		audioClip.open(audioStream);
*/		
		
		// the WAV file has already been validated for it to be copying
		validWavFile = true;
	}

	// ------ SETTERS ------
	// Function: setWavFileToNotValid
	// Description: A setter function to the "validWavFile" to false
	public void setWavFileToNotValid() {
		validWavFile = false;
	}
	
	// ------ GETTERS ------
	// Function: getAllWavData
	// Description: A getter function that returns "allWavData"
	public byte[] getAllWavData () {
		return allWavData;
	}
	
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
	// Function: isUsingAudioResources
	// Description: Returns if the JWav is currently using audio resources
	public Boolean isUsingAudioResources() {
		if ( (audioClip != null) &&
			 (audioClip.isOpen()) ) {
			return true;
		}
		
		return false;
	}
	
	// Function: releaseAudioResources
	// Description: Will release the audio resources currently in use
	public void releaseAudioResources() {
		audioClip.stop();
		audioClip.close();
	}
	
	// Function: readWavFileData
	// Description: Will read in the WAV file's data into a byte array
	public void readWavFileData () throws Exception {
		System.out.println("Called readWavFileData()");
		
		// Prepare to read the WAV file's data
		inStream = new FileInputStream(file);
		buffInStream = new BufferedInputStream(inStream);
		
		// Copy the file's data into a byte array
		int availableBytes = buffInStream.available();
		if (availableBytes > 0) {
			allWavData = new byte[availableBytes];
			int read = 0;
			while (availableBytes > 0) {
				read = buffInStream.read(allWavData, read, availableBytes);
				availableBytes = buffInStream.available();
			}
		}
		else {
			buffInStream.close();
			throw new Exception("Error: Couldn't copy WAV file's data into array.");
		}
			
		// After copying the data, close the current stream.
		buffInStream.close();
	}
	
	// Function: copyWavFile
	// Description: Will either overwrite a WAV file if the file exists, or
	//              will create a new WAV file if the file doesn't exist.
	//              The file will be an exact copy of the WAV file that is
	//              referred to in the "file" member variable.
	public void copyWavFile() throws Exception {
		System.out.println("Called copyWavFile()");
		
		// Grab the file the user selected/input to save to
		File destFile = fc.getSelectedFile();
		
		System.out.print("File ");
		
		// Overwrite the selected WAV file
		if (destFile.exists()) {
			System.out.println(" exists.");
			System.out.println("Overwriting file.");
			Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File overwritten.");
		}
		// Create a new WAV file
		else {
			System.out.println(" does not exist.");
			System.out.println("Looks like we're going to create this new file.");
			Files.copy(file.toPath(), destFile.toPath());
			System.out.println("File created.");
		}
		
		// Next, point to our newly saved WAV file
		file = destFile;
		
		// Read in the WAV file data
		readWavFileData();
		
		// Break the WAV's data into it's corresponding parts:
		//      Header, Format, Data.
		// Will be useful later for manipulating the data and injecting it into
		// a new WAV file.
//		wavHeader = new WavHeader();
//		wavFormat = new WavFormatChunk(); // TO-DO
//		wavData = new WavDataChunk(); // TO-DO
	}
	
	// Function: initialize
	// Description: Will use the user-provided WAV file to setup
	//              being able to process and use the WAV file.
	public void initialize () throws Exception {
		// Get the file
		file = fc.getSelectedFile();
		
		// Make sure we can work with the file
		if (!file.canExecute()) {
			throw new Exception("Error: File either does not exist or can not execute");
		}
	
		// Setup the necessary items so the user can interact with the WAV file
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
	public void encryptMessage (char[] guiPassword, String guiMessage) {
		System.out.println("Called encryptMessage function.");
/*
//		System.out.println("encryptMessage: Password = " + guiPassword.toString());
		char [] correctPass = new char[] {'a', 'a'};
		System.out.println("encryptMessage: Message = " + guiMessage);
*/
	}
	
	// Function: injectEncryptedMessage
	// Description: Will access the WAV file's data, modify it
	//              to contain the encrypted message's data.
	// TO-DO
	public void injectEncryptedMessage () throws Exception {
		System.out.println("Called injectEncryptedMessage function.");
		
		// TEST #1 START
		// Status: Passed. :)
		// Let's assume we have been able to convert our message from the GUI into a byte array
		// For testing purposes, I'm going to grab the bytes from an arbitrary WAV file and use
		// them to write to the WAV file in question.
		File tempFile = new File("C:/Users/Larcade/Documents/Cory/School/CS499a_b/soundFiles/LinkinPark_Numb.wav");
		
		// Prepare to read the WAV file's data
		InputStream tempInStream= new FileInputStream(tempFile);
		BufferedInputStream tempBuffInStream = new BufferedInputStream(tempInStream);
		
		// Copy the file's data into a byte array
		byte[] tempAllData;
		int availableBytes = tempBuffInStream.available();
		if (availableBytes > 0) {
			tempAllData = new byte[availableBytes];
			int read = 0;
			while (availableBytes > 0) {
				read = tempBuffInStream.read(tempAllData, read, availableBytes);
				availableBytes = tempBuffInStream.available();
			}
		}
		else {
			tempBuffInStream.close();
			throw new Exception("Error: Couldn't read test WAV file's stuff.");
		}
			
		// After copying the data, close the current stream.
		tempBuffInStream.close();
		tempFile = null;
		
		// Try to copy the data from LinkinPark_Numb.wav into testing123_Copy.wav
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(tempAllData);
		fop.close();
		// TEST #1 END
		
		// TEST #2 START - Simulate message.
		// Status: TO-DO
		// Now we're going to convert a String message into bits, insert it into
		// the byte array, and finally write to the WAV file using the byte array.
		//message = "Hello World!";
		// TEST #2 END
	}

	// Function: decryptMessage
	// Description: Will extract the WAV file's encrypted message,
	//              and then decrypt the message. Will use the
	//              password the user provided to decrypt the message
	//              whether it is the right password or not.
	// TO-DO
	public void decryptMessage (char [] guiPassword) {
		System.out.println("Called decryptMessage function.");
/*
		// This should actually be extracted from the WAV file
		char [] correctPassword = new char [] {'a', 'a'};

		if (Arrays.equals(guiPassword, correctPassword)) {
		    System.out.println("Password is correct");
		} else {
		    System.out.println("Incorrect password");
		}
*/
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
	
	// Function: testStuff()
	// Description: Will be used to test stuff. Oh yeah.
	public void testStuff() {
		System.out.println("Testing Stuff:");
	}
	
	// ------ INNER CLASSES ------
	// Class: WavHeader
	// Description: Will contain the WAV file's header info.
	private class WavHeader {
		// ------ MEMBER VARIABLES ------
		private byte[] chunkID; // Should be "RIFF"
		private Integer chunkSize;
		private byte[] format;
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavHeader()
		public WavHeader () {
			try {
				// Grab the chunkID. Should be "RIFF"
				System.out.println("WavHeader Constructor");
				chunkID = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkID[i] = allWavData[i];
				}
				System.out.println("chunkID: " + new String(chunkID, "UTF-8"));

				// Grab the size of the WAV file
				byte[] tempSize = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempSize[i] = allWavData[i + 4];
				}
				ByteBuffer bb = ByteBuffer.wrap(tempSize);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				chunkSize = bb.getInt();
				System.out.println("chunkSize: " + chunkSize);
				
				// Grab the format of the file. Should be "WAVE"
				format = new byte[4];
				for (int i = 0; i < 4; i++) {
					format[i] = allWavData[i + 8];
				}
				System.out.println("format: " + new String(format, "UTF-8"));
			}
			catch (Exception e) {
				System.out.println("WavHeader Error: " + e.getMessage());
			}
		}
		
		public void display () {
			try {
				System.out.println("HEADER CHUNK CONTENTS:");
				System.out.println("chunkID: " + new String(chunkID, "UTF-8"));
				System.out.println("chunkSize: " + chunkSize);
				System.out.println("format: " + new String(format, "UTF-8"));
			}
			catch (Exception e) {
				System.out.println("WavHeader.display Error: " + e.getMessage());
			}
		}
	}
	
	// Class: WavFormatChunk
	// Description: Will contain the WAV file's format info.
	private class WavFormatChunk {
		// ------ MEMBER VARIABLES ------
		// TO-DO
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavFormatChunk()
		// TO-DO
		public WavFormatChunk () {
			System.out.println("WavFormatChunk Constructor");
		}
	}
	
	// Class: WavDataChunk
	// Description: Will contain the WAV file's data info.
	private class WavDataChunk {
		// ------ MEMBER VARIABLES ------
		// TO-DO
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavDataChunk()
		// TO-DO
		public WavDataChunk () {
			System.out.println("WavDataChunk Constructor");
		}
	}
}
