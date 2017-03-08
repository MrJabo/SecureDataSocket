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

import java.io.PrintWriter;
import java.io.StringWriter;

public class SecureDataSocketException extends Exception {

	private Exception originalException;
	private boolean critical = false;

	SecureDataSocketException(String description, Exception originalException) {
		this(description, originalException, true);	
	}

	SecureDataSocketException(String description, Exception originalException, boolean critical) {
		super(description+"\n"+getStackTrace(originalException));
		this.critical = critical;
		this.originalException = originalException;
	}

	private static String getStackTrace(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
