package com.mxk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

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
    	String regxpForJS = "<script ([^>]*)>([^>]*)</script>"; //[\s\S]*
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
    
//    public static void main(String[] args) {
//    	String str = "量产高手！<img />感觉黑过头了～～做的真心不错了，MS21确实不容易做，其实可以适当夸张点拉大色彩对比<br />已經試著將海軍藍再調淡些,然MS21真的很難抓住精隨~~~";
//    	System.out.println(StringUtil.regxpForHtml(str));
//    }
}
