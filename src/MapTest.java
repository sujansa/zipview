import java.util.*;
import java.util.zip.*;
import java.io.*;

public class MapTest {

	Map<String, ZipEntry> entries = new HashMap<String, ZipEntry>();

	File source_file;

	ZipFile source;

	public void open(String jar_file_path) {
		source_file = new File(jar_file_path);
		try {
			source = new ZipFile(jar_file_path);

			// Transfer all the zip entries into local memory to make
			// them easier to access and manipulate.
			for (Enumeration e = source.entries(); e.hasMoreElements();) {
				ZipEntry current = (ZipEntry) e.nextElement();
				entries.put(current.getName(), current);
			}
		} catch (Exception e) // Assume file doen't exist
		{
			source = null; // Since the "entries" list will be
		} // empty,
	}

	public void close() {
		try {
			if (source != null) {// there is a source archive
				source.close();
			}
		} catch (Exception e) {
		}
	}

	public static void main(String args[]) {
		MapTest mt = new MapTest();
		mt.open(args[0]);
		Set set = mt.entries.keySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
		mt.close();
	}
}
