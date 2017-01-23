Interface CryptoSocket {
	/**
	* Constructor.
	* channel defines the interface, which will be used for the 
	* OOB-information.
	* If you call listen() afterwards, connections from the given
	* destination are accepted.
	* If you otherwise call connect() afterwards, the other device will be 
	* searched in the localnetwork via broadcast and when found, a 
	* connection will be set up.
	*/
	CryptoSocket(Channel channel);

	/**
	* Constructor.
	* channel defines the interface, which will be used for the 
	* OOB-information.
	* If you call listen() afterwards, only connections from the given
	* destination are accepted.
	* If you otherwise call connect() afterwards, you will connect to the
	* given destination.
	*/
	CryptoSocket(Channel channel, DestinationIdentifier id);
	
	/**
	* Await a connection from a device, which calls connect().
	* listen() is blocking, until a connection is established
	*/
	void listen();

	/**
	* Connect to a device, which awaits a connection and called listen().
	* Throws ConnectionException when connection timedout or the 
	* destinationdevice couldn't be found.
	*/
	void connect() throws ConnectionException;

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
	*/
	void write(byte[] array) throws UnverifiedException, IllegalStateException;
	
	/**
	* Sends the given int to the communicatonpartner.
	* If the partnerdevice is not verified (via OOB), an UnverifiedException will be
	* thrown.
	*/
	void writeInt(int i) throws UnverifiedException, IllegalStateException;
	
	/**
	* Sends the given char to the communicatonpartner.
	* If the partnerdevice is not verified (via OOB), an UnverifiedException will be
	* thrown.
	*/
	void writeChar(char c) throws UnverifiedException, IllegalStateException;
	
	/**
	* Sends the given double to the communicatonpartner.
	* If the partnerdevice is not verified (via OOB), an UnverifiedException will be
	* thrown.
	*/
	void writeDouble(double d) throws UnverifiedException, IllegalStateException;
	
	/**
	* Sends the given Object to the communicatonpartner.
	* If the partnerdevice is not verified (via OOB), an UnverifiedException will be
	* thrown.
	*/
	void writeObject(Object o) throws UnverifiedException, IllegalStateException;
	
	/**
	* Sends the given String to the communicatonpartner.
	* If the partnerdevice is not verified (via OOB), an UnverifiedException will be
	* thrown.
	*/
	void writeString(String s) throws UnverifiedException, IllegalStateException;
	
	/**
	* Reads bytearray send from the communicationpartner.
	* If blocking is true, this function blocks until the next entity is 
	* read.
	* If blocking is false, null is returned if there is no entity to read.
	*/
	byte[] read(boolean blocking) throws IllegalStateException, IllegalArgumentException;
	
	/**
	* Reads int send from the communicationpartner.
	* If blocking is true, this function blocks until the next entity is 
	* read.
	* If blocking is false, null is returned if there is no entity to read.
	* Throws IllegalArgumentException, if the read entity isn't a int.
	*/
	int readInt(boolean blocking) throws IllegalStateException, IllegalArgumentException;

	/**
	* Reads Object send from the communicationpartner.
	* If blocking is true, this function blocks until the next entity is 
	* read.
	* If blocking is false, null is returned if there is no entity to read.
	* Throws IllegalArgumentException, if the read entity isn't a int.
	*/
	Object readObject(boolean blocking) throws IllegalStateException, IllegalArgumentException;
	
	/**
	* Reads String send from the communicationpartner.
	* If blocking is true, this function blocks until the next entity is 
	* read.
	* If blocking is false, null is returned if there is no entity to read.
	* Throws IllegalArgumentException, if the read entity isn't a int.
	*/
	String readString(boolean blocking) throws IllegalStateException, IllegalArgumentException;
	
	/*
	...
	*/
	
	/**
	* Returns true, if there is something to read, false, if not.
	*/
	boolean hasNext();

	/**
	* Close the connection. 
	*/
	void close();
}
