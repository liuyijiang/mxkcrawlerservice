package com.mxk.util;
/**
 * 一些有用的正则表达式枚举
 * @author Administrator
 *
 */
public enum RegxpEnumUtil {

	/**英语不区分大小写 */
	ENGLISH_LOWER_UPPER_CASE("^[a-zA-Z]*"),
	
	/**罗马数字小写 */
	ROMAN_NUMERALS_LOWER_CASE("(l?x{0,3}|x[lc])(v?i{0,3}|i[vx])$"),
	
	/**罗马数字大写 */
	ROMAN_NUMERALS_UPPER_CASE("(L?X{0,3}|X[LC])(V?I{0,3}|I[VX])$"),
	
	/**罗马数字不区分大小写 */
	ROMAN_NUMERALS_LOWER_UPPER_CASE("^((l|L)?(x|X){0,3}|(x|X)[lc]|[LC])((v|V)?(i|I){0,3}|(i|I)[vx]|[VX])$"),
	
	/**匹配html */
	HTML_CASE("<([^>]*)>"),
	
	/**匹配js */
	JAVASCRIPT_CASE("<script ([^>]*)>([^>]*)</script>"),
	
	/**汉字*/
	CHINESE("^[\u4e00-\u9fa5],{0,}$"),
	
	/**数字*/
	NUMBER("^[0-9]*$");
	
	private String code;
	
	private RegxpEnumUtil(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
