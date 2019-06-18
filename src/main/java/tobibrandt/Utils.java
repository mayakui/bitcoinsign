package tobibrandt;

import java.math.BigInteger;

public class Utils {

	private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

	/**
	 * Concatenates the two given byte arrays and returns the combined byte
	 * array.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static byte[] concatenateByteArrays(byte[] first, byte[] second) {
		byte[] concatenatedBytes = new byte[first.length + second.length];

		System.arraycopy(first, 0, concatenatedBytes, 0, first.length);
		System.arraycopy(second, 0, concatenatedBytes, first.length, second.length);

		return concatenatedBytes;
	}

	private static byte[] decodeBase58To25Bytes(String input) {
		BigInteger num = BigInteger.ZERO;
		for (char t : input.toCharArray()) {
			int p = ALPHABET.indexOf(t);
			if (p == -1)
				return null;
			num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
		}

		byte[] result = new byte[25];
		byte[] numBytes = num.toByteArray();
		System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
		return result;
	}
}
