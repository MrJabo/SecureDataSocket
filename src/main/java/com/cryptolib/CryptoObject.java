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

import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Key;
import java.security.Provider;
import java.security.KeyPair;
import java.security.KeyFactory;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.AEADBadTagException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.KeyAgreement;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.ec.*;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;

import java.util.Arrays;

public class CryptoObject {
	private Provider provider = null;
	private KeyPair encKeypair = null;
	private SecretKeySpec sharedSecretSecond = null;
	private SecretKeySpec sharedSecretFirst = null;
	private Cipher enc = null;
	private Cipher dec = null;
	private String enc_algorithm = "";
	private String curve = "";
	private byte[] OOB = null;
	private boolean merged = false;
	private boolean has_symmetric_key = false;
	private CryptoCommitmentObject cc = null;
	private SecureRandom random = null;
	private int iv_size = 16;
	private int tag_size = 32;

	/** 
	* Constructor.
	* Create a new CryptoObject with encryption asymmetric elliptic curve25519 encryption keypair 
	* Short authentication byte length is 3 byte.
	*/
	public CryptoObject() throws CryptoSocketException {
		this("curve25519", "ECDH", 3, 16, 16);
	}

	/** 
	* Constructor.
	* Create a new CryptoObject with encryption asymmetric elliptic curve encryption keypair 
	* and digital sign asymmetric elliptic curve keypair.
	* curve specificies elliptic curve for encryption scheme and sign algorithm e.g. "curve25519"
	* enc_algorithm must be an implemented elliptic curve encryption algorithm e.g. "ECDH"
	* shortAuthenticationStringSize must be a positive number, that represents the short authentication byte length.
	* iv_size must be positiv, byte size of iv for encryption scheme
	* tag_size must be positiv, byte size of tag for encryption scheme
	*/
	public CryptoObject(String curve, String enc_algorithm, int shortAuthenticationStringSize, int iv_size, int tag_size) throws CryptoSocketException{
		if (0 >= shortAuthenticationStringSize || 0 >= iv_size || 0 >= tag_size){
			throw new CryptoSocketException("shortAuthenticationStringSize,iv_size and tag_size must be a positive number!");
		}

		try{
			X9ECParameters ecP = CustomNamedCurves.getByName(curve);
			org.bouncycastle.jce.spec.ECParameterSpec ecGenSpec = new org.bouncycastle.jce.spec.ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());
			this.provider = new BouncyCastleProvider();
			KeyPairGenerator g = KeyPairGenerator.getInstance(enc_algorithm, this.provider);
			this.random = new SecureRandom();
			g.initialize(ecGenSpec, this.random);
			this.encKeypair = g.generateKeyPair();

			if (this.encKeypair == null){
				throw new CryptoSocketException("Unable to create new key pair!");
			}

			this.OOB = new byte[shortAuthenticationStringSize];
			this.random.nextBytes(this.OOB);
		} catch(NoSuchAlgorithmException nsa){
			throw new CryptoSocketException("Algorithm is not supported!");
		} catch(InvalidAlgorithmParameterException iap){
			throw new CryptoSocketException("Wrong parameter for algorithm!");
		}

