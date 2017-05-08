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

import java.io.Serializable;

public interface VerificationInterface {
	
	/**
	 * internally setup Server; create QRCode; listen to Port in extra Thread.
	 * */
	public String getQRCode();

	
	/**
	 * setup client intern by reading ip-address, shared secret etc from QR-Code.
	 * */
	public void gotQRCode(String s);

	/**
	 * setup Server/Client with intern use of OOB-Commitment-Scheme. 
	 *
	 * String id has to be "ipaddress:port"
	 *
	 * If isServer is true, String id is used to block all Clients unless the one with the address given in id. If id is "" all clients are accepted.
	 *
	 * If isServer is false, String id is used to identify the Server.
	 * */
	public void prepareForComparablePhrases(boolean isServer, String id);
	public String getComparableVerificationPhrase();
	public void comparedPhrases(boolean equalPhrases);

	public ConnectionStatus getConnectionStatus();

	public void closeConnection();

	public void write(byte[] b);
	public byte[] readBytes();

	public void write(String s);
	public String readString();

	public void write(int i);
	public int readInt();

	public void write(float f);
	public float readFloat();

	public void write(double d);
	public double readDouble();

	public void write(Serializable s);
	public Serializable readObject();
}
