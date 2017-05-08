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
import com.cryptolib.SecureDataSocket;
import java.security.Security;
import java.util.Arrays;

public class SecureDataSocketServerWLAN{
	public static void main(String [] args){
		try{
			SecureDataSocket cs = new SecureDataSocket(4711);
			System.out.println("SecureDataSocket initialisation finished!");
			System.out.println(cs.setupServerNoClientCamera());
			//Never do this without checking, if the phrases on both devices are the same. If they are different call cs.comparesPhrases(false)
			cs.comparedPhrases(true);
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
