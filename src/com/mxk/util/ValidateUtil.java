package com.mxk.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 验证
 * @author liuyijiang
 *
 */
public class ValidateUtil {
  
	 public static boolean isMobileNo(String mobiles) {
	        if (null == mobiles || mobiles.trim().length() == 0)
	            return false;
	        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	        Matcher m = p.matcher(mobiles);
	        return m.matches();
	    }

	    public static boolean isEmail(String email) {
	        if (null == email || email.trim().length() == 0)
	            return false;
	        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	        Pattern p = Pattern.compile(str);
	        Matcher m = p.matcher(email);
	        return m.matches();
	    }

	    public static boolean isPhoneNo(String phoneNo) {
	        if (null == phoneNo || phoneNo.trim().length() == 0)
	            return false;
	        Pattern p = Pattern.compile("((^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$)|(^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$))");
	        Matcher m = p.matcher(phoneNo);
	        return m.matches();
	    }

	    public static boolean isFaxNo(String faxNo) {
	        if (null == faxNo || faxNo.trim().length() == 0)
	            return false;
	        return isPhoneNo(faxNo);
	        // Pattern.compile("^(00\\d{8,16})(($)|(,\\d{1,8}$))$");//国际传真
	        // TODO:Add International support,now onlu support china.
	    }

	    public static boolean isZipCode(String zipCode) {
	        if (null == zipCode || zipCode.trim().length() == 0)
	            return false;
	        Pattern p = Pattern.compile("[0-9]{6}");
	        Matcher m = p.matcher(zipCode);
	        return m.matches();
	    }

	    public static boolean isNumber(String number) {
	        if (null == number || number.trim().length() == 0)
	            return false;
	        Pattern p = Pattern.compile("[0-9]+(.[0-9]{2})?|\\d{1,10}");
	        Matcher m = p.matcher(number);
	        if (m.matches()) {
	            String prefix = number.split("\\.")[0];
	            return prefix.length() <= 10;
	        }
	        return false;
	    }

	    public static boolean isInt(String number) {
	        if (null == number || number.trim().length() == 0)
	            return false;
	        Pattern p = Pattern.compile("^[1-9]\\d*$");
	        Matcher m = p.matcher(number);
	        if (m.matches()) {
	            double d = Double.valueOf(number);
	            return d <= 2147483647d;
	        }
	        return false;
	    }

	    public static boolean isDate(String number) {
	        if (null == number || number.trim().length() == 0)
	            return false;
	        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(number);
	        if (m.matches()) {
	            return true;
	        }
	        return false;
	    }
	
	    
	    public static boolean isImage(String flieName){
	    	String type = flieName.substring(flieName.lastIndexOf("."),flieName.length());
	    	if( type.contains("jpg")||type.contains("png")||type.contains("gif")){
	    		return true;
	    	}else{
	    		return false;
	    	}
	    }
	    
}
