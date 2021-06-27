package io.yoshizaki4439.robatest.utils;

public final class Constants {
	private Constants() {
	}

	// HTTP Method
	public static final String HTTP_METHOD_GET = "get";
	public static final String HTTP_METHOD_POST = "post";

	// Parameter Category
	public static final String PARAMETER_CATEGORY_BODY = "body";
	public static final String PARAMETER_CATEGORY_HEADER = "header";
	public static final String PARAMETER_CATEGORY_QUERY = "query";
	public static final String PARAMETER_CATEGORY_URI = "uri";

	// Parameter Type
	public static final String PARAMETER_TYEP_ARRAY = "array";
	public static final String PARAMETER_TYEP_OBJECT = "object";
	public static final String PARAMETER_TYEP_STRING = "string";

	// Parameter Name
	public static final String PARAMETER_NAME_STATUS = "status";

	// Property Key
	public static final String PROPERTY_KEY_JOBDETAILNAME = "jobDetailName";
	public static final String PROPERTY_KEY_JOBDETAILGROUP = "jobDetailGroup";
	public static final String PROPERTY_KEY_JOBDESCRIPTION = "jobDescription";
	public static final String PROPERTY_KEY_FLOWNAME = "flowName";

	// Symbol
	public static final String BLANK = "";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String DOT = ".";
	public static final String EQUAL = "=";
	public static final String HYPHEN = "-";
	public static final String MINUS = "-";
	public static final String PLUS = "+";
	public static final String PARENTHESIS_LEFT = "(";
	public static final String PARENTHESIS_RIGHT = ")";
	public static final String SHARP = "#";
	public static final String SINGLE_QUOTE = "'";
	public static final String SLASH = "/";
	public static final String SPACE = " ";
	public static final String UNDERSCORE = "_";

	// DateTime
	public static final String ISO8601_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	// charset
	public static final String UTF_8 = "UTF-8";
	
	// Drools
	public static final String DTDRL_TODAY = "TODAY" ;
	public static final String DTDRL_DATE_FORMAT_HEAD = "Date{";
	public static final String DTDRL_DATE_FORMAT_TAIL = "}";
	public static final String DTDRL_DATE_FORMAT_YYYYMMDD = "yyyy/mm/dd";
	public static final String DTDRL_EIGYOUBI = "営業日";
	public static final String DTDRL_BIZDATE = "BZD" ;
}