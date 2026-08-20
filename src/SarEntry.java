/*
 ** headerSize>offset>itemSize>date>name>metaData>>
 */
/**
 * Contains .
 * 
 * @author Sujan S A
 * @version 1.0
 */
public class SarEntry {
	public final String SAR_DELIM = ">";

	private int headerSize;

	private int offset;

	private int itemSize;

	private String date; // yyyy//mm//dd//24hr//min//sec

	private String name;

	private String metaData; /*
								 * Trash, isDir, comment, password,
								 * keywords,warning, triggers, thumbnail,
								 */

	public SarEntry() {

	}

	public SarEntry(int offset, int itemSize, String date, String name,
			String data) {
		this.offset = offset;
		this.itemSize = itemSize;
		this.date = date;
		this.name = name;
		this.metaData = data;
	}

	public int getHeaderSize() {
		getByteArray();
		return headerSize;
	}

	public int getOffset() {
		return offset;
	}

	public int getItemSize() {
		return itemSize;
	}

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String getMetaData() {
		return metaData;
	}

	public void setOffset(int offset) {
		this.offset = offset;

	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
		// System.out.println(this.name + "+");
	}

	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public byte[] getByteArray() {
		String str = date + SAR_DELIM + name + SAR_DELIM + metaData + SAR_DELIM
				+ SAR_DELIM;
		byte[] byteArr = null;
		try {
			byteArr = str.getBytes("UTF8");
		} catch (Exception e) {
			System.out.println("Excep: " + e);
			return null;
		}
		headerSize = byteArr.length + 3 * 4;
		byte[] arr = new byte[headerSize];
		writeIntBytes(arr, 0, headerSize);
		writeIntBytes(arr, 4, offset);
		writeIntBytes(arr, 8, itemSize);
		System.arraycopy(byteArr, 0, arr, 3 * 4, byteArr.length);
		return arr;
	}

	public void setByteArray(byte[] data) {
		headerSize = readIntBytes(data, 0);
		offset = readIntBytes(data, 4);
		itemSize = readIntBytes(data, 8);
		String str = null;
		try {
			str = new String(data, 3 * 4, headerSize - 3 * 4, "UTF8");
		} catch (Exception e) {
			System.out.println("Excep: " + e);
			return;
		}
		String[] strArr = str.split(SAR_DELIM);
		this.date = strArr[0];
		this.name = strArr[1];
		this.metaData = strArr[2];
	}

	/**
	 * .
	 * 
	 * @param .
	 * @return
	 */
	public static void writeIntBytes(byte[] block, int start, int val) {
		int rem, quot;
		if (val < 0) {
			quot = -val;
		} else {
			quot = val;
		}
		for (int i = 3; i >= 0; i--) {
			rem = quot % 128;
			quot = quot / 128;
			block[start + i] = (byte) rem;
		}
	}

	/**
	 * .
	 * 
	 * @param .
	 * @return
	 */
	public static int readIntBytes(byte[] block, int start) {
		return block[start + 0] * 128 * 128 * 128 + block[start + 1] * 128
				* 128 + block[start + 2] * 128 + block[start + 3] * 1;
	}
}