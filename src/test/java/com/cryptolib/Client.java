package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import java.util.Arrays;

public class Client{
	public static void main(String [] args){
		try{
			CryptoSocket cs = new CryptoSocket(new Channel(ChannelType.WLAN, "127.0.0.1:4711"));
			boolean test = cs.connect();
			if (!test){
				System.out.println("Failed!");
				return;
			}

			byte[] testb = new byte[13];
			System.out.println("Read: "+cs.read(true, testb));
			System.out.println(new String(testb));
			cs.verifiedOOB();
			System.out.println("Write: "+cs.write("Hallo".getBytes()));
			System.out.println("Read: "+cs.read(true, testb));
			System.out.println(new String(testb));
			System.out.println("Write: "+cs.write("Hallo".getBytes()));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
