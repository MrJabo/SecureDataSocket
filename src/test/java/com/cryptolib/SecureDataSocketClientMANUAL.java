package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import com.cryptolib.SecureDataSocket;
import java.util.Arrays;

public class SecureDataSocketClientMANUAL{
	public static void main(String [] args){
		try{
			SecureDataSocket cs = new SecureDataSocket(4711);
			cs.setupClientWithCamera("192.168.178.34:4711:0Yknu8HioYL4CZ3XPkxguiXlIdbIf+jblHmNMLK6cpM="); //insert the output of the server here

			byte[] testb = cs.read();
			System.out.println("Read!");
			System.out.println(new String(testb));
			//cs.verifiedOOB();
			System.out.println("Write: "+cs.write("Hallo"));
			int i = cs.readInt();
			System.out.println("Read!");
			System.out.println(i);
			System.out.println("Write: "+cs.write(3.45f));
			System.out.println("Read!");
			System.out.println(cs.readDouble());
			System.out.println("Write: "+cs.write(new SerializationObject(5)));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
