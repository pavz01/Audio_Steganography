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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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
	private byte[] message;
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
		
		// the WAV file has already been validated for it to be copying
		validWavFile = true;
	}

	// ------ SETTERS ------
	// Function: setWavFileToNotValid
	// Description: A setter function to set "validWavFile" to false
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
		if (message == null) {
			throw new Exception("getMessage Error: 'message' is null.");
		}
		
		String stringMessage = new String(message, "UTF-8");
		
		return stringMessage;
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
		wavHeaderChunk.display();
		wavFormatChunk.display();
		wavDataChunk.display();
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
	
	// Function: readWavBytes
	// Description: Will read in the WAV file's data into a byte array
	public void readWavBytes () throws Exception {
		System.out.println("Called readWavBytes()");
		
		if ( (file == null) || !file.exists() ) {
			throw new Exception("readWavBytes Error: file is either null or doesn't exist.");
		}
		
		// Prepare to read the WAV file's data
		inStream = new FileInputStream(file);
		buffInStream = new BufferedInputStream(inStream);
		
		// Copy the file's data into a byte array
		int availableBytes = buffInStream.available();
		byte[] tempWavData;
		if (availableBytes > 0) {
			tempWavData = new byte[availableBytes];
			int read = 0;
			while (availableBytes > 0) {
				read = buffInStream.read(tempWavData, read, availableBytes);
				availableBytes = buffInStream.available();
			}
		}
		else {
			buffInStream.close();
			throw new Exception("Error: Couldn't copy WAV file's data into array.");
		}
			
		// After copying the data, close the current stream.
		buffInStream.close();
		
		wavHeaderChunk = new WavHeader(tempWavData);
		wavFormatChunk = new WavFormatChunk(tempWavData);
		wavDataChunk = new WavDataChunk(tempWavData);
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

	// Function: encryptMessage (String, char[])
	// Description: Will use the user-provided password and encrypt the
	//              user-provided message.
	// The plan is to create a byte[] that has the following contents:
	//    #[number of characters in message to be hidden]#[encrypted message to be hidden][encrypted padding of '#'s if necessary]
	//    For example, for a message of "hello world": #11#[encrypted form of 'hello world#####']
	public void encryptMessage (String tempMessage, char[] guiPassword) throws Exception {
		System.out.println("Called encryptMessage function.");
		
		System.out.println("Message is: " + tempMessage);

		// Create the '#[number of characters in message to be hidden]#' portion of our byte array
		byte[] unencryptedBytes = new String("#" + tempMessage.length() + "#").getBytes("UTF-8");
		System.out.println("unencryptedBytes is: " + new String(unencryptedBytes, "UTF-8")); // For debugging purposes
		
		// Add any necessary padding to the user-provided password
		String tempPassword = new String(guiPassword);
		int padding = 16 - guiPassword.length;
		for (int i = 0; i < padding; i++) {
			tempPassword += "0";
		}

		// Use the password w/ padding to create a secret key
		SecretKeySpec secretKey = new SecretKeySpec(tempPassword.getBytes("UTF-8"), "AES");
		tempPassword = null;
		guiPassword = null;
		
		// Get an instance for an AES cipher
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

		// Initialize the AES cipher using the secret key
		cipher.init(Cipher.ENCRYPT_MODE,  secretKey);
		
		// Add padding to the user-provided message. Ex/ 'hello world' -> 'hello world#####'
		for (int i = tempMessage.length(); (i % 16) != 0; i++) {
			tempMessage += "#";
		}

		// Create the encrypted portion. Ex/ [encrypted form of 'hello world#####']
		byte[] encryptedBytes = cipher.doFinal(tempMessage.getBytes("UTF-8"));
		
		System.out.println("Encrypted message is: " + new String(encryptedBytes, "UTF-8"));
		
		message = new byte[unencryptedBytes.length + encryptedBytes.length];
		System.arraycopy(unencryptedBytes, 0, message, 0, unencryptedBytes.length);
		System.arraycopy(encryptedBytes, 0, message, unencryptedBytes.length, encryptedBytes.length);
		
		
		// KEEP THIS FOR DEBUGGING
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		System.out.println("Decrypted message is: " + new String(decryptedBytes, "UTF-8"));

	}

	// Function: decryptMessage
	// Description: Will extract the WAV file's encrypted message,
	//              and then decrypt the message. Will use the
	//              password the user provided to decrypt the message
	//              whether it is the right password or not.
	// In Progress
	public void decryptMessage (char [] guiPassword) throws Exception {
		System.out.println("Called decryptMessage function.");
		
		byte poundByte = (byte) 0x23;
		// This will be used to determine what character is potentially hidden
		byte tempByte = 0;
		byte[] tempData = wavDataChunk.getData();
		String numCharacters = ""; // We will have up to 3 characters
		
		ByteBuffer bb = ByteBuffer.wrap(wavFormatChunk.getBlockAlign());
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int byteOffset = bb.getInt(); // The # of bytes between LSB's in the WAV file
		
		byte maskByte = (byte) 0x80; // 1000 0000
		byte maskOne = (byte)0x01; // Need to use | operator
		byte maskReset = (byte)0x80;
		
		// This first search is to find out how many characters are in the
		// hidden message. If there is a hidden message, the number of
		// characters hidden will be within the 1st 5 hidden characters.
		for (int i = 0, j = 0; i < 5; j = j + byteOffset) {
			// Determine if the current bit for the current character is a one
			if ( (tempData[j] & maskOne) == maskOne) {
				// Mask the LSB of the current byte of tempByte with a one
				tempByte = (byte)(tempByte | maskByte);
			}
			
			// Check if we are on the last bit of the current character
			if (maskByte == maskOne) {
				if (i == 0) {
					// If we're on the 1st character and it isn't a pound,
					// then we don't have a hidden message.
					if (tempByte != poundByte) {
						// Set numCharacters to 300 to spit out gibberish
						// to the user for trying to retrieve a hidden
						// when it isn't there. Security measure.
						numCharacters = "300";
						break;
					}
					
					// Otherwise, reset the necessary variables and don't
					// bother recording the '#' in numCharacters
					
					// Reset tempByte
					tempByte = 0;
					
					// Reset the bit to check for to the MSB
					maskByte = maskReset;
					
					// Then, move on to the next character
					i++;
					
					continue;
				}
				// If we find a second '#' character after the 1st character,
				// then we've either found the number of characters hidden or
				// there isn't a hidden message. Either way, we're done processing
				// this part.
				else if ( (i != 0) && (tempByte == poundByte) ) {
					break;
				}
				
				// Otherwise, record the current character as a string
				numCharacters += new String(new byte[] { (byte) tempByte }, "UTF-8");
				
				// Reset tempByte
				tempByte = 0;
				
				// Reset the bit to check for to the MSB
				maskByte = maskReset;
				
				// Then, move on to the next character
				i++;
			}
			// Otherwise, move on to the next bit of the current character
			else {
				maskByte = (byte)((maskByte & 0xff) >>> 1);
			}
		}
		
		// Validate and retrieve the integer form of numCharacters
		int sizeCharacters = Integer.parseInt(numCharacters);
		int sizeMessagePadding = 0;
		
		// must add '#' padding to message. WORKING HERE!!!
		while ( (sizeCharacters % 16) != 0 ) {
			sizeCharacters++;
			sizeMessagePadding++;
		}
		
		message = new byte[sizeCharacters];
		tempByte = 0;
		maskByte = maskReset;
		
		// Use this for extracting the encrypted bytes. WORKING HERE NOW
		for (int i = 0, j = 8 * byteOffset * (numCharacters.length() + 2); i < sizeCharacters; j = j + byteOffset) {
			// Determine if the current bit for the current character is a one
			if ( (tempData[j] & maskOne) == maskOne) {
				// Mask the LSB of the current byte of tempByte with a one
				tempByte = (byte)(tempByte | maskByte);
			}
			
			// Check if we are on the last bit of the current character
			if (maskByte == maskOne) {
				// If we are, then save the compiled byte into our message
				message[i] = tempByte;
				
				// Reset tempByte
				tempByte = 0;
				
				// Reset the bit to check for to the MSB
				maskByte = maskReset;
				
				// Then, move on to the next character
				i++;
			}
			// Otherwise, move on to the next bit of the current character
			else {
				maskByte = (byte)((maskByte & 0xff) >>> 1);
			}
		}
		
		// Message is correct so far
		System.out.println("encrypted message is: " + new String(message, "UTF-8"));
		
		// Add any necessary padding to the user-provided password
		String tempPassword = new String(guiPassword);

		int padding = 16 - guiPassword.length;
		for (int i = 0; i < padding; i++) {
			tempPassword += "0";
		}

		// Use the password w/ padding to create a secret key
		SecretKeySpec secretKey = new SecretKeySpec(tempPassword.getBytes("UTF-8"), "AES");
		tempPassword = null;
		guiPassword = null;
		
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		message = cipher.doFinal(message);

		int messageLength = message.length;
		byte[] tempMessage = new byte[messageLength];
		
		for (int i = 0; i < messageLength; i++) {
			tempMessage[i] = message[i];
		}
		
		int count = messageLength - sizeMessagePadding;
		message = new byte[count];
		
		for (int i = 0; i < count; i++) {
			message[i] = tempMessage[i];
		}
		
		System.out.println("Decrypted message is: " + new String(message, "UTF-8"));
	}
	
	// Function: injectMessage
	// Description: Will inject the "message" into the WAV file.
	public void injectMessage() throws Exception {
		System.out.println("Called injectEncryptedMessage function.");
		
		if (message == null) {
			throw new Exception("injectMessage Error: message is null.");
		}
		
		int numBytes = message.length; // # of bytes to injected
		byte maskByte = (byte) 0x80; // 1000 0000
		
		if (wavDataChunk == null) {
			throw new Exception("injectMessage Error: wavDataChunk is null.");
		}
		
		byte[] tempData = new byte[wavDataChunk.getData().length];
		for (int i = 0; i < wavDataChunk.getData().length; i++) {
			tempData[i] = wavDataChunk.getData()[i];
		}
		
		if ( (tempData == null) || (tempData.length <= 0) ) {
			throw new Exception("injectMessage Error: tempData is either null or tempData.length is <= 0");
		}
		int dataSize = tempData.length; // The # of bytes in the data chunk
		
		if (wavFormatChunk == null) {
			throw new Exception("injectMessage Error: wavFormatChunk is null");
		}
		ByteBuffer bb = ByteBuffer.wrap(wavFormatChunk.getBlockAlign());
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int byteOffset = bb.getInt(); // The # of bytes between LSB's in the WAV file
		
		int sizeNeeded = numBytes * 8 * byteOffset; // # of bytes to hide * # of bytes needed to hide 1 byte of data * offset
		byte maskZero = (byte)0xfe; // Need to use & operator
		byte maskOne = (byte)0x01; // Need to use | operator
		byte maskReset = (byte)0x80;
		
		if (dataSize < sizeNeeded) {
			throw new Exception ("Error: WAV file is too small to hide message.");
		}

		// Inject all characters of the message into the WAV's data
		for (int i = 0, j = 0; i < numBytes; j = j + byteOffset) {
			// Determine if the current bit for the current character is a one
			if ( (message[i] & maskByte) == maskByte) {
				// Mask the LSB of the current byte of tempData with a one
				tempData[j] = (byte)(tempData[j] | maskOne);
			}
			// If it's not a one, then it's a zero
			else {
				// Mask the LSB of the current byte of tempData with a zero
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
		
		// Copy tempData to the data in the dataChunk
		if (tempData.length == wavDataChunk.getData().length) {
			wavDataChunk.setData(tempData);
		}
		else {
			throw new Exception("injectMessage Error: Error occurred while trying to inject message.");
		}
		
		// Overwrite the WAV file with new data that contains the hidden message
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(compileNewWavData());
		fop.close();
		System.out.println("Message Injected.");
	}
	
	public byte[] compileNewWavData() throws Exception {
		ByteBuffer bb = ByteBuffer.wrap(wavHeaderChunk.getChunkSize());
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int wavHeaderChunkSize = bb.getInt();
		byte[] tempBytes = new byte[wavHeaderChunkSize + 8];
		
		byte[] tempWavHeaderBytes = wavHeaderChunk.getAllHeaderBytes(); // Cleared for takeoff
		byte[] tempWavFormatBytes = wavFormatChunk.getAllFormatBytes(); // Cleared for takeoff
		byte[] tempWavDataBytes = wavDataChunk.getAllDataChunkBytes(); // Cleared, I think...
		
		int sizeHeaderBytes = tempWavHeaderBytes.length;
		int sizeFormatBytes = tempWavFormatBytes.length;
		int sizeDataBytes = tempWavDataBytes.length;
		
		System.arraycopy(tempWavHeaderBytes, 0, tempBytes, 0, sizeHeaderBytes);
		System.arraycopy(tempWavFormatBytes, 0, tempBytes, sizeHeaderBytes, sizeFormatBytes);
		System.arraycopy(tempWavDataBytes, 0, tempBytes, (sizeHeaderBytes + sizeFormatBytes), sizeDataBytes);
		
		return tempBytes;
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
		private byte[] chunkSize; // Total wav bytes - 8 bytes for the chunkID and the chunkSize
		private byte[] format; // Should be "WAVE"
		private byte[] allHeaderBytes;
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavHeader(byte[])
		public WavHeader (byte[] tempAllWavData) throws Exception {
			if (tempAllWavData == null) {
				throw new Exception("WavHeader Constructor Error: tempAllWavData is empty.");
			}
			
			try {
				System.out.println("WavHeader Constructor");
				System.out.println("Total Wav Length is: " + tempAllWavData.length);
				
				allHeaderBytes = new byte[12];
				
				// Set chunkID
				chunkID = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkID[i] = tempAllWavData[i];
					allHeaderBytes[i] = tempAllWavData[i];
				}

				// Grab the size of the WAV file
				chunkSize = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkSize[i] = tempAllWavData[i + 4];
					allHeaderBytes[i + 4] = tempAllWavData[i + 4];
				}
				
				// Grab the format of the file. Should be "WAVE"
				format = new byte[4];
				for (int i = 0; i < 4; i++) {
					format[i] = tempAllWavData[i + 8];
					allHeaderBytes[i + 8] = tempAllWavData[i + 8];
				}
				
				// Set allHeaderData
				for (int i = 0; i < 12; i++) {
					allHeaderBytes[i] = tempAllWavData[i];
				}
			}
			catch (Exception e) {
				System.out.println("WavHeader Error: " + e.getMessage());
			}
		}
		
		public byte[] getAllHeaderBytes() throws Exception {
			if (allHeaderBytes == null) {
				throw new Exception("Error: allHeaderBytes is null");
			}
			
			return allHeaderBytes;
		}
		
		public byte[] getChunkID() throws Exception {
			if (chunkID == null) {
				throw new Exception("Error: chunkID is null");
			}
			
			return chunkID;
		}
		
		public byte[] getChunkSize() throws Exception {
			if (chunkSize == null) {
				throw new Exception("Error: chunkSize is null");
			}
			
			return chunkSize;
		}
		
		public byte[] getFormat() throws Exception {
			if (format == null) {
				throw new Exception("Error: format is null");
			}
			
			return format;
		}
		
		public void display () {
			try {
				if (chunkID == null) {
					throw new Exception("chunkID is null");
				}
				
				System.out.println("HEADER CHUNK CONTENTS:");
				System.out.println("chunkID is: " + new String(chunkID, "UTF-8"));
				
				if (chunkSize == null) {
					throw new Exception("chunkSize is null");
				}
				
				ByteBuffer bb = ByteBuffer.wrap(chunkSize);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				System.out.println("chunkSize is: " + bb.getInt());
				
				if (format == null) {
					throw new Exception("format is null");
				}
				
				System.out.println("format is: " + new String(format, "UTF-8"));
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
		private byte[] chunkID; // Contains the letters "fmt "
		private byte[] chunkSize; // size of the rest of the sub-chunk. 16 for PCM.
		private byte[] audioFormat; // values other than 1 indicate some form of compression
		private byte[] numChannels; // Mono = 1, Stereo = 2, etc.
		private byte[] sampleRate;
		private byte[] byteRate;
		private byte[] blockAlign; // # of bytes for 1 sample
		private byte[] bitsPerSample;
		private byte[] allFormatBytes;
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavFormatChunk(byte[])
		public WavFormatChunk (byte[] tempAllWavData) throws Exception {
			System.out.println("WavFormatChunk Constructor");
			
			if (tempAllWavData == null) {
				throw new Exception("WavFormatChunk Constructor Error: tempAllWavData is empty.");
			}
			
			try {
				allFormatBytes = new byte[24];
				
				// Set chunkID
				chunkID = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkID[i] = tempAllWavData[i + 12];
					allFormatBytes[i] = tempAllWavData[i + 12];
				}

				// Set chunkSize
				chunkSize = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkSize[i] = tempAllWavData[i + 16];
					allFormatBytes[i + 4] = tempAllWavData[i + 16];
				}
				
				// Set audioFormat
				audioFormat = new byte[4];
				for (int i = 0; i < 2; i++) {
					audioFormat[i] = tempAllWavData[i + 20];
					allFormatBytes[i + 8] = tempAllWavData[i + 20];
				}
				
				// Set numChannels
				numChannels = new byte[4];
				for (int i = 0; i < 2; i++) {
					numChannels[i] = tempAllWavData[i + 22];
					allFormatBytes[i + 10] = tempAllWavData[i + 22];
				}
				
				// Set sampleRate
				sampleRate = new byte[4];
				for (int i = 0; i < 4; i++) {
					sampleRate[i] = tempAllWavData[i + 24];
					allFormatBytes[i + 12] = tempAllWavData[i + 24];
				}
				
				// Set byteRate
				byteRate = new byte[4];
				for (int i = 0; i < 4; i++) {
					byteRate[i] = tempAllWavData[i + 28];
					allFormatBytes[i + 16] = tempAllWavData[i + 28];
				}
				
				// Set blockAlign
				blockAlign = new byte[4];
				for (int i = 0; i < 2; i++) {
					blockAlign[i] = tempAllWavData[i + 32];
					allFormatBytes[i + 20] = tempAllWavData[i + 32];
				}
				
				// Set bitsPerSample
				bitsPerSample = new byte[4];
				for (int i = 0; i < 2; i++) {
					bitsPerSample[i] = tempAllWavData[i + 34];
					allFormatBytes[i + 22] = tempAllWavData[i + 34];
				}
				
			}
			catch (Exception e) {
				System.out.println("Error WavFormatChunk: " + e.getMessage());
			}
		}
		
		public byte[] getAllFormatBytes() throws Exception {
			if (allFormatBytes == null) {
				throw new Exception("getAllFormatBytes Error: allFormatBytes is null");
			}
			
			return allFormatBytes;
		}
		
		public byte[] getChunkID() throws Exception {
			if (chunkID == null) {
				throw new Exception("getChunkID Error: chunkID is null");
			}
			
			return chunkID;
		}
		
		public byte[] getChunkSize() throws Exception {
			if (chunkSize == null) {
				throw new Exception("getChunkSize Error: chunkSize is null");
			}
			
			return chunkSize;
		}
		
		public byte[] getAudioFormat() throws Exception {
			if (audioFormat == null) {
				throw new Exception("getAudioFormat Error: audioFormat is null");
			}
			
			return audioFormat;
		}
		
		public byte[] getNumChannels() throws Exception {
			if (numChannels == null) {
				throw new Exception("getNumChannels Error: numChannels is null");
			}
			
			return numChannels;
		}
		
		public byte[] getSampleRate() throws Exception {
			if (sampleRate == null) {
				throw new Exception("getSampleRate Error: sampleRate is null");
			}
			
			return sampleRate;
		}
		
		public byte[] getByteRate () throws Exception {
			if (byteRate == null) {
				throw new Exception("getByteRate Error: byteRate is null");
			}
			
			return byteRate;
		}
		
		public byte[] getBlockAlign () throws Exception {
			if (blockAlign == null) {
				throw new Exception("getBlockAlign Error: blockAlign is null");
			}
			
			return blockAlign;
		}
		
		public byte[] getBitsPerSample () throws Exception {
			if (bitsPerSample == null) {
				throw new Exception("getBitsPerSample Error: bitsPerSample is null");
			}
			
			return bitsPerSample;
		}
		
		public void display () {
			try {
				System.out.println("FORMAT CHUNK CONTENTS:");
				
				if (chunkID == null) {
					throw new Exception("chunkID is null");
				}
				if (audioFormat == null) {
					throw new Exception("audioFormat is null");
				}
				
				if (numChannels == null) {
					throw new Exception("numChannels is null");
				}
				
				if (sampleRate == null) {
					throw new Exception("sampleRate is null");
				}
				
				if (byteRate == null) {
					throw new Exception("byteRate is null");
				}
				
				if (blockAlign == null) {
					throw new Exception("blockAlign is null");
				}
								
				if (bitsPerSample == null) {
					throw new Exception("bitsPerSample is null");
				}
								
				System.out.println("chunkID is: " + new String(chunkID, "UTF-8"));
				
				int tempChunkSize = ByteBuffer.wrap(chunkSize).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int tempAudioFormat = ByteBuffer.wrap(audioFormat).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int tempNumChannels = ByteBuffer.wrap(numChannels).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int tempSampleRate = ByteBuffer.wrap(sampleRate).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int tempByteRate = ByteBuffer.wrap(byteRate).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int tempBlockAlign = ByteBuffer.wrap(blockAlign).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int tempBitsPerSample = ByteBuffer.wrap(bitsPerSample).order(ByteOrder.LITTLE_ENDIAN).getInt();
				int expectedByteRate = tempSampleRate * tempNumChannels * tempBitsPerSample / 8;
				
				System.out.println("chunkSize is: " + tempChunkSize);
				System.out.println("audioFormat is: " + tempAudioFormat);
				System.out.println("numChannels is: " + tempNumChannels);
				System.out.println("sampleRate is: " + tempSampleRate);
				System.out.println("byteRate is: " + tempByteRate);
				System.out.println("byteRate should be: " + expectedByteRate);
				System.out.println("blockAlign is: " + tempBlockAlign);
				System.out.println("bitsPerSample is: " + tempBitsPerSample);
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
		private byte[] chunkID;
		private byte[] chunkSize;
		private byte[] data;
		private byte[] allDataChunkBytes;
		
		// ------ CONSTRUCTORS ------
		// Constructor: WavDataChunk(byte[])
		public WavDataChunk (byte[] tempAllWavData) throws Exception {
			System.out.println("WavDataChunk Constructor");
			
			if (tempAllWavData == null) {
				throw new Exception("WavDataChunk Constructor Error: tempAllWavData is empty.");
			}
			
			try {
				// Set chunkID
				chunkID = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkID[i] = tempAllWavData[i + 36];
				}

				// Set chunkSize
				chunkSize = new byte[4];
				for (int i = 0; i < 4; i++) {
					chunkSize[i] = tempAllWavData[i + 40];
				}
				
				int numDataBytes = ByteBuffer.wrap(chunkSize).order(ByteOrder.LITTLE_ENDIAN).getInt();
				
				// Make sure chunkSize is right
				if (numDataBytes != (tempAllWavData.length - 44)) {
					throw new Exception("Aaaaah! chunkSize isn't what it should be!");
				}
				
				// Set data
				data = new byte[numDataBytes];
				for (int i = 0; i < numDataBytes; i++) {
					data[i] = tempAllWavData[i + 44];
				}
				
				// Set allDataChunkBytes
				int totalBytesToRead = 8 + numDataBytes;
				allDataChunkBytes = new byte[totalBytesToRead];
				for (int i = 0; i < totalBytesToRead; i++) {
					allDataChunkBytes[i] = tempAllWavData[i + 36];
				}
			}
			catch (Exception e) {
				System.out.println("WavData Chunk Error: " + e.getMessage());
			}
		}
		
		public byte[] getAllDataChunkBytes() throws Exception {
			if (allDataChunkBytes == null) {
				throw new Exception("Error: allDataChunkBytes is null");
			}
			
			return allDataChunkBytes;
		}
		
		public byte[] getChunkID() throws Exception {
			if (chunkID == null) {
				throw new Exception("getChunkID Error: chunkID is null");
			}
			
			return chunkID;
		}
		
		public byte[] getChunkSize() throws Exception {
			if (chunkSize == null) {
				throw new Exception("getChunkSize Error: chunkSize is null");
			}
			
			return chunkSize;
		}
		
		public byte[] getData() throws Exception {
			if (data == null) {
				throw new Exception("getData Error: data is null");
			}
			
			return data;
		}
		
		public void setData(byte[] tempData) throws Exception {
			if (tempData == null) {
				throw new Exception("setData Error: tempData is null");
			}
			
			int length = tempData.length;
			for (int i = 0; i < length; i++) {
				data[i] = tempData[i];
				allDataChunkBytes[i + 8] = tempData[i];
			}
		}
		
		public void display () {
			try {
				if (chunkID == null) {
					throw new Exception("chunkID is null");
				}
				
				System.out.println("DATA CHUNK CONTENTS:");
				System.out.println("chunkID is: " + new String(chunkID, "UTF-8"));
				
				if (chunkSize == null) {
					throw new Exception("chunkSize is null");
				}
				
				ByteBuffer bb = ByteBuffer.wrap(chunkSize);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				System.out.println("chunkSize is: " + bb.getInt());
			}
			catch (Exception e) {
				System.out.println("WavDataChunk.display Error: " + e.getMessage());
			}
		}
	}
}
