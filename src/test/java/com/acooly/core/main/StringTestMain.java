package com.acooly.core.main;

public class StringTestMain {

	public static void main(String[] args) {
		String str = "abcasdfasdfasdfasdfas\"\"dffsd123";
		System.out.println(replace(str,"\"\"","\""));
	}

	public static String removeEnd(String str, String remove) {
		if(!str.endsWith(remove)) return str;
		return str.substring(0, str.indexOf(remove));
	}

	public static String removeStart(String str, String remove) {
		if(!str.startsWith(remove)) return str;
		return str.substring(remove.length(), str.length());
	}
	
	
	public static String replace(String str,String find, String replace){
		return str.replace(find, replace);
	}

}
