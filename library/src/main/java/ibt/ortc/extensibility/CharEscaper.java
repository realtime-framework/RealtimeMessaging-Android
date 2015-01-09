package ibt.ortc.extensibility;

import org.json.simple.JSONValue;

public class CharEscaper {
	public static String removeEsc(String str){
		Object o1 = JSONValue.parse("\""+str+"\"");
		Object o2 = JSONValue.parse("\""+o1.toString()+"\"");
		return o2.toString();				
	}
	/*
	public static String addEsc(String str){
		String pass1 = JSONValue.toJSONString(str);
		pass1 = pass1.substring(1, pass1.length()-1);
		String pass2 = JSONValue.toJSONString(pass1);
		pass2 = pass2.substring(1, pass2.length()-1);
		return pass2;
	}*/
}
