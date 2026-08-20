package com.sujan.crisbox.util;

import java.io.*;

/**
 * Contains .
 * 
 * @author Sujan S A
 * @version 1.0
 */
public class StreamCopier {
	public static final int LIM = 32768;

	/**
	 * .
	 * 
	 * @param .
	 * @return
	 */
	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		// Do not allow other threads to read from the input
		// or write to the output while copying is taking place
		synchronized (in) {
			synchronized (out) {
				byte[] buffer = new byte[LIM];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1)
						break;
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}

	/**
	 * .
	 * 
	 * @param .
	 * @return
	 */
	public static void copy(InputStream in, OutputStream out, int len)
			throws IOException {
		// Do not allow other threads to read from the input
		// or write to the output while copying is taking place
		int bytesCovered = 0;
		synchronized (in) {
			synchronized (out) {
				int n = len / LIM;
				byte[] buffer = new byte[LIM];
				bytesCovered = 0;
				for (int i = 0; bytesCovered < len; i++) {
					if ((len - bytesCovered) < LIM) {
						buffer = new byte[len - bytesCovered];
					}
					int bytesRead = in.read(buffer);
					if (bytesRead == -1)
						break;
					bytesCovered += bytesRead;
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}

	/**
	 * .
	 * 
	 * @param .
	 * @return
	 */
	public static byte[] getArray(InputStream in, int len) throws IOException {
		byte[] block = new byte[len];
		int pos;
		// Do not allow other threads to read from the input
		// or write to the output while copying is taking place
		synchronized (in) {
			// synchronized (out) {
			byte[] buffer = new byte[LIM];
			pos = 0;
			while (true) {
				int bytesRead = in.read(buffer);
				if (bytesRead == -1)
					break;
				System.arraycopy(buffer, 0, block, pos, bytesRead);
				pos += bytesRead;
			}
			return block;
			// }
		}
	}

	public static void main(String[] args) {

	}
}