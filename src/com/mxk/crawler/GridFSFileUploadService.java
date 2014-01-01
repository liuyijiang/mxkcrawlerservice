package com.mxk.crawler;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.mxk.crawler.model.Links;
import com.mxk.util.StringUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 文件上传到gridfs文件服务器
 * @author Administrator
 *
 */
@Service
public class GridFSFileUploadService {

	private static final String TYPE_PNG = ".png";
	
    public static final Logger logger = LoggerFactory.getLogger(GridFSFileUploadService.class);
    
    private static final String TEMP_PATH = GridFSFileUploadService.class.getResource("/").getPath(); //临时文件路径
    
    @Value("${gridfs.image.colloction}")
	private String imageCollection;
    
	@Autowired
	private MongoOperations mog; 
	
	/**
	 * 
	 * @param file 文件
	 * @param id 
	 * @param type
	 * @param size
	 * @param max
	 * @return
	 */
	public String uploadImageFile(File file, String fileName,double max) {
		logger.info("开始上传文件{}",fileName);
		String successName = null;
		GridFSInputFile gfsInput = null;
		DB db = mog.getCollection(mog.getCollectionName(Links.class)).getDB();
		db.requestStart(); 
		try{
			 weakZipImage(file,max);
			 gfsInput = new GridFS(db, imageCollection).createFile(file);//
			 gfsInput.setFilename(fileName + TYPE_PNG);
			 gfsInput.setContentType("png");
			 gfsInput.save(); //save
			 successName = fileName + TYPE_PNG;
			 logger.info("完成上传文件{}",fileName);
		}catch(Exception e){
			logger.error("上传文件失败",e);
		}
		return successName;
	}
	
	/**
	 * 上传byte[] 文件
	 * @param byteFile
	 * @param fileName
	 * @param type
	 * @return
	 */
	public boolean uploadImageByte(byte[] byteFile, String fileName) {
		boolean success = true;
		if(byteFile == null){
			return false;
		}
		logger.info("开始上传文件{}",fileName);
		GridFSInputFile gfsInput = null;
		DB db = mog.getCollection(mog.getCollectionName(Links.class)).getDB();
		db.requestStart(); 
		try{
			 if((byteFile.length /1024) > 100){ //压缩
				 File file = weakZipByte(byteFile,fileName);
				 if(file.length() == 0){
					 return false;
				 }
			     gfsInput = new GridFS(db, imageCollection).createFile(file);//
			     gfsInput.setFilename(fileName);
				 gfsInput.setContentType(StringUtil.getFileSuffixName(fileName));
				 gfsInput.save(); //save
			     file.delete();//删除临时文件
			 }else{
				 gfsInput = new GridFS(db, imageCollection).createFile(byteFile);//
				 gfsInput.setFilename(fileName);
				 gfsInput.setContentType(StringUtil.getFileSuffixName(fileName));
				 gfsInput.save(); //save
			 }
			 logger.info("完成上传文件{}",fileName);
		}catch(Exception e){
			success = false;
			logger.error("上传文件失败",e);
		}
		return success;
	}
	
	/**
	 * 根据比例来压缩文件
	 * @param file
	 * @param max
	 */
	private File weakZipByte(byte[] byteFile,String fileName){
		logger.info("压缩文件");
		logger.info("压缩前文件大小：{}",byteFile.length /1024 + "KB");
		double max = 710.0;
		String tempfileName = TEMP_PATH+StringUtil.dateToString(new Date(),"yyyy_MM_dd_HH_mm_ss")+fileName;
		File file = new File(tempfileName);
		try{
			FileUtils.writeByteArrayToFile(file, byteFile);
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
			logger.error("上传文件失败",e);
        }
		return file;
	}
	
	/**
	 * 根据比例来压缩byte
	 * @param file
	 * @param max
	 */
	private void weakZipImage(File file,double max){
		logger.info("压缩文件");
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
			logger.error("上传文件失败",e);
        }
	}
	
}
