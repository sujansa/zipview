//html>
//    images>
//    TOC.html
//    pic1.html
//    .
//    .

package com.sujan.crisbox.tools;

import java.io.*;
import javax.swing.*;
import java.awt.Dimension;

public class Gallery {
	String baseDir = ".";

	String baseName = "_";

	public Gallery(String dir) {
		baseDir = new String(dir);
		baseName = ((new String(dir)).replace("/", "_")).replace("\\", "_");
		if (!baseName.endsWith("_")) {
			baseName = baseName + "_";
		}
	}

	public void addImages(String imgStr[], int len) {

		/*
		 * File dirFile = (new File(baseDir)).getParentFile(); if (dirFile ==
		 * null) { System.out.println("null"); System.exit(1); }
		 */
		File dirFile = new File(".");
		File file = new File(dirFile, "TOC.html");
		PrintWriter out = null;
		int j = 0;
		int total_width = 0;
		for (int i = 0; i < len; i++) {
			System.out.println(imgStr[i]);
			if (i % 15 == 0) { // || total_width > 2000) {
				total_width = 0;
				if (i != 0) {

					out.println("</body></html>");
					out.close();
				}
				try {
					File cfile = new File(dirFile, baseName + "toc_" + (j++)
							+ ".html");
					out = new PrintWriter(new FileOutputStream(cfile, false));
				} catch (Exception e) {
					System.out.println("cannot : " + e);
					System.exit(1);
				}
				out
						.println("<html><head><title>Gallery</title></head><body bgcolor = \"black\">");
				out.println("<div align=\"right\">");
				out.println("<font color = \"red\">" + baseName + "toc_"
						+ (j - 1) + ".html" + "</font>");
				if (i >= 15) {
					out.println("<A HREF=\"" + baseName + "toc_" + (j - 2)
							+ ".html" + "\">Previous</A>");
				} else {
					out.println("Previous");
				}
				if (len - i > 15) { // how many more to add?
					out.println("<A HREF=\"" + baseName + "toc_" + (j)
							+ ".html" + "\">Next</A>");
				} else {
					out.println("Next");
				}

				out.println("</div>");

			}
			ImageIcon ic = new ImageIcon(baseDir + "/" + imgStr[i]);
			float height = (float) ic.getIconHeight();
			float width = (float) ic.getIconWidth();
			float icon_width, icon_height;

			Dimension obj = new Dimension((int) width, (int) height);
			Dimension ret = getFittingBox(new Dimension(200, 200), obj, true,
					true);
			// icon_width = (float) ret.getWidth();
			// icon_height = (float) ret.getHeight();

			icon_width = (width * 200.0F) / height; // don't change order
			icon_height = 200.0F;
			total_width += icon_width;

			float new_height = 500.0F;
			if (new_height > 4 * height) {
				new_height = 4 * height;
			}
			if (height < new_height) {
				width = (width * new_height) / height; // don't change order
				height = new_height;
			}
			out.println("<A HREF=\"" + baseName + imgStr[i] + ".html"
					+ "\"><IMG SRC=\"" + baseDir + "/" + imgStr[i]
					+ "\" width=\"" + (int) icon_width + "\" height = \""
					+ (int) icon_height + "\" BORDER=\"0\" ALT=\"" + imgStr[i]
					+ "\"></A>");

			PrintWriter hout = null;
			try {
				hout = new PrintWriter(new FileOutputStream(new File(dirFile,
						baseName + imgStr[i] + ".html"), false));
			} catch (Exception e) {
				System.out.println("cannot");
			}
			hout
					.println("<html><head><title>Gallery</title></head><body bgcolor = \"black\">"
							+ "<div align=\"right\">"
							+ "<font color = \"red\">"
							+ imgStr[i]
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>"
							+ "<A halign = \"left\" HREF=\""
							+ baseName
							+ "toc_"
							+ (j - 1)
							+ ".html"
							+ "\">back</A></div><IMG SRC=\""
							+ baseDir
							+ "/"
							+ imgStr[i]
							+ "\" width=\""
							+ (int) width
							+ "\" height = \""
							+ (int) height
							+ "\" BORDER=\"0\" ALT=\"back\"></body></html>");
			hout.close();
		}
		out.close();
	}

	public static Dimension getFittingBox(Dimension ref, Dimension obj,
			boolean bf, boolean zs) {
		Dimension ret = new Dimension(obj);
		boolean wider; // (c) Sujan S A 09AUG2007
		if (((float) ref.getWidth() / ref.getHeight()) > ((float) obj
				.getWidth() / obj.getHeight())) {
			wider = false;
		} else {
			wider = true;
		}
		if (bf) {
			if (ref.getHeight() < obj.getHeight()
					|| ref.getWidth() < obj.getWidth() || zs) {
				if (wider) {
					ret.setSize(ref.getWidth(), ret.getHeight());
					// ret = new Dimension(ref.getWidth(), obj.getHeight());
				} else {
					ret.setSize(ret.getWidth(), ref.getHeight());
					// ret = new Dimension(obj.getWidth(), ref.getHeight());
				}
			}
		}
		return ret;
	}

	public static void main(String args[]) {
		BufferedReader in = null;
		String[] imgStr = new String[10000];
		int i = 0;
		if (args.length == 0) {
			System.out.println("no args");
			System.exit(0);
		}
		/*
		 * try { in = new BufferedReader(new FileReader(args[0])); }
		 * catch(Exception e) { System.out.println("cannot : " + e);
		 * System.exit(1); }
		 * 
		 * 
		 * String line;
		 * 
		 * try { while ((line = in.readLine()) != null) { imgStr[i++] = line; }
		 * in.close();
		 */

		File dir = new File(args[0]);
		if (!dir.isDirectory()) {
			System.out.println("Not a dir");
			System.exit(1);
		}
		String[] fStr = dir.list();
		imgStr = fStr;
		i = fStr.length;
		Gallery g = new Gallery(args[0]);
		g.addImages(imgStr, i);
		// } catch (Exception e) { }
	}
}
