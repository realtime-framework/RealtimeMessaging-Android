/**
 * @fileoverview This file contains the implementation of some Strings utilities methods
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.api;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static class containing the implementation of some Strings utilities methods
 * 
 * @version 2.1.0 27 Mar 2013
 * @author ORTC team members (ortc@ibt.pt) 
 */
public class Strings {
	/**
	 * Checks if a string is null or an empty text
	 * 
	 * @param text
	 *            Text to check
	 * @return boolean True if text is null or empty otherwise false
	 */
	public static boolean isNullOrEmpty(String text) {
        return text == null || text.trim().equals("");
	}

	private static final char[] symbols = new char[36];

	static {
        // CAUSE: For Loops Should Use Braces
        for (int idx = 0; idx < 10; ++idx) {
			symbols[idx] = (char) ('0' + idx);
        }
        // CAUSE: For Loops Should Use Braces
        for (int idx = 10; idx < 36; ++idx) {
			symbols[idx] = (char) ('a' + idx - 10);
	}
    }

	private static final Random random = new Random();

	/**
	 * Generates a random alphanumeric string
	 * @param length Number of characters the random string contains
	 * @return String with the specified length
	 */
	public static String randomString(int length) {
        // CAUSE: If-Else Statements Should Use Braces
        if (length < 1) {
            throw new IllegalArgumentException(String.format("length < 1: %s", length));
        }

		char[] buf = new char[length];

		return nextString(buf);
	}

	private static String nextString(char[] buf) {
        // CAUSE: For Loops Should Use Braces
        for (int idx = 0; idx < buf.length; ++idx) {
			buf[idx] = symbols[random.nextInt(symbols.length)];
        }
		return new String(buf);
	}
	
	public static boolean ortcIsValidInput(String value) {
		Pattern regexPattern = Pattern.compile("^[\\w-:\\/.]*$");
		Matcher matcher = regexPattern.matcher(value);
		
		return (matcher != null && matcher.matches()) ? true : false;
	}
	
	public static boolean ortcIsValidChannelForNotifications(String value) {
		Pattern regexPattern = Pattern.compile("^[\\w-:]*$");
		Matcher matcher = regexPattern.matcher(value);
		
		return (matcher != null && matcher.matches()) ? true : false;
	}
	
	public static boolean ortcIsValidUrl(String value) {
		Pattern regexPattern = Pattern.compile("^\\s*(http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?\\s*$");
		Matcher matcher = regexPattern.matcher(value);
		
		return (matcher != null && matcher.matches()) ? true : false;
	}
	
	public static String treatUrl(String url) {
        // CAUSE: Assignment to method parameter
        String lUrl = url;
        if (!Strings.isNullOrEmpty(lUrl)) {
            lUrl = lUrl.trim();
			
            if (lUrl.charAt(lUrl.length() - 1) == '/') {
                lUrl = lUrl.substring(0, lUrl.length() - 1);
			}
		}
		
        return lUrl;
    }

    // CAUSE: Utility class contains only static elements and is still instantiable
    private Strings() {
	}
}
