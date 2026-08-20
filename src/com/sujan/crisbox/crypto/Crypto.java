//Sujan S A First DES lib 24 JUL 2007

package com.sujan.crisbox.crypto;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Crypto {
	public static boolean encrypt(String infile, String outfile, String password) {

		boolean flag = false;
		String filename = infile;
		if (password.length() < 8) {
			System.err
					.println("Password must be at least eight characters long");
		}
		try {
			FileInputStream fin = new FileInputStream(infile);
			FileOutputStream fout = new FileOutputStream(outfile);
			// Create a key.
			byte[] desKeyData = password.getBytes();
			DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = keyFactory.generateSecret(desKeySpec);
			// Use Data Encryption Standard.
			Cipher des = Cipher.getInstance("DES/CBC/PKCS5Padding");
			des.init(Cipher.ENCRYPT_MODE, desKey);
			// Write the initialization vector onto the output.
			byte[] iv = des.getIV();
			DataOutputStream dout = new DataOutputStream(fout);
			dout.writeInt(iv.length);
			dout.write(iv);
			byte[] input = new byte[64];
			while (true) {
				int bytesRead = fin.read(input);
				if (bytesRead == -1)
					break;
				byte[] output = des.update(input, 0, bytesRead);
				if (output != null)
					dout.write(output);
			}
			byte[] output = des.doFinal();
			if (output != null)
				dout.write(output);
			fin.close();
			dout.flush();
			dout.close();
			flag = true;
		} catch (InvalidKeySpecException e) {
			System.err.println(e);
		} catch (InvalidKeyException e) {
			System.err.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			System.err.println(e);
		} catch (BadPaddingException e) {
			System.err.println(e);
		} catch (IllegalBlockSizeException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			return flag;
		}
	}

	public static boolean decrypt(String infile, String outfile, String password) {

		boolean flag = false;
		if (password.length() < 8) {
			System.err
					.println("Password must be at least eight characters long");
		}
		try {
			FileInputStream fin = new FileInputStream(infile);
			FileOutputStream fout = new FileOutputStream(outfile);
			// Create a key.
			byte[] desKeyData = password.getBytes();
			DESKeySpec desKeySpec = new DESKeySpec(desKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey desKey = keyFactory.generateSecret(desKeySpec);
			// Read the initialization vector.
			DataInputStream din = new DataInputStream(fin);
			int ivSize = din.readInt();
			byte[] iv = new byte[ivSize];
			din.readFully(iv);
			IvParameterSpec ivps = new IvParameterSpec(iv);
			// Use Data Encryption Standard.
			Cipher des = Cipher.getInstance("DES/CBC/PKCS5Padding");
			des.init(Cipher.DECRYPT_MODE, desKey, ivps);
			byte[] input = new byte[64];
			while (true) {
				int bytesRead = fin.read(input);
				if (bytesRead == -1)
					break;
				byte[] output = des.update(input, 0, bytesRead);
				if (output != null)
					fout.write(output);
			}
			byte[] output = des.doFinal();
			if (output != null)
				fout.write(output);
			fin.close();
			fout.flush();
			fout.close();
			flag = true;
		} catch (InvalidKeySpecException e) {
			System.err.println(e);
		} catch (InvalidKeyException e) {
			System.err.println(e);
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println(e);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			System.err.println(e);
		} catch (BadPaddingException e) {
			System.err.println(e);
		} catch (IllegalBlockSizeException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			return flag;
		}
	}
}
