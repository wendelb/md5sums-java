package de.bwendel.java;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5sum {
	// http://stackoverflow.com/a/304275
	private static byte[] createChecksum(String filename) throws Exception  {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		}
		while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	// see this How-to for a faster way to convert
	// a byte array to a HEX string
	private static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	private static String getChecksumLine(String filename) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(getMD5Checksum(filename));
		sb.append("   ");
		sb.append(filename);
		
		return sb.toString();
	}
	

	public static void main(String[] args) {
		if (args.length == 0) {
			showUsage();
			System.exit(-1);
		}
				
		try {
			for (String item: args) {
				File file = new File(item);
				
				if (!file.exists()) {
					System.err.println(item + " does not exists - skipping");
					continue;
				}
				else if (file.isDirectory()) {
					//walkDirectory(file);
				}
				else if (file.isFile()) {
					System.out.println(getChecksumLine(item));
				}
				else {
					System.err.println("This should never happen!");
				}
			}
		}
		catch (Exception e) {
			System.err.println("Oups - an error occured");
			e.printStackTrace();
			System.err.println("Exiting");
			System.exit(-2);
		}
	}

	private static void showUsage() {
		// ...
		System.err.println("MD5sum - compute MD5 message digest (Java Version)");
		System.err.println("by Bernhard Wendel");
		System.err.println("");
		System.err.println("Usage: md5sum.jar [FILE] ...");
	}

}
