package com.mxk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static Map<String,String> map = new HashMap<String,String>();
	
	static {
		map.put("&nbsp;", "");
		map.put("&middot;", ".");
		map.put("&copy;", "");
		map.put("&aacute;", "");
		map.put("&uuml;", "");
		map.put("&quot;", "");
		
	}
	
	public static String subString(String str,int lenght){
		return str.length() <= lenght ? str : str.substring(0,lenght) + "...";
	}
	
	public static String toEnpty(String str){
		return str == null ? "" :str;
	}
	
	public static String cutOutUrlFileName(String url){
		if(stringIsEmpty(url)){
			return null;
		}
		return url.substring(url.lastIndexOf("/")+1,url.length());
	}
	
	
	public static boolean stringIsEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String dateToString(Date date){
    	return dateToString(date,"yyyy-MM-dd HH:mm:ss");
	}
	
    public static String dateToString(Date date,String formater){
    	if(formater == null){
    		formater = "yyyy-MM-dd HH:mm:ss";
    	}
    	SimpleDateFormat s  = new SimpleDateFormat(formater);
    	return s.format(date);
	}
    
    public static String urlTrim(String url){
		if(url.indexOf("?") != -1){
			return url.substring(0,url.indexOf("?")).trim();
		}else{
			return url.trim();
		}
	}
	
    public static String getFileSuffixName(String fileName){
    	if(stringIsEmpty(fileName)){
			return null;
		}
		return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
    }
    
    /**
     * 自定义替换值
     * @param html
     * @param replace
     * @return
     */
    public static String regxpForHtml(String html,String replace){
    	String regxpForJS = "<ignore_js_op>([/s/S]*)</ignore_js_op>"; //[\s\S]* <script ([^>]*)>([^>]*)</script> <ignore_js_op>([/s/S]*)</ignore_js_op>
    	String regxpForHtml = "<([^>]*)>";
    	Pattern pattern = Pattern.compile(regxpForJS);
    	Matcher matcher = pattern.matcher(html);
    	StringBuffer sb = new StringBuffer();
    	boolean result1 = matcher.find();
    	while (result1) { //去除js
    	   matcher.appendReplacement(sb, " ");
    	   result1 = matcher.find();
    	}
    	matcher.appendTail(sb);
    	Pattern patternHtml = Pattern.compile(regxpForHtml);
    	Matcher matcherHtml = patternHtml.matcher(sb.toString());
    	StringBuffer sbHtml = new StringBuffer();
    	result1 = matcherHtml.find();
    	while (result1) { //去除html
    	   matcherHtml.appendReplacement(sbHtml, replace);
    	   result1 = matcherHtml.find();
    	}
    	matcherHtml.appendTail(sbHtml);
    	return sbHtml.toString();
    }
    
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断字符串里是否有中文
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
    	System.out.println(formaterRFC2396URI("http://www.militarymodelling.com/sites/1/images/member_albums/48461/rr1 [640x480].jpg"));
//    	Pattern pattern=Pattern.compile("[\\u4e00-\\u9fa5]+");  
//    	Matcher matcher=pattern.matcher(str);  
//    	System.out.println(matcher.matches());  
    	//System.out.println(isChinese(str));
//    	Set<Entry<String,String>> set = map.entrySet();
//    	for(Entry<String,String> entry:set){
//    		str = str.replaceAll(entry.getKey(), entry.getValue());
//    	}
//    	System.out.println(str);
	}
    
    public static String formaterRFC2396URI(String url){
    	return url.replace(" ", "%20").replace("[", "%5B").replace("]", "%5D");
    }
    
    /**
     * 用+号分隔
     * @param html
     * @return
     */
    public static String regxpForHtml(String html){
    	return regxpForHtml(html," + ");
    }
    
    public static String regxpForSymbol(String str){
    	Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
    	return  m.replaceAll("");
    }
    
    public static String replaceTag(String str){
    	Set<Entry<String,String>> set = map.entrySet();
    	for(Entry<String,String> entry:set){
    		str = str.replaceAll(entry.getKey(), entry.getValue());
    	}
    	return str;
    }
    
    
//    public static void main(String[] args) {
//    	String str = "量产高手！<img />感觉黑过头了～～做的真心不错了，MS21确实不容易做，其实可以适当夸张点拉大色彩对比<br />已經試著將海軍藍再調淡些,然MS21真的很難抓住精隨~~~";
//    	System.out.println(StringUtil.regxpForHtml(str));
//    }
}
