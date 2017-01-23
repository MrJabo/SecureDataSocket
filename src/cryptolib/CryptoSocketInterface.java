package cryptolib;
import java.lang.IllegalStateException;
import java.net.SocketTimeoutException;
import java.net.SocketAddress;
import java.io.IOException;

public interface CryptoSocketInterface {

	public enum Channel {
		WLAN;
	}

	public enum RETURN {
		SUCCESS(0),
		READ(-1), 
		WRITE(-2), 
		WRONG_TAG(-3),
		NOT_AVAILABLE(-4), 
		INVALID_CIPHERTEXT(-5);

		private final int value;

		RETURN(int value) {
			this.value = value;
		}

		public int getValue(){
			return this.value;
		}
	}

	/**
	* Await a connection from a device, which calls connect().
	* listen is blocking, until a connection is established
	* port - On which port server listening. 0 for random
	*/
	SocketAddress listen(int port) throws IOException, SocketTimeoutException; //blocking until connection is established

	/**
	* Connect to a device, which awaits a connection and called listen().
	* Throws SocketTimeoutException when connection timedout, IllegalArgumentException when the 
	* destination id is wrong or IOException if socket creation fails
	* returns if connection is established
	*/
	boolean connect() throws IllegalArgumentException, IOException, SocketTimeoutException;

	/**
	* Return the OutOfBand information.
	* IMPORTANT: This has to be the same between both 
	* communicationpartners. If it differs, NEVER call verifiedOOB()!
	*/
	byte[] getOOB() throws IllegalStateException;
	
	/**
	* Confirmation, that the other side is the one we expected.
	* Use getOOB() for comparison, between both partners. 
	* IMPORTANT: when calling verifiedOOB() you sign, that you verified the
	* communicationpartner is the one you expect. If you call verifiedOOB() 
	* though your verificationresult is false, an adversary will be able to
	* read and manipulate your communication.
	*/
	void verifiedOOB() throws IllegalStateException;

	/**
	* Sends the given bytearray to the communicatonpartner.
	* If the partnerdevice is not verified (via OOB), an UnverifiedException will be
	* thrown.
	* Return negativ value on error, for error codes please look at enum RETURN.
	* 	Returns RETURN.INVALID_CIPHERTEXT or RETURN.SUCCESS
	*/
	int write(byte[] array) throws UnverifiedException, IllegalStateException, IOException;
		
	/*
	* Reads bytes send from the communicationpartner and stores them in data.
	* If blocking is true, this function blocks until the next entity is 
	* read.
	* If blocking is false, methods tries to read data.length byte or less bytes.
	* Returns the total number of bytes read into the buffer. 
	* Negativ value on error, for error codes please look at enum RETURN.
	* 	Returns RETURN.READ, RETURN.WRONG_TAG, RETURN.NOT_AVAILABLE, RETURN.INVALID_CIPHERTEXT
	*/
	int read(boolean blocking, byte[] data) throws IllegalStateException, IllegalArgumentException, IOException;
	
	/**
	* Returns true, if there is something to read, false, if not.
	*/
	boolean hasNext() throws IOException;

	/**
	* Close the connection. 
	*/
	void close();
}
