/**
 * 
 */
package classes;

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
	private String message = "Hello World";
	
	public JWav(String filePath) {
		System.out.println("filePath is: " + filePath);
	}

	public void sayHello() {
		System.out.println(message);
	}
}
