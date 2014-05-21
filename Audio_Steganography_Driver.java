/*
 * 
 */
import javax.swing.*;
import javax.swing.filechooser.*;
import classes.JWav;

/**
 * @author Larcade
 * Description: Audio_Steganography_Driver will utilize the JWav class to prompt the user
 *              for a .wav file, read in the .wav file, and create an identical .wav file with 
 *              an injected and encrypted message. It will give the user the option to play
 *              the original and newly created .wav files. It will allow the user to select
 *              new .wav files to read in and to create new .wav files from.
 */
public class Audio_Steganography_Driver {
	private static void createAndShowGUI() {
		/**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    //Create and set up the window.
	    JFrame frame = new JFrame("Audio_Steganography");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //Add the ubiquitous "Hello World" label.
	    JLabel label = new JLabel("Please input wav file.");
	    frame.getContentPane().add(label);

	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
		JWav wav = new JWav("this will be a filepath");
		
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
}
