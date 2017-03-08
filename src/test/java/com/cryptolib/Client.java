/*     This file is part of SecureDataSocket
	   Copyright (C) 2017 Jakob Bode and Matthias Sekul

	   SecureDataSocket is free software: you can redistribute it and/or modify
	   it under the terms of the GNU General Public License as published by
	   the Free Software Foundation, either version 3 of the License, or
	   (at your option) any later version.

	   SecureDataSocket is distributed in the hope that it will be useful,
	   but WITHOUT ANY WARRANTY; without even the implied warranty of
	   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	   GNU General Public License for more details.

	   You should have received a copy of the GNU General Public License
	   along with SecureDataSocket.  If not, see <http://www.gnu.org/licenses/>*/

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
