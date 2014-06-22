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
	private WavHeader wavHeaderChunk;
	private WavFormatChunk wavFormatChunk;
	private WavDataChunk wavDataChunk;
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
	// Description: A setter function to set "validWavFile" to false
	public void setWavFileToNotValid() {
		validWavFile = false;
	}
	
	// Function: setMessage
	// Description: A setter function to set "message"
	public void setMessage (String guiMessage) {
		message = guiMessage;
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
	public void encryptMessage (char[] guiPassword) {
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
		
		/*
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
		*/
		
		// TEST #2 START - Simulate message.
		// Status: PASSED. :)
		// We are going to test out our binary manipulation now.
		/*
		byte byteTestZero = (byte)0x63;
		byte zeroMask = (byte)0xfe; // Need to use & operator
		byte byteTestOne = (byte)0x58;
		byte oneMask = (byte)0x01; // Need to use | operator
		
		System.out.println("Should be 98: " + (byteTestZero & zeroMask));
		System.out.println("Should be 89: " + (byteTestOne | oneMask));
		
		// TEST #2 END
		*/
		
		// TEST #3 START - Simulate message.
		// Status: Passed. :)
		// Now we're going to convert a String message into a byte array, 
		// and write to the WAV file using the byte array.
		/*
		int numCharacters = message.length();
		byte[] bytesToHide = message.getBytes();
		byte maskByte = (byte) 0x80;
		int dataSize = allWavData.length - 44;
		int sizeNeeded = 64 * numCharacters;
		byte maskZero = (byte)0xfe; // Need to use & operator
		byte maskOne = (byte)0x01; // Need to use | operator
		byte maskReset = (byte)0x80;
		
		if (dataSize < sizeNeeded) {
			throw new Exception ("Error: WAV file is too small to hide message.");
		}

		// Inject all characters of the message into allWavData
		// BUG ALERT: We're currently injecting our bytes into each byte sequentially.
		//   This can change an int of: 30452 or 0111 0110 1111 0100
		//                          to: 30709 or 0111 0111 1111 0101.
		//   That's why we're getting tons of static initially.
		for (int i = 0, j = 100000; i < numCharacters; j++) {
			// Determine if the current bit for the current character is a one
			if ( (bytesToHide[i] & maskByte) == maskByte) {
				// Mask the LSB of the current byte of allWavData with a one
				allWavData[j] = (byte)(allWavData[j] | maskOne);
			}
			// If it's not a one, then it's a zero
			else {
				// Mask the LSB of the current byte of allWavData with a zero
				allWavData[j] = (byte)(allWavData[j] & maskZero);
			}

			// Check if we have injected the last bit of the current character
			if (maskByte == maskOne) {
				// If so, reset the bit to check for to the MSB
				maskByte = maskReset;
				
				// Then, move on to the next character to inject
				i++;
			}
			// Otherwise, move on to the next bit of the current character
			else {
				maskByte = (byte)((maskByte & 0xff) >>> 1);
			}
		}
		
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(allWavData);
		fop.close();
		
		System.out.println("test3");
		// TEST #3 END
		*/
		
		/*
		// TEST 4 START - Initialize WavHeader, WavFormatChunk, WavDataChunk.
		// Status: Passed. :)
		// Break the WAV's data into it's corresponding parts: Header, Format, Data.
		// Will be useful later for manipulating the data and injecting it into
		// a new WAV file.
		wavHeader = new WavHeader(); // DONE
		wavHeader.display();
		wavFormat = new WavFormatChunk(); // DONE
		wavFormat.display();
		wavData = new WavDataChunk(); // DONE
		wavData.display();
		
		System.out.println("test4");
		// TEST #4 END
		*/
		
		// TEST 5 START - Use the new classes to modify and update the WAV file.
		// Upon completion of this test, the user will not be able to notice
		// the difference between the original WAV file and the WAV file that
		// has the hidden message contained in it. This should be the last step
		// for completing the main functionality for this function.
		// Status: Passed. :)
		wavHeaderChunk = new WavHeader();
		wavFormatChunk = new WavFormatChunk();
		wavDataChunk = new WavDataChunk();
		
		int numCharacters = message.length();
		byte[] bytesToHide = message.getBytes();
		byte maskByte = (byte) 0x80;
		byte[] tempData = wavDataChunk.getData();
		int dataSize = tempData.length;
		int byteOffset = wavFormatChunk.getBlockAlign();
		int sizeNeeded = 64 * numCharacters * byteOffset;
		byte maskZero = (byte)0xfe; // Need to use & operator
		byte maskOne = (byte)0x01; // Need to use | operator
		byte maskReset = (byte)0x80;
		
		if (dataSize < sizeNeeded) {
			throw new Exception ("Error: WAV file is too small to hide message.");
		}

		// Inject all characters of the message into allWavData
		for (int i = 0, j = 0; i < numCharacters; j = j + byteOffset) {
			// Determine if the current bit for the current character is a one
			if ( (bytesToHide[i] & maskByte) == maskByte) {
				// Mask the LSB of the current byte of allWavData with a one
				tempData[j] = (byte)(tempData[j] | maskOne);
			}
			// If it's not a one, then it's a zero
			else {
				// Mask the LSB of the current byte of allWavData with a zero
				tempData[j] = (byte)(tempData[j] & maskZero);
			}

			// Check if we have injected the last bit of the current character
			if (maskByte == maskOne) {
				// If so, reset the bit to check for to the MSB
				maskByte = maskReset;
				
				// Then, move on to the next character to inject
				i++;
			}
			// Otherwise, move on to the next bit of the current character
			else {
				maskByte = (byte)((maskByte & 0xff) >>> 1);
			}
		}
		
		// Copy tempData to allWavData
		if (tempData.length == (allWavData.length - 44)) {
			for (int i = 0; i < dataSize; i++) {
//				System.out.print("allWavData[" + (i + 4) + "] before is: " + allWavData[i + 44]);
				allWavData[i + 44] = tempData[i];
//				System.out.println("         allWavData[" + (i + 44) + "] after is: " + allWavData[i + 44]);
			}
		}
		
		// Overwrite the WAV file with new data that contains the hidden message
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(allWavData);
		fop.close();
		
		System.out.println("test5");
		// TEST #5 END
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
		private String chunkID; // Should be "RIFF"
		private Integer chunkSize;
		private String format; // Should be "WAVE"
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavHeader()
		public WavHeader () {
			try {
				System.out.println("WavHeader Constructor");
				
				// Set chunkID
				byte[] tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i];
				}
				chunkID = new String(tempByteArray, "UTF-8");

				// Grab the size of the WAV file
				tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 4];
				}
				ByteBuffer bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				chunkSize = bb.getInt();
				
				// Grab the format of the file. Should be "WAVE"
				tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 8];
				}
				format = new String(tempByteArray, "UTF-8");

			}
			catch (Exception e) {
				System.out.println("WavHeader Error: " + e.getMessage());
			}
		}
		
		public void display () {
			try {
				System.out.println("HEADER CHUNK CONTENTS:");
				System.out.println("chunkID is: " + chunkID);
				System.out.println("chunkSize is: " + chunkSize);
				System.out.println("chunkSize should be: " + (allWavData.length - 8));
				System.out.println("format is: " + format);
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
		private String chunkID; // Contains the letters "fmt "
		private Integer chunkSize; // size of the rest of the sub-chunk. 16 for PCM.
		private Integer audioFormat; // values other than 1 indicate some form of compression
		private Integer numChannels; // Mono = 1, Stereo = 2, etc.
		private Integer sampleRate;
		private Integer byteRate;
		private Integer blockAlign; // # of bytes for 1 sample
		private Integer bitsPerSample;
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavFormatChunk()
		public WavFormatChunk () {
			System.out.println("WavFormatChunk Constructor");
			
			try {
				// Set chunkID
				byte[] tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 12];
				}
				chunkID = new String(tempByteArray, "UTF-8");

				// Set chunkSize
				tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 16];
				}
				ByteBuffer bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				chunkSize = bb.getInt();
				
				// Set audioFormat
				tempByteArray = new byte[4];
				for (int i = 0; i < 2; i++) {
					tempByteArray[i] = allWavData[i + 20];
				}
				bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				audioFormat = bb.getInt();
				
				// Set numChannels
				tempByteArray = new byte[4];
				for (int i = 0; i < 2; i++) {
					tempByteArray[i] = allWavData[i + 22];
				}
				bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				numChannels = bb.getInt();
				
				// Set sampleRate
				tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 24];
				}
				bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				sampleRate = bb.getInt();
				
				// Set byteRate
				tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 28];
				}
				bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				byteRate = bb.getInt();
				
				// Set blockAlign
				tempByteArray = new byte[4];
				for (int i = 0; i < 2; i++) {
					tempByteArray[i] = allWavData[i + 32];
				}
				bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				blockAlign = bb.getInt();
				
				// Set bitsPerSample
				tempByteArray = new byte[4];
				for (int i = 0; i < 2; i++) {
					tempByteArray[i] = allWavData[i + 34];
				}
				bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				bitsPerSample = bb.getInt();
			}
			catch (Exception e) {
				System.out.println("Error WavFormatChunk: " + e.getMessage());
			}
		}
		
		public Integer getBlockAlign () {
			return blockAlign;
		}
		
		public void display () {
			try {
				System.out.println("FORMAT CHUNK CONTENTS:");
				System.out.println("chunkID is: " + chunkID);
				System.out.println("chunkSize is: " + chunkSize);
				System.out.println("audioFormat is: " + audioFormat);
				System.out.println("numChannels is: " + numChannels);
				System.out.println("sampleRate is: " + sampleRate);
				System.out.println("byteRate is: " + byteRate);
				System.out.println("byteRate should be: " + (sampleRate * numChannels * bitsPerSample / 8));
				System.out.println("blockAlign is: " + blockAlign);
				System.out.println("bitsPerSample is: " + bitsPerSample);
			}
			catch (Exception e) {
				System.out.println("WavFormatChunk.display Error: " + e.getMessage());
			}
		}
	}
	
	// Class: WavDataChunk
	// Description: Will contain the WAV file's data info.
	private class WavDataChunk {
		// ------ MEMBER VARIABLES ------
		private String chunkID;
		private Integer chunkSize;
		private byte[] data;
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavDataChunk()
		public WavDataChunk () {
			System.out.println("WavDataChunk Constructor");
			
			try {
				// Set chunkID
				byte[] tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 36];
				}
				chunkID = new String(tempByteArray, "UTF-8");

				// Set chunkSize
				tempByteArray = new byte[4];
				for (int i = 0; i < 4; i++) {
					tempByteArray[i] = allWavData[i + 40];
				}
				ByteBuffer bb = ByteBuffer.wrap(tempByteArray);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				chunkSize = bb.getInt();
				
				// Make sure chunkSize is right
				if (chunkSize != (allWavData.length - 44)) {
					throw new Exception("Aaaaah! chunkSize isn't what it should be!");
				}
				
				// Set data
				data = new byte[chunkSize];
				for (int i = 0; i < chunkSize; i++) {
					data[i] = allWavData[i + 44];
				}
			}
			catch (Exception e) {
				System.out.println("WavData Chunk Error: " + e.getMessage());
			}
		}
		
		public byte[] getData() {
			return data;
		}
		
		public void display () {
			try {
				System.out.println("DATA CHUNK CONTENTS:");
				System.out.println("chunkID is: " + chunkID);
				System.out.println("chunkSize is: " + chunkSize);
				System.out.println("chunkSize should be: " + (allWavData.length - 44));
				
				/*  // For debug purposes. Both outputs should be exactly the same.
				// Compare all elements of both arrays.
//				for (int i = 0, j = 44; i < data.length; i++, j++) { 
				// Compare the last 1000000 elements of both arrays
				for (int i = data.length - 1000000, j = allWavData.length - 1000000; i < data.length; i++, j++) {
					System.out.print("data[" + i + "] is: " + data[i]);
					System.out.println("         allWavData[" + j + "] is: " + allWavData[j]);
				}
				*/
			}
			catch (Exception e) {
				System.out.println("WavDataChunk.display Error: " + e.getMessage());
			}
		}
	}
}