		this.enc_algorithm = enc_algorithm;
		this.curve = curve;
		this.iv_size = iv_size;
		this.tag_size = tag_size;
	}

	/**
	* Merge out of band challange.
	* For e.g. short authentication string this would merge the two strings together, that Alice and Bob should have the same string.
	* partner string from the other party.
	*/
	public void mergeOOB(byte[] partner) throws CryptoSocketException, IllegalStateException {
		if (this.OOB.length != partner.length){
			throw new CryptoSocketException("out of band challange has not the same length!");
		}

		if (this.merged){
			throw new IllegalStateException("you already merged the OOB challanges! You can't merge them twice!");
		}

		for(int i = 0; i < this.OOB.length; i++){
			this.OOB[i] = (byte) ((int) this.OOB[i] ^ (int) partner[i]);
		}

		this.merged = true;
	}

	/**
	* Get encryption offset (iv_size + tag_size)
	*/
	public int getOffset(){
		return this.iv_size + this.tag_size;
	}

	/**
	* Returns out of band challange, if merged, otherwise null.
	*/
	public byte[] getOOB(){
		if (this.merged){
			return this.OOB;
		} else {
			return null;
		}
	}

	/**
	* Get merge state.
	*/
	public boolean getMergeStatus(){
		return this.merged;
	}

	/**
	* Create new commitment for protocol exchange.
	*/
	public void createCryptoCommitment() throws CryptoSocketException, InvalidKeyException, NoSuchAlgorithmException {
		BCECPublicKey pk = (BCECPublicKey) (this.encKeypair.getPublic());
		int publicKeySize = pk.getQ().getEncoded(true).length - 1;
		byte[] message = new byte[publicKeySize + this.OOB.length];
		System.arraycopy(pk.getQ().getEncoded(true), 1, message, 0, publicKeySize);
		System.arraycopy(this.OOB, 0, message, publicKeySize, this.OOB.length);
		this.cc = new CryptoCommitmentObject(message);
	}

	/**
	* Get commitment object.
	*/
	public CryptoCommitmentObject getCryptoCommitment(){
		return this.cc;
	}

	/**
	* Open commitment and extract message to create shared secret.
	*/
	public void openCommitmentAndCreateSharedSecret(byte [] decommitment) throws CryptoSocketException, InvalidKeyException, NoSuchAlgorithmException{
		this.cc.open(decommitment);

		try{
			BCECPublicKey mypk = (BCECPublicKey) (this.encKeypair.getPublic());
			int publicKeySize = mypk.getQ().getEncoded(true).length - 1;
			byte[] message =  this.cc.getOtherMessage();

			if (message.length != publicKeySize + this.OOB.length){
				throw new CryptoSocketException("Message size is wrong!");
			}

			byte[] otherPK = new byte[publicKeySize + 1];

			//compressed encoding magic byte
			otherPK[0] = (byte) 0x02;
			byte[] otherOOB = new byte[this.OOB.length];
			System.arraycopy(message, 0, otherPK, 1, publicKeySize);
			System.arraycopy(message, publicKeySize, otherOOB, 0, otherOOB.length);
			X9ECParameters ecP = CustomNamedCurves.getByName(curve);
			org.bouncycastle.jce.spec.ECParameterSpec ecGenSpec = new org.bouncycastle.jce.spec.ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH());
			//ECNamedCurveParameterSpec ecP = ECNamedCurveTable.getParameterSpec(this.curve);
			ECPublicKeySpec pubKey = new ECPublicKeySpec(ecP.getCurve().decodePoint(otherPK), ecGenSpec);
			KeyFactory kf = KeyFactory.getInstance(this.enc_algorithm, new BouncyCastleProvider());
			ECPublicKey pk = (ECPublicKey) kf.generatePublic(pubKey);
			createSharedEncKey(pk);
			mergeOOB(otherOOB);
		} catch(NoSuchAlgorithmException nsa){
			throw new CryptoSocketException("Algorithm is not supported!");
		} catch(InvalidKeySpecException iks){
			throw new CryptoSocketException("Wrong parameter for algorithm!");
		}
	}

	/**
	* Performs ECDH
	*/
	public void createSharedEncKey(ECPublicKey key) throws CryptoSocketException {
		try {
			X9ECParameters ecP = CustomNamedCurves.getByName(curve);
			ECDomainParameters ecdp = new ECDomainParameters(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH());
			ECPublicKeyParameters ecpkp = new ECPublicKeyParameters(key.getQ(), ecdp);
			BCECPrivateKey sk = (BCECPrivateKey) this.encKeypair.getPrivate();
			ECPrivateKeyParameters ecskp = new ECPrivateKeyParameters(sk.getD() , ecdp);
			ECDHCBasicAgreement ba = new ECDHCBasicAgreement();
			ba.init(ecskp);
			byte[] byteSharedSecret = ba.calculateAgreement(ecpkp).toByteArray();
			byte[] byteSharedSecretSecond = new byte[byteSharedSecret.length/2];
			byte[] byteSharedSecretFirst = new byte[byteSharedSecret.length/2];
			System.arraycopy(byteSharedSecret, 0, byteSharedSecretSecond, 0, byteSharedSecretSecond.length);
			System.arraycopy(byteSharedSecret, byteSharedSecretSecond.length, byteSharedSecretFirst, 0, byteSharedSecretFirst.length);
			this.sharedSecretFirst = new SecretKeySpec(byteSharedSecretFirst, "AES");
			this.sharedSecretSecond = new SecretKeySpec(byteSharedSecretSecond, "AES");
			this.has_symmetric_key = true;
			this.enc = Cipher.getInstance("AES/GCM/NoPadding");
			this.dec = Cipher.getInstance("AES/GCM/NoPadding");
		} catch(IllegalStateException is){
			throw new CryptoSocketException("unable to create shared encryption key, wrong state!");
		} catch(NoSuchAlgorithmException nsa){
			throw new CryptoSocketException("Encryption algorithm not found!");
		} catch (NoSuchPaddingException nsp){
			throw new CryptoSocketException("Invalid padding algorithm!");
		} 
	}


	/**
	 * set a already known sharedsecret, instead of using commitment to create one
	 * */
	public void setSharedSecret(byte[] sharedSecret) throws CryptoSocketException {
		if (sharedSecret.length != 32){
			throw new CryptoSocketException("invalid sharedSecret-size; has to have a length of 32 bytes");
		}
		try {
			byte[] byteSharedSecretSecond = new byte[sharedSecret.length/2];
			byte[] byteSharedSecretFirst = new byte[sharedSecret.length/2];
			System.arraycopy(sharedSecret, 0, byteSharedSecretSecond, 0, byteSharedSecretSecond.length);
			System.arraycopy(sharedSecret, byteSharedSecretSecond.length, byteSharedSecretFirst, 0, byteSharedSecretFirst.length);
			this.sharedSecretFirst = new SecretKeySpec(byteSharedSecretFirst, "AES");
			this.sharedSecretSecond = new SecretKeySpec(byteSharedSecretSecond, "AES");
			this.has_symmetric_key = true;
			this.enc = Cipher.getInstance("AES/GCM/NoPadding");
			this.dec = Cipher.getInstance("AES/GCM/NoPadding");
		} catch(IllegalStateException is){
			throw new CryptoSocketException("unable to create shared encryption key, wrong state!");
		} catch(NoSuchAlgorithmException nsa){
			throw new CryptoSocketException("Encryption algorithm not found!");
		} catch (NoSuchPaddingException nsp){
			throw new CryptoSocketException("Invalid padding algorithm!");
		}
		
	}


	/**
	* Encrypt data with AES-GCM mode.
	* Encrypt data array and returns ciphertext.
	*/

	public byte[] encrypt(byte [] data) throws IllegalStateException, CryptoSocketException, IllegalBlockSizeException {
		if (!this.has_symmetric_key){
			throw new IllegalStateException("Have no symmetric key, you need to create a shared secret first!");
		}

		if (data == null){
			throw new CryptoSocketException("No data found for encryption");
		}

		byte[] iv = new byte[this.iv_size];
		byte[] output = null;
		byte[] encryptData = null;
		this.random.nextBytes(iv);
		try {
			this.enc.init(Cipher.ENCRYPT_MODE, this.sharedSecretFirst, new GCMParameterSpec(this.tag_size * 8, iv));
			encryptData = this.enc.doFinal(data);
			output = new byte[this.iv_size + encryptData.length];
			System.arraycopy(iv, 0, output, 0, this.iv_size);
			System.arraycopy(encryptData, 0, output, this.iv_size, encryptData.length);
		} catch(AEADBadTagException abt){
			throw new CryptoSocketException("Decryption exception? Impossible!");
		} catch(BadPaddingException bp){
			throw new CryptoSocketException("Padding exception? Impossible!");
		} catch (IllegalStateException ise){
			throw new IllegalStateException("Wrong AES cipher use!");
		} catch (InvalidKeyException ik){
			ik.printStackTrace();
			throw new CryptoSocketException("Invalid key for encryption!");
		} catch (InvalidAlgorithmParameterException iap){
			iap.printStackTrace();
			throw new CryptoSocketException("Encryption parameters are wrong!");
		}

		return output;
	}

	/**
	* Decrypt data with AES-GCM mode.
	* Decrypt data array and return message, if tag was valid, otherwise null.
	*/

	public byte[] decrypt(byte [] data) throws IllegalStateException, CryptoSocketException, IllegalBlockSizeException {
		if (!this.has_symmetric_key){
			throw new IllegalStateException("Have no symmetric key, you need to create a shared secret first!");
		}

		if (data.length <= this.iv_size + this.tag_size || data == null){
			throw new CryptoSocketException("The data are too small for a ciphertext!");
		}

		byte[] iv = new byte[this.iv_size];
		byte[] ciphertext = new byte[data.length - this.iv_size];
		System.arraycopy(data, 0, iv, 0, this.iv_size);
		System.arraycopy(data, this.iv_size, ciphertext, 0, ciphertext.length);
		byte[] decryptData = null;
		try {
			this.dec.init(Cipher.DECRYPT_MODE, this.sharedSecretFirst, new GCMParameterSpec(this.tag_size * 8, iv));
			decryptData = this.dec.doFinal(ciphertext);
		} catch (AEADBadTagException abt){
			return null;
		} catch (BadPaddingException bp){
			throw new CryptoSocketException("Padding exception? Impossible!");
		} catch (IllegalStateException ibs){
			throw new IllegalStateException("Wrong AES cipher use!");
		} catch (InvalidKeyException ik){
			throw new CryptoSocketException("Invalid key for decryption!");
		} catch (InvalidAlgorithmParameterException iap){
			throw new CryptoSocketException("Decryption parameters are wrong!");
		}

		return decryptData;
	}
}
