package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import com.cryptolib.FDESocket;
import java.util.Arrays;

public class FDEClient{
	public static void main(String [] args){
		try{
			FDESocket cs = new FDESocket(new Channel(ChannelType.WLAN, "127.0.0.1:4711"));
			boolean test = cs.connect();
			if (!test){
				System.out.println("Failed!");
				return;
			}

			byte[] testb = cs.read();
			System.out.println("Read!");
			System.out.println(new String(testb));
			//never do this! check OOB before verifying
			cs.verifiedOOB();
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
