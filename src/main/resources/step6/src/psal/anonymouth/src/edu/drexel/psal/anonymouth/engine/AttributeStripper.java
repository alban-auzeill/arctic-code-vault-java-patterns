package edu.drexel.psal.anonymouth.engine; // (rank 902) copied from https://github.com/psal/anonymouth/blob/7bd8e24b661f587facf8715a1de345dffde9734e/src/edu/drexel/psal/anonymouth/engine/AttributeStripper.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.drexel.psal.jstylo.generics.Logger;

/**
 * Strips the undesirable 'extra' characters in the attribute's fullName (as taken from Weka's Instances object)
 * @author Andrew W.E. McDonald
 *
 */
public class AttributeStripper {
	
	private final String NAME = "( "+this.getClass().getName()+" ) - ";
	
	private static Pattern bracketPat = Pattern.compile("\\{[^-{1}]+\\}");
	private static Pattern someString = Pattern.compile("\\{.*\\}"); // use this pattern, and if an exception is thrown, 
	
	/**
	 * strips the input string
	 * @param input
	 * @return
	 *  stripped input string
	 */
	public static String strip(String input){
		String output = "";
		Matcher histTest = bracketPat.matcher(input);

		if(histTest.find() == true) {
			Matcher m = someString.matcher(input);
			m.find();
			output = input.substring(m.start()+1,input.indexOf('}'));
		}
		else {
			if (!input.contains("authorName"))
				output = input.substring(input.indexOf("'")+1,input.indexOf("{"));
		}

		return output;
	}

}
