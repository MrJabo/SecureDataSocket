package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import com.cryptolib.SecureDataSocket;
import java.security.Security;
import java.util.Arrays;

public class SecureDataSocketServerQR{
	public static void main(String [] args){
		try{
			SecureDataSocket cs = new SecureDataSocket(4711);
			System.out.println(cs.prepareServerWithClientCamera());
			cs.setupServerWithClientCamera();
			System.out.println("Write: "+cs.write("Hallo Client!".getBytes()));
			byte[] test = cs.read();
			System.out.println("Read!");
			System.out.println(new String(test, "UTF-8"));
			System.out.println("Write: "+cs.write(1337));
			//test = cs.read();
			System.out.println("Read!");
			System.out.println(cs.readFloat());
			double d = 18.4;
			System.out.println("Write: "+cs.write(d));
			System.out.println("Read!");
			System.out.println(((SerializationObject) cs.readObject()).x);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
