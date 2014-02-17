package com.mxk.crawler;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 实现文件本地保存 ftp上传 文件压缩
 * @author liuyijiang
 * 每隔一个天新建一个文件夹 保存下载的图片 
 */
//TODO 换成分布式文件系统
@Service
public class BaseFileUploadService {
    
    public static final Logger logger = LoggerFactory.getLogger(BaseFileUploadService.class);
	
    /** 文件保存的根路径 */
	@Value("${file.path}")
	private String rootpath;
	
	/** 压缩文件最大值 */
	@Value("${file.max.size}")
	private double max;
	
	/**
	 * 保存文件 到一个文件夹
	 * @param byteFile
	 * @param fileName
	 * @param foldler
	 * @return
	 */
     public String saveFile(byte[] byteFile, String fileName , String foldler){
    	String filepath = null;
    	if(byteFile == null){
 			return filepath;
 		}
    	filepath = rootpath + File.separator + foldler + File.separator + fileName;
    	logger.debug("开始保存文件{}",fileName);
    	File file = new File(filepath);
		try{
			FileUtils.writeByteArrayToFile(file, byteFile);
			weakZipImage(file,max);
		} catch (Exception e) {
			filepath = null;
			logger.error("保存文件失败{} 文件名路径：{}",e, filepath);
        }
		return filepath;	
     }	
     
     /**
      * 将上传文件保存到文件夹
      * @param inputStream
      * @param fileName
      * @param foldler
      * @return
      */
     public String saveFile(InputStream inputStream, String fileName , String foldler){
    	String filepath = null;
     	filepath = rootpath + File.separator + foldler + File.separator + fileName;
     	logger.debug("开始保存文件{}",fileName);
     	BufferedOutputStream out = null;
		try{
			File uploadFile = new File(filepath);
			byte[] bit = new byte[1024] ;
			int len = -1;
			out = new BufferedOutputStream(new FileOutputStream(uploadFile));
			while((len = inputStream.read(bit)) != -1){
				out.write(bit,0,len);
			}
			out.flush();
		} catch (Exception e) {
			filepath = null;
			logger.error("保存文件失败{} 文件名路径：{}",e, filepath);
		} finally {
			try {
				out.close();
				inputStream.close();
			} catch (IOException e) {
				filepath = null;
				logger.error("保存文件失败{} 文件名路径：{}",e, filepath);
			}
		}
 		return filepath; 
     }
	
     /**
      * 生成文件路径
      * @return
      */
//     private String createFilePath(String fileName){
//    	return File.separator + StringUtil.dateToString(new Date(), "yyyyMMdd") + File.separator + fileName;
//     }
     
     /**
 	 * 根据比例来压缩byte
 	 * @param file
 	 * @param max
 	 */
 	private void weakZipImage(File file,double max){
 		logger.debug("压缩文件");
 		logger.info("压缩前文件大小：{}",file.length() /1024 + "KB");
 		try{
 			BufferedImage image = ImageIO.read(file);
 		    int newWidth = 0;
 		    int newHeight = 0;
 		    double ratio = 0.0; 
 		    if(image.getHeight() > max || image.getWidth() > max){
 	    	   if (image.getHeight() > image.getWidth()) {
 	                ratio = max / image.getHeight();
 	              } else {
 	                ratio = max / image.getWidth();
 	            }
 	    	    newWidth = (int) (image.getWidth() * ratio);
 	            newHeight = (int) (image.getHeight() * ratio);
 		    }else{
 		    	newWidth = image.getWidth();
 	            newHeight = image.getHeight();
 		    }
 		    BufferedImage bfImage = new BufferedImage(newWidth, newHeight,
                     BufferedImage.TYPE_INT_RGB);
             bfImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight,
                             Image.SCALE_SMOOTH), 0, 0, null);
             FileOutputStream os = new FileOutputStream(file);
             JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
             encoder.encode(bfImage);
             os.close();
             logger.info("压缩后文件大小：{}",file.length() /1024 + "KB");
 		} catch (Exception e) {
 			 logger.error("压缩文件失败",e);
         }
 	}
     
}
