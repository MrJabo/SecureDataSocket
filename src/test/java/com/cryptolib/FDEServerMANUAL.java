package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import com.cryptolib.FDESocket;
import java.security.Security;
import java.util.Arrays;

public class FDEServerMANUAL{
	public static void main(String [] args){
		try{
			/*FDESocket cs = new FDESocket(new Channel(ChannelType.MANUAL, ""));
			//never call setSharedSecret on both sides
			cs.setSharedSecret("00000000000000000000000000000000".getBytes());
			cs.listen(4711);
			//cs.verifiedOOB();
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
			System.out.println(((SerializationObject) cs.readObject()).x);*/
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
