package com.ibm.issac.toolkit.util;

public class NumericUtil {

	private final static char[] HEX = "0123456789abcdef".toCharArray();

	/**
	 * 将 int 类型数据转成二进制的字符串，不足 int 类型位数时在前面添“0”以凑足位数
	 * 
	 * @param num
	 * @return
	 */
	public static String toFullBinaryString(int num) {
		char[] chs = new char[Integer.SIZE];
		for (int i = 0; i < Integer.SIZE; i++) {
			chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
		}
		return new String(chs);
	}

	/**
	 * 将 int 类型数据转成十六进制的字符串，不足 int 类型位数时在前面添“0”以凑足位数
	 * 
	 * @param num
	 * @return
	 */
	public static String toFullHexString(int num) {
		char[] chs = new char[Integer.SIZE / 4]; // integer的SIZE是32
		for (int i = 0; i < chs.length; i++) {
			chs[chs.length - 1 - i] = HEX[(num >> (i * 4)) & 0xf];
		}
		return new String(chs);
	}

	/**
	 * MQ HEX每个数字只有2位
	 * 
	 * @param num
	 *            注意：必须是0-255表示的BYTE，JAVA默认BYTE范围为-127~128
	 * @return
	 */
	public static String toMQHexString(int num) {
		char[] chs = new char[Integer.SIZE / 16]; // integer的SIZE是32
		for (int i = 0; i < chs.length; i++) {
			chs[chs.length - 1 - i] = HEX[(num >> (i * 4)) & 0xf];
		}
		return new String(chs);
	}

	/**
	 * 把-127-128的BYTE类型转换为0-255表示的整数类型
	 * 
	 * @param b
	 * @return
	 */
	public static int toIntByte(byte b) {
		return -1; //现在没找到简单方法，在EncodingUtil里用ByteArrayInputStream解决了问题。
	}

	/**
	 * 将 long 类型数据转成二进制的字符串，不足 long 类型位数时在前面添“0”以凑足位数
	 * 
	 * @param num
	 * @return
	 */
	public static String toFullBinaryString(long num) {
		char[] chs = new char[Long.SIZE];
		for (int i = 0; i < Long.SIZE; i++) {
			chs[Long.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
		}
		return new String(chs);
	}

	/**
	 * 将 long 类型数据转成十六进制的字符串，不足 long 类型位数时在前面添“0”以凑足位数
	 * 
	 * @param num
	 * @return
	 */
	public static String toFullHexString(long num) {
		char[] chs = new char[Long.SIZE / 4];
		for (int i = 0; i < chs.length; i++) {
			chs[chs.length - 1 - i] = HEX[(int) ((num >> (i * 4)) & 0xf)];
		}
		return new String(chs);
	}
}
