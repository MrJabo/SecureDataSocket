package com.cryptolib;

import java.io.Serializable;

public class SerializationObject implements Serializable {
	public int x;
	SerializationObject(int x){
		this.x = x;
	}
}
