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
    
    
    public static void main(String[] args) {
    	String sss = "dsaa<ignore_js_op><div class='mbn'><img class='zoom' onclick='zoom(this, this.src, 0, 0, 0)' width='640' inpost='1' onmouseover='showMenu({'ctrlid':this.id,'pos':'12'})' /></div> <div class='tip tip_4 aimg_tip' id='aimg_157835_menu' style='position: absolute; display: none' disautofocus='true'> <div class='xs0'><p><strong>DSC00367.JPG</strong> <em class='xg1'>(88.87 KB, 下载次数: 2)</em></p><p><a href='http://bbs.xiaot.com/forum.php?mod=attachment&amp;aid=MTU3ODM1fDFiZDBjMjU2fDEzOTAyODM1Mzh8MHwzMTAwNzA%3D&amp;nothumb=yes' target='_blank'>下载附件</a> &nbsp;<a href='javascript:;' onclick='showWindow(this.id, this.getAttribute('url'), 'get', 0);' url='home.php?mod=spacecp&amp;ac=album&amp;op=saveforumphoto&amp;aid=157835&amp;handlekey=savephoto_157835'>保存到相册</a> </p> <p class='xg1 y'>12-8-11 19:55 上传</p></div><div class='tip_horn'></div> </div></ignore_js_op>";
		String str = "刘一江得到<ignore_js_op><div>ss</div></ignore_js_op>"; //<div>ss</div>dsadads <div>ss</div>dsadads
		String re = "<ignore_js_op>(.*)</ignore_js_op>";
//		if(str.indexOf("<ignore_js_op>") != -1){
			System.out.println(sss.replaceAll(re, ""));
//		}
//			System.out.println(sss.matches(re));
			System.out.println(1);
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
