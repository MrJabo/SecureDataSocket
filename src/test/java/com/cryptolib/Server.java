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
