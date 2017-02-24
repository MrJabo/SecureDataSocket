package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import java.security.Security;
import java.util.Arrays;

public class Server{
	public static void main(String [] args){
		try{
			CryptoSocket cs = new CryptoSocket(new Channel(ChannelType.WLAN,""));
			cs.listen(4711);
			cs.verifiedOOB();
			System.out.println("Write: "+cs.write("Hallo Client!".getBytes()));
			byte[] test = new byte[5];
			System.out.println("Read: "+cs.read(true, test));
			System.out.println(new String(test, "UTF-8"));
			System.out.println("Write: "+cs.write("Hallo Client!".getBytes()));
			System.out.println("Read: "+cs.read(true, test));
			System.out.println(new String(test, "UTF-8"));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
