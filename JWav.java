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
 * @author Larcade
 * Description: JWav is a class that will read, modify, and write .wav files.
 *              It will provide means to inject secret encrypted messages into
 *              new .wav files. It will also provide means to decrypt and display
 *              secret messages if the user provides the secret password. As such,
 *              the user will be able to provide a secret password when injecting
 *              a secret message into a new .wav file.
 */
public class JWav {
	private JFileChooser fc;
	private File file;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private DataLine.Info audioInfo;
	private Clip audioClip;
	private String message;
	
	public JWav () {
		// Set directory that Open File dialogue window will begin
		fc = new JFileChooser(new File("C:/Users/Larcade/Documents/Cory/School/CS499a_b/soundFiles"));
		
		// Create and use a filter to only accept .wav files
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "WAV Files", "wav");
		fc.setFileFilter(filter);
	}
	
	public JFileChooser getJFileChooser () {
		return fc;
	}
	
	public File getFile() {
		return fc.getSelectedFile();
	}
	
	public String getMessage () {
		return message;
	}

	public void initialize () throws Exception {
		file = fc.getSelectedFile();
		audioStream = AudioSystem.getAudioInputStream(file);
		audioFormat = audioStream.getFormat();
		audioInfo = new DataLine.Info(Clip.class, audioFormat);
		audioClip = (Clip) AudioSystem.getLine(audioInfo);
		audioClip.open(audioStream);
	}
	
	public String getFileName () {
		return file.getName();
	}
	
	// TO-DO
	public void encryptMessage (String guiPassword, String guiMessage) {
		System.out.println("Called encryptMessage function.");
	}
	
	// TO-DO
	public void createWavFileAndInjectMessage () {
		System.out.println("Called createWavFileAndInjectMessage function.");
	}
	
	// TO-DO
	public void decryptMessage (String password) {
		System.out.println("Called decryptMessage function.");
	}
	
	// TO-DO
	public Boolean hasValidWavFile() {
		System.out.println("Called hasValidWavFile function.");
		return true;
	}
	
	// Will validate if the WAV file has a hidden message in it
	// TO-DO
	public Boolean hasHiddenMessage() {
		System.out.println("Called hasHiddenMessage function.");
		return true;
	}
	
	// Plays the WAV file
	public void play () {
		audioClip.start();
	}
	
	// Stops the WAV file
	public void stop () {
		audioClip.stop();
	}
}
