package com.sujan.crisbox.crypto;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Cpt {
	public static final int MIN_LENGTH = 8;

	public static final int bufLen = 2097152;

	public static final String verName = "SCRAM2007"; // do not modify

	public static void scram(byte b[], long n, byte k[]) {
		for (int i = 0; i < n; i++) {
			b[i] = (byte) (b[i] ^ k[(i + 1) % k.length]);
		}
	}

	public static int checkVersion(RandomAccessFile r) {

		int res = -2;
		try {
			final byte[] cmt = verName.getBytes("UTF8"); // throws
			long len = r.length();
			byte b[] = new byte[bufLen];
			r.seek(len - cmt.length);
			long n = r.read(b, 0, cmt.length);
			boolean goodver = true;
			for (int i = 0; i < cmt.length; i++) {
				if (b[i] != cmt[i]) {
					goodver = false;
				}
			}
			if (!goodver) {
				res = -1;
			} else {
				res = 0;
			}
		} catch (Exception e) {
			System.out.println(verName + ": ERROR: " + e);
		} finally {
			return res;
		}
	}

	public static int checkVersion(File f) {

		int res = -2;
		try {
			final byte[] cmt = verName.getBytes("UTF8"); // throws
			RandomAccessFile r = new RandomAccessFile(f, "rwd");
			long len = r.length();
			byte b[] = new byte[bufLen];
			r.seek(len - cmt.length);
			long n = r.read(b, 0, cmt.length);
			boolean goodver = true;
			for (int i = 0; i < cmt.length; i++) {
				if (b[i] != cmt[i]) {
					goodver = false;
				}
			}
			r.close();
			if (!goodver) {
				res = -1;
			} else {
				res = 0;
			}
		} catch (Exception e) {
			System.out.println(verName + ": ERROR: " + e);
		} finally {
			return res;
		}
	}

	public static int checkPassword(File f, String pw) {
		int res = -2;
		long n;
		try {
			final byte[] cmt = verName.getBytes("UTF8"); // throws
			byte[] k = pw.getBytes("UTF8");
			RandomAccessFile r = new RandomAccessFile(f, "rwd");
			long len = r.length();
			byte b[] = new byte[bufLen];
			r.seek(len - cmt.length);
			n = r.read(b, 0, cmt.length);
			boolean goodver = true;
			for (int i = 0; i < cmt.length; i++) {
				if (b[i] != cmt[i]) {
					goodver = false;
				}
			}

			if (!goodver) {
				res = -2;
			} else {
				res = -1;
				r.seek(len - cmt.length - k.length);
				n = r.read(b, 0, k.length);
				scram(b, n, k);
				boolean goodk = true;
				for (int i = 0; i < k.length; i++) {
					if (b[i] != k[i]) {
						goodk = false;
					}
				}
				if (goodk) {
					res = 0;
				}
			}
			r.close();
		} catch (Exception e) {
			System.out.println(verName + ": ERROR: " + e);
		} finally {

			return res;
		}
	}

	/*
	 * boolean checkPassword(RandomAccessFile rf) { r.seek(len - cmt.length -
	 * k.length); n = r.read(b, 0, k.length); scram(b, n, k); boolean goodk =
	 * true; for ( int i=0; i<k.length; i++){ if ( b[i] != k[i] ) { goodk =
	 * false; } } return goodk; }
	 */

	public static long scramFile(File f, String pw, boolean enc) {

		long scanned = -1;
		RandomAccessFile r = null;
		try {
			final byte[] cmt = verName.getBytes("UTF8"); // throws
			r = new RandomAccessFile(f, "rwd");
			long len = r.length();
			long sec;
			if (pw.endsWith("\\m")) {
				sec = 10000000L;
			} else if (pw.endsWith("\\k")) {
				sec = 10000L;
			} else {
				sec = len;
			}
			if (len < sec) {
				sec = len;
			}

			int n;
			byte k[] = new byte[pw.length()];
			byte b[] = new byte[bufLen];

			k = pw.getBytes("UTF8");

			if (enc) {
				// System.out.println("Enc" + (char)k[k.length - 1]);
				for (long i = 0; i < sec; /* sec <= len */i += bufLen) {
					r.seek(i);
					n = r.read(b, 0, bufLen);
					scram(b, n, k);
					r.seek(i);
					r.write(b, 0, n);
				}

				r.seek(len);
				for (int i = 0; i < k.length; i++)
					b[i] = k[i];
				scram(b, k.length, k);
				r.write(b, 0, k.length);
				r.write(cmt, 0, cmt.length);
				scanned = sec;
			} else {
				boolean goodver = (checkVersion(r) == 0);

				if (!goodver) {
					Exception e = new Exception("File was not enc.d with "
							+ verName);
					throw e;
				}
				r.seek(len - cmt.length - k.length);
				n = r.read(b, 0, k.length);
				scram(b, n, k);
				boolean goodk = true;
				for (int i = 0; i < k.length; i++)
					if (b[i] != k[i])
						goodk = false;
				if (goodk) {
					long i;
					for (i = 0L; i < sec; i += bufLen) {
						r.seek(i);
						n = r.read(b, 0, bufLen);
						scram(b, n, k);
						r.seek(i);
						r.write(b, 0, n);
					}
					r.setLength(len - cmt.length - k.length);

					scanned = sec;// len - cmt.length - k.length;
				}
			}

		} catch (Exception e) {
			System.out.println(verName + ": Exception: " + e);
		} finally {
			try {
				if (r != null) {
					r.close();
				}
			} catch (Exception e) {

			}
			return scanned;
		}
	}
}
