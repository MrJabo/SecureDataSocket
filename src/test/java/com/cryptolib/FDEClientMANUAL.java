/*
 * This file is part of SecureDataSocket
 * Copyright (C) 2017 Jakob Bode and Matthias Sekul
 *
 * SecureDataSocket is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SecureDataSocket is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SecureDataSocket.  If not, see <http://www.gnu.org/licenses/>
 */

package com.cryptolib;

import com.cryptolib.CryptoSocketInterface;
import com.cryptolib.CryptoSocketInterface.Channel;
import com.cryptolib.CryptoSocketInterface.ChannelType;
import com.cryptolib.CryptoSocket;
import com.cryptolib.FDESocket;
import java.util.Arrays;

public class FDEClientMANUAL{
	public static void main(String [] args){
		try{
			/*FDESocket cs = new FDESocket(new Channel(ChannelType.MANUAL, "127.0.0.1:4711"));
			//never call setSharedSecret on both sides
			cs.setSharedSecret("00000000000000000000000000000000".getBytes());
			boolean test = cs.connect();
			if (!test){
				System.out.println("Failed!");
				return;
			}

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
			System.out.println("Write: "+cs.write(new SerializationObject(5)));*/
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
