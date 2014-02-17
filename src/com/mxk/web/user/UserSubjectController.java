package com.mxk.web.user;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.mxk.crawler.BaseFileUploadService;
import com.mxk.system.catalog.CatalogResourcePlus;
import com.mxk.web.base.MessageAndView;
import com.mxk.web.http.ServiceResponse;
import com.mxk.web.security.SecurityDescription;

/**
 * 
 * @author Administrator
 * 
 */
@Controller
public class UserSubjectController {

	public static final Logger logger = LoggerFactory.getLogger(UserSubjectController.class);
	
	@Value("${file.image.subject.default.mini}")
	private String defaultImage;
	
	/** 文件保存的根路径 */
	@Value("${file.part.path}")
	private String path;
	
	@Autowired
	private UserSubjectService userSubjectService;
	
	@Autowired
	private BaseFileUploadService baseFileUploadService;
	
	/**
	 * 用户新建专题
	 * @param tag
	 * @param title
	 * @param category
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/add/subject", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	@SecurityDescription(loginRequest = true)
	public MessageAndView createUserSubject(@RequestParam("tag") String tag,
			@RequestParam("title") String title,
			@RequestParam("category") String category, @CookieValue("id") int id) {
		UserSubjectPlus plus = createSubjectPlus(tag, title, category, id);
		userSubjectService.createUserSubject(plus);
		return MessageAndView.newInstance().put(plus);
	}
	
	private UserSubjectPlus createSubjectPlus(String tag, String title, String category,
			int id) {
		UserSubjectPlus plus = new UserSubjectPlus();
		plus.setCategory(category);
		plus.setCreatetime(new Date());
		plus.setFaceimage(defaultImage);
		plus.setTag(tag);
		plus.setTitle(title);
		plus.setUserid(id);
		return plus;
	}
	
     /**
      * 创建专题part
      * @param request
      * @param response
      * @param plus
      * @param id
      * @throws Exception
      */
	//FIXME
	@RequestMapping(value = "/add/part", method = { RequestMethod.POST, RequestMethod.GET })
	public void createUserSubjectPart(HttpServletRequest request,HttpServletResponse response,UserSubjectPartPlus plus,@CookieValue("id") int id) throws Exception{
		//转型为MultipartHttpRequest(重点的所在)  
        MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;  
        //  获得第1张图片（根据前台的name名称得到上传的文件）   
        MultipartFile imgFile =  multipartRequest.getFile("imgFile"); 
       // imgurl = baseFileUploadService.saveFile(imgFile.getInputStream(), imgFile.getOriginalFilename(), path);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "error");  
        if(userSubjectService.checkSubjectOwner(plus.getSubjectId(),id)){
        	plus.setCreateTime(new Date());
       	    plus.setUserId(id);
       	    if(validateUserSubjectPartPlus(plus)){
       	    	userSubjectService.createUserPart(plus);
       	    	if(plus.getId() != null){
       	    		String filename = plus.getSubjectId()+ "_" + plus.getId() + "_"+ imgFile.getOriginalFilename();
       	    		String imgurl = baseFileUploadService.saveFile(imgFile.getInputStream(), filename , path);
       	    		if(imgurl != null){
       	    			plus.setImgUrl(path+filename);
       	    			userSubjectService.updateUserSubjectPart(plus);
       	    		}
       	    		if(plus.isChange()){
       	    			UserSubjectPlus splus = userSubjectService.findUserSubjectPlusById(plus.getId());
       	    			splus.setFaceimage(path+filename);
       	    			userSubjectService.updateUserSubject(splus);
       	    		}
       	    		jsonObject.put("message", "success+url"+path+filename);  
       	    	}
       	    }
        }else{
        	 jsonObject.put("message", "用户信息不匹配");  
        }
        response.setContentType("text/html");  
        response.getWriter().println(jsonObject.toString()); 
	}
	
	private boolean validateUserSubjectPartPlus(UserSubjectPartPlus plus){
		if(plus.getSubjectId() == null){
			return false;
		} 
		if(plus.getUserId() == null){
			return false;
		}
		return true;
	}
	
    
//	private UserSubjectPlus createUserSubjectPlus(){
//		
//	}
	
//	private boolean validateUserSubjectPlus(UserSubjectPlus plus){
//		
//	}
	
}
