package com.cryptolib;

import com.cryptolib.FDESocket;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

public class SecureDataSocket {
	
	private int port;
	private FDESocket socket;
	private SocketAddress connectionInfo;	

	public SecureDataSocket(int port){
		this.port = port;
	}

	/**
	 * setup client with commitmentscheme.
	 * connectionDetails have to look like ipAddress:port
	 *
	 * compare the returned String with the one on the other device. call comparedPhrases(stringsWereEqual) afterwards.
	 * */
	public String setupClientNoCamera(String connectionDetails) throws SecureDataSocketException {
		//commitment
		try{ 
			this.socket = new FDESocket(new Channel(ChannelType.WLAN, connectionDetails));
			this.socket.connect();
			return byteArrayToPhrase(this.socket.getOOB());
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
	}

	/**
	 * compare
	 * */
	public void comparedPhrases(boolean phrasesMatched) throws SecureDataSocketException {
		if (phrasesMatched) {
			try{
				this.socket.verifiedOOB();
			} catch(Exception e) {
				throw new SecureDataSocketException(e.toString(), e);
			}
		}
		else {
			this.close();
		}
	}

	/**
	 * setup client with already known sharedSecret.
	 * connectionDetails have to look like ipAddress:port:sharedSecret
	 *
	 * Connection established afterwards.
	 * */
	public void setupClientWithCamera(String connectionDetails) throws SecureDataSocketException {
		try {
			this.socket = new FDESocket(new Channel(ChannelType.MANUAL, connectionDetails));
			this.socket.connect();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
	}


	/**
	 * setup Server with commitmentscheme.
	 * Method blocks until a client connected.
	 *
	 * compare the returned String with the one on the other device. call comparedPhrases(stringsWereEqual) afterwards.
	 * */
	public String setupServerNoClientCamera() throws SecureDataSocketException {
		try{
			this.socket.listen(this.port);
			return byteArrayToPhrase(this.socket.getOOB());
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
	}


	/**
	 * call setupServerWithClientCamera() afterwards.
	 *
	 * returns the connectiondetails and the sharedSecret, that has to be transferred securely to the client by the user.
	 * */
	public String prepareServerWithClientCamera() throws SecureDataSocketException {
		try {
			this.socket = new FDESocket(new Channel(ChannelType.MANUAL, "::"));
			return getIPAddress(true) + ":" + this.port + ":" + this.socket.createSharedSecret();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
	}


	/**
	 * Method blocks until a client connected
	 * */
	public void setupServerWithClientCamera() throws SecureDataSocketException {
		try{
			this.socket.listen(this.port);
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
	}

	private String byteArrayToPhrase(byte[] bytes) throws IOException {
		//TODO create phrases instead of hexstring
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	
	public byte[] read() throws SecureDataSocketException {
		byte[] read;
		try {	
			read = this.socket.read();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
		return read; 
	}

	public String readString() throws SecureDataSocketException {
		String read;
		try {	
			read = this.socket.readString();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
		return read; 
	}
	
	public int readInt() throws SecureDataSocketException {
		int read;
		try {	
			read = this.socket.readInt();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
		return read; 
	}
	
	public float readFloat() throws SecureDataSocketException {
		float read;
		try {	
			read = this.socket.readFloat();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
		return read; 
	}

	public double readDouble() throws SecureDataSocketException {
		double read;
		try {	
			read = this.socket.readDouble();
		} catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
		return read; 
	}

	public Serializable readObject() throws SecureDataSocketException, ClassNotFoundException {
		Serializable read;
		try {	
			read = this.socket.readObject();
		} 
		catch(ClassNotFoundException e){
			throw e;
		}
		catch(Exception e) {
			throw new SecureDataSocketException(e.toString(), e);
		}
		return read; 
	}

	public int write(byte[] array) throws SecureDataSocketException {
		int ret = 0;
		try {
			ret = this.socket.write(array);
		}
		catch(Exception e){
			throw new SecureDataSocketException(e.toString(), e);
		}
		return ret;
	}

	public int write(int i) throws SecureDataSocketException {
		int ret = 0;
		try {
			ret = this.socket.write(i);
		}
		catch(Exception e){
			throw new SecureDataSocketException(e.toString(), e);
		}
		return ret;
	}

	public int write(Float f) throws SecureDataSocketException {
		int ret = 0;
		try {
			ret = this.socket.write(f);
		}
		catch(Exception e){
			throw new SecureDataSocketException(e.toString(), e);
		}
		return ret;
	}

	public int write(Double d) throws SecureDataSocketException {
		int ret = 0;
		try {
			ret = this.socket.write(d);
		}
		catch(Exception e){
			throw new SecureDataSocketException(e.toString(), e);
		}
		return ret;
	}

	public int write(String s) throws SecureDataSocketException {
		int ret = 0;
		try {
			ret = this.socket.write(s);
		}
		catch(Exception e){
			throw new SecureDataSocketException(e.toString(), e);
		}
		return ret;
	}

	public int write(Serializable s)  throws SecureDataSocketException {
		int ret = 0;
		try {
			ret = this.socket.write(s);
		}
		catch(Exception e){
			throw new SecureDataSocketException(e.toString(), e);
		}
		return ret;
	}

	public void close() {
		this.socket.close();
	}

	/**
	 * from: http://stackoverflow.com/a/13007325
	 *
	 * Get IP address from first non-localhost interface
	 * @param useIPv4  true=return ipv4, false=return ipv6
	 * @return  address or empty string
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						//boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						boolean isIPv4 = sAddr.indexOf(':')<0;

						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
								return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
							}
						}
					}
				}
			}
		} catch (Exception ex) { } // for now eat exceptions
		return "";
	}
}
