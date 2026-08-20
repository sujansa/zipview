package com.sujan.crisbox.tools;

import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.io.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.*; //LinkedList<E>
import java.awt.event.*;
import java.net.URL;
import javax.swing.text.html.*;
import javax.swing.event.*;
import javax.swing.*; //Hyperlink event
import java.awt.Image;

/**
 * Contains some useful functions.
 * 
 * @author Sujan S A
 * @version 1.0
 */
public class SujOption {

	static JFrame consoleFrame = new JFrame("Debug Console");

	static JTextArea altOut = new JTextArea();

	static boolean isConsoleSet = false;

	static String logFileName = "crisboxGlobal.log";

	public static void initConsole() {
		if (isConsoleSet) {
			System.out.println("Alt Console already set.");
		}
		altOut.setEditable(false);
		consoleFrame.add(new JScrollPane(altOut));
		consoleFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				hideConsole();
			}
			/*
			 * public void windowClosed(WindowEvent event) { saveConsole(); }
			 */
		});
		consoleFrame.setSize(800, 200);
		consoleFrame.setLocation(0, 0);
		PrintStream consoleStream = new PrintStream(new OutputStream() {
			public void write(int b) {
			} // never used

			public void write(byte[] b, int off, int len) {
				altOut.append(new String(b, off, len));
			}
		}/* , true */); // autoflush has no effect
		System.setOut(consoleStream);
		System.setErr(consoleStream);
		isConsoleSet = true;
		// System.out.println("Alt Console ");
	}

	public static void showConsole() {
		consoleFrame.setVisible(true);
		consoleFrame.setState(JFrame.NORMAL);
	}

	public static void hideConsole() {
		consoleFrame.setVisible(false);
		// saveConsole();
	}

	public static void timeStampConsole(String str) {
		Calendar c = Calendar.getInstance();
		String dateStr = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH)
				+ "-" + c.get(Calendar.DATE) + " "
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND) + "  "
				+ c.getTimeZone().getDisplayName();
		altOut.append("\n----------------\n" + str + " Time: " + dateStr
				+ "\n----------------\n");
	}

	public static void console(String str) {
		altOut.append(str + "\n");
	}

	public static void saveConsole() {
		try {
			FileOutputStream fos = new FileOutputStream(logFileName, true);
			byte[] b = altOut.getText().getBytes("UTF8"); // throws
			fos.write(b);
			fos.close();
		} catch (Exception e) {
			System.out.println("Error: " + e); // will not be displayed anyhow
			JOptionPane.showOptionDialog(null, new JLabel("Cannot Save Log : "
					+ e), "Logging Error!", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE, null, null, null);
			// try { Thread.sleep(2000); } catch(Exception ex) {
			// System.out.println("Cannot sleep");}
		}
	}

	// public void printConsole()

	/*
	 * (c) Sujan S A 16 AUG 2007
	 */
	/**
	 * Obtains a file listing of a directory.
	 * 
	 * @param file
	 *            The directory to be listed.
	 * @return an array of File
	 */
	public static File[] fullList(File file) { // f[0] == file
		LinkedList<File> ll = new LinkedList<File>();
		ll.add(file);
		if (file.isDirectory()) {
			for (int i = 0; i < ll.size(); i++) {
				File f = ll.get(i);
				if (f.isDirectory()) {
					File[] fList = f.listFiles();
					for (int j = 0; j < fList.length; j++) {
						ll.add(fList[j]);
					}
				}
			}
		}
		return ll.toArray(new File[0]);
	}

	/**
	 * Resizes an image.
	 * 
	 * @param zipIC
	 *            Image to be resized.
	 * @param h
	 *            New height.
	 * @param w
	 *            New width.
	 * @param bf
	 *            Alway true.
	 * @param zs
	 *            If an image smaller than the new size should be zoomed.
	 * @param type
	 *            The scaling scheme to be used.
	 * @return the resized image.
	 */
	public static ImageIcon getBestfit(ImageIcon zipIC, int h, int w,
			boolean bf, boolean zs, int type) {
		Image img = zipIC.getImage();
		ImageIcon iicon = zipIC;

		boolean wider; // (c) Sujan S A 09AUG2007
		if (((float) w / h) > ((float) iicon.getIconWidth() / iicon
				.getIconHeight())) {
			wider = false;
		} else {
			wider = true;
		}
		if (bf) {
			if (h < iicon.getIconHeight() || w < iicon.getIconWidth() || zs) {
				if (wider) {
					img = img.getScaledInstance(w, -1, type);
					iicon = new ImageIcon(img);
				} else {
					img = img.getScaledInstance(-1, h, type);
					iicon = new ImageIcon(img);
				}
			}
		}
		return iicon;
	}

	public static ImageIcon getBestfit2(ImageIcon iicon, int w, int h,
			boolean zoomSmallp, int scaleType) {
		int h2 = iicon.getIconWidth();
		int w2 = iicon.getIconHeight();
		Image img = iicon.getImage();
		boolean wider; // (c) Sujan S A 09AUG2007
		if (((float) w / h) > ((float) w2 / h2)) {
			wider = false;
		} else {
			wider = true;
		}
		if (h < h2 || w < w2 || zoomSmallp) {
			if (wider) {
				img = img.getScaledInstance(w, -1, scaleType);
				iicon = new ImageIcon(img);
			} else {
				img = img.getScaledInstance(-1, h, scaleType);
				iicon = new ImageIcon(img);
			}
		}
		System.out.println("" + h + " " + w + " ");
		return iicon;
	}

	public static char showPasswordDialog(String str, String title, String def)[] {
		JPasswordField passwordField = new JPasswordField(def, 10);
		Object[] msg = new Object[2];
		msg[0] = str;
		msg[1] = passwordField;
		passwordField.selectAll();
		passwordField.requestFocusInWindow();
		passwordField.grabFocus();
		if (JOptionPane.showOptionDialog(null, msg, title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				new ImageIcon(SujOption.class
						.getResource("/com/sujan/res/icons/eyeG.class")), null,
				null) != JOptionPane.OK_OPTION)
			return null;
		return passwordField.getPassword();
	}

	public static JEditorPane createEditorPane(String url_of_file,
			String path_to_file) {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new Hyperactive());

		java.net.URL helpURL = null;

		try {
			URL cache_url = new URL("jar:" + url_of_file + "!/");
			URL file_url = new URL(cache_url, path_to_file);

			helpURL = file_url;
			// helpURL = TextSamplerDemo.class.getResource(
			// file_url.toString());
		} catch (Exception e) {
		}

		if (helpURL != null) {
			try {
				editorPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS,
						Boolean.TRUE);
				editorPane.putClientProperty(
						JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
				editorPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			System.err.println("Couldn't find file: " + url_of_file
					+ path_to_file);
		}
		return editorPane;
	}

	public static JEditorPane createEditorPane(URL helpURL) {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new Hyperactive());

		if (helpURL != null) {
			try {
				editorPane.putClientProperty(JEditorPane.W3C_LENGTH_UNITS,
						Boolean.TRUE);
				editorPane.putClientProperty(
						JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
				editorPane.setPage(helpURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + helpURL);
			}
		} else {
			System.err.println("Null url: Couldn't find file");// +
																// helpURL.toString());
		}
		return editorPane;
	}

}

class Hyperactive implements HyperlinkListener {

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			JEditorPane pane = (JEditorPane) e.getSource();
			if (e instanceof HTMLFrameHyperlinkEvent) {
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
				HTMLDocument doc = (HTMLDocument) pane.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			} else {
				try {
					pane.setPage(e.getURL());
				} catch (Throwable t) {
					// t.printStackTrace();
					// System.out.println("CB: Hyperlink generated Exception!");
					String link = "null:getURL";
					URL url = e.getURL();
					if (url != null) {
						link = e.getURL().toString();
					}
					String msgName = "Bad Hyperlink";
					String msg = "Hyperlink: " + link;
					System.out.println(msgName + ":" + msg);
					JOptionPane
							.showOptionDialog(
									null,
									new JLabel(msg),
									msgName,
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.ERROR_MESSAGE,
									/*
									 * new
									 * ImageIcon(ZipView.class.getResource("WizardG.class"))
									 */null,
									null, null);
				}
			}
		}
	}
}