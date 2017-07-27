package com.eshore.yxt.media.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class FileUtils {

	public static final Log log = LogFactory.getLog(FileUtils.class);
	
	/**
	 * 表单提交单文件上传
	 * @param name  上传文件名
	 * @param folder  指定目录
	 * @param request  
	 * @return
	 */
	public static String uploadFile(String name,String folder, HttpServletRequest request){

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		MultipartFile multipartFile = multipartRequest.getFile(name);
		
		String originalFileName =  multipartFile.getOriginalFilename();
		
		if(StringUtils.isEmpty(originalFileName)) {     //上传文件为空
			return null;
		}

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");

		/** 构建文件保存的目录* */
		String logoPathDir = folder + dateformat.format(new Date());

		/** 得到文件保存目录的真实路径**/
		String logoRealPathDir  = request.getSession().getServletContext()
				.getRealPath(folder);

		 /** 根据真实路径创建目录* */
        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists())
            logoSaveFile.mkdirs();
        

		/** 获取文件的后缀* */
		String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

		/** 使用UUID生成文件名称* */
		String logImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
		
		/** 拼成完整的文件保存路径加文件**/
		String fileName = logoRealPathDir + File.separator + logImageName;
		
		File file = new File(fileName);
		
		try {
			multipartFile.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/** 打印出上传到服务器的文件的绝对路径* */
		log.debug("****************" + fileName + "**************");
		
		return folder +"/"+ logImageName;
	}
	
	
	/**
	 * 文件下载
	 * @param path   下载目录
	 * @param postName 显示名称
	 * @param request 
	 * @param resp
	 */
	public static void downLoadFile(String path,String postName,HttpServletRequest request,HttpServletResponse response) throws Exception{
	    response.setContentType("text/html;charset=UTF-8");  
	    request.setCharacterEncoding("UTF-8");	
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null; 
        
        String downLoadPath = request.getSession().getServletContext().getRealPath("/")+ path;  
  
        long fileLength = new File(downLoadPath).length();  
  
        response.setContentType("application/octet-stream");  
        
        response.setHeader("Content-disposition", "attachment; filename="  
                + new String(postName.getBytes("utf-8"), "ISO8859-1"));  
        
        response.setHeader("Content-Length", String.valueOf(fileLength));  
  
        bis = new BufferedInputStream(new FileInputStream(downLoadPath));  
        bos = new BufferedOutputStream(response.getOutputStream());  
        byte[] buff = new byte[2048];  
        int bytesRead;  
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
            bos.write(buff, 0, bytesRead);  
        }  
        bis.close();  
        bos.close();  
        
		log.debug("****************" + postName + "**************");
		log.debug("****************下载完成**************");
	}
	
}
