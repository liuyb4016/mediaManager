package com.eshore.yxt.media.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.eshore.yxt.media.core.util.barcode.QRBarCodeWriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import cn.eshore.common.util.MobileGlobals;
import cn.eshore.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FtpUtil {
    public static final Logger log = LoggerFactory.getLogger(FtpUtil.class);
	private static String ftpServer1=Util.nullToStr(MobileGlobals.getProperty("yxt.ftp.upload.sever1"));
	private static String ftpServer2=Util.nullToStr(MobileGlobals.getProperty("yxt.ftp.upload.sever2"));
	private static int ftpPort=Integer.parseInt(MobileGlobals.getProperty("yxt.ftp.upload.port"));
	private static String ftpUser=MobileGlobals.getProperty("yxt.ftp.user");
	private static String ftpPassword=MobileGlobals.getProperty("yxt.ftp.password");
	private static String mode=MobileGlobals.getProperty("yxt.ftp.mode");
	private static String ftpServers=ftpServer1+"$"+ftpServer2;
	@Deprecated
	public static boolean ftpUploadFile(InputStream input,String savePath,String fileName){
		return true;//uploadFile(ftpServer, ftpPort, ftpUser, ftpPassword, savePath,fileName,input, mode);
	}
	/**上传文件调用方法*/
	public static boolean ftpUploadFile(InputStream input,String savePath,String oldPth,String fileName){
		log.info("ftpServer------------------"+ftpServers);
		boolean flag=true;
		if(null!=ftpServers&&!"".equals(ftpServers)){
			String ftpServer[]=ftpServers.split("\\$");
			if(ftpServers == null || ftpServers.length() <= 0) {
				return false;
			}
			for(int i=0;i<ftpServer.length;i++){
				if(ftpServer[i]!=null&&!"".equals(ftpServer[i])){
					//有一个失败则判断为失败
					flag=flag && uploadFileNew(ftpServer[i], ftpPort, ftpUser, ftpPassword, savePath,oldPth, fileName, input, mode);
				}
			}
		}else{
			flag = false;
		}
		return flag;
	}
	/**删除ftp文件*/
	public static boolean deleteFtpFile(String savePath,String oldPth){
		log.info("ftpServer------------------"+ftpServers);
		boolean flag=false;
		if(null!=ftpServers&&!"".equals(ftpServers)){
			String ftpServer[]=ftpServers.split("\\$");
			for(int i=0;i<ftpServer.length;i++){
				if(ftpServer[i]!=null&&!"".equals(ftpServer[i])){
					flag=deleteFile(ftpServer[i],ftpPort,ftpUser,ftpPassword,savePath,oldPth,mode);
				}
			}
		}
		return flag;
	}
	public static InputStream  downLoadFile(String path,String fileName){
		return downLoadFile(ftpServer1,ftpPort,ftpUser,ftpPassword,path,fileName,mode);
	}
	
	public static boolean uploadFile(String url,int port,String username, String password, String path,String filename, InputStream input, String mode) {		
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			boolean l = ftp.login(username, password);// 登录
			if (l) {
				System.out.println("login success(成功登陆FTP)!");
				log.info("login success(成功登陆FTP)!");
			} else {
				System.out.println("login success(登陆FTP不成功)!");
				log.info("login success(登陆FTP不成功)!");
				return success;
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.info("连接服务器失败!");
				return success;
			}
			ftp.setBufferSize(1024 * 1024);
			if (mode == null || mode.equals(""))
				mode = "0";
			if (mode.equals("0")) {
				// 传输模式使用passive被动模式
				ftp.enterLocalPassiveMode();
			} else {
				// 传输模式使用active主动模式
				ftp.enterLocalActiveMode();
			}
			// FTP文件传输方式为：二进制
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			if (!ftp.makeDirectory(path)) {
				ftp.mkd(path);
			}
			ftp.changeWorkingDirectory(path);
			boolean b = ftp.storeFile(filename, input);
			input.close();
			ftp.logout();
			if (!b) {
				System.out.println("上传文件不成功!" + b);
				log.info("上传文件不成功!" + b);
				return success;
			} else {
				System.out.println("上传文件成功-_-!");
				log.info("上传文件成功-_-!");
				success = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}

	/** 修改上传文件调用方法 */
	public static boolean uploadFileNew(String url,int port,String username, String password, String path,String oldPath,String filename, InputStream input, String mode) {		
		boolean success = false;
		String filename1 = null;
		FTPClient ftp = new FTPClient();
		if(null!=path&&!"".equals(path)){
			path=path.replaceAll("/+", "/");
		}
		if(null!=oldPath&&!"".equals(oldPath)){
			oldPath=oldPath.replaceAll("/+", "/");
		}
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			boolean l = ftp.login(username, password);// 登录
			if (l) {
				System.out.println("login success(成功登陆FTP)!");
				log.info("login success(成功登陆FTP)!");
			} else {
				System.out.println("login success(登陆FTP不成功)!");
				log.info("login success(登陆FTP不成功)!");
				return success;
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.info("连接服务器失败!");
				return success;
			}
			ftp.setBufferSize(1024 * 1024);
			if (mode == null || mode.equals(""))
				mode = "0";
			if (mode.equals("0")) {
				// 传输模式使用passive被动模式
				ftp.enterLocalPassiveMode();
			} else {
				// 传输模式使用active主动模式
				ftp.enterLocalActiveMode();
			}
			// FTP文件传输方式为：二进制
			if (oldPath != null && !"".equals(oldPath)) {
				filename1 = FilenameUtils.getName(oldPath);
				log.info("old filmname--------" + filename1);
				ftp.deleteFile(path + "/" + filename1);
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			createDirecroty(path, ftp);
			boolean b = ftp.storeFile(filename, input);
			ftp.logout();
			if (!b) {
				System.out.println("上传文件不成功!" + b);
				log.info("上传文件不成功!" + b);
				return success;
			} else {
				System.out.println("上传文件成功-_-!");
				log.info("上传文件成功-_-!");
				success = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}
	private static boolean  deleteFile(String url,int port,String username, String password,String path,String oldPath,String mode){
				boolean success = false;
		String filename1;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			boolean l = ftp.login(username, password);// 登录
			if (l) {
				System.out.println("login success(成功登陆FTP)!");
				log.info("login success(成功登陆FTP)!");
			} else {
				System.out.println("login success(登陆FTP不成功)!");
				log.info("login success(登陆FTP不成功)!");
				return success;
			}
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.info("连接服务器失败!");
				return success;
			}
			if (oldPath != null && !"".equals(oldPath)) {
				try {
					File file = new File(oldPath);
					filename1 = file.getName();
				} catch (RuntimeException e) {
					filename1 = oldPath.substring(oldPath.lastIndexOf("/") + 1);
				}
				log.info("filmname--------" + filename1);
				ftp.deleteFile(path + "/" + filename1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}
			
	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote *
	 *            远程服务器文件绝对路径
	 * @param ftpClient
	 *            FTPClient对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public static boolean createDirecroty(String remote, FTPClient ftpClient)
			throws IOException {
		// UploadStatus status = UploadStatus.Create_Directory_Success;
		boolean status = true;
		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		if (!directory.equalsIgnoreCase("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes("GBK"), "iso-8859-1"))) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes("GBK"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录失败");
						log.info("创建目录失败");
						status = false;
						break;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}   
		    
	private static InputStream  downLoadFile(String url,int port,String username, String password,String path,String fileName,String mode){
	//	 path ="deploy/images//activityData/";
	// fileName ="113220193019.java";
		System.out.println("downLoadFile==path="+path);
		InputStream inputStream =null;
		FTPClient ftp = new FTPClient();
		try {
		   int reply;
		   ftp.connect(url, port);// 连接FTP服务器//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
		   boolean l = ftp.login(username, password);// 登录
		   if(l){
			   System.out.println("login success(成功登陆FTP)!");  
			   log.info("login success(成功登陆FTP)!");
		   }else{
			   System.out.println("login success(登陆FTP不成功)!");
			   log.info("login success(登陆FTP不成功)!");
			   return null;
		   }	   
		   reply = ftp.getReplyCode();
		   if (!FTPReply.isPositiveCompletion(reply)) {
		      ftp.disconnect();
		      log.info("连接服务器失败!");
		      return null;
		   }
		   ftp.setBufferSize(1024*1024);
		   if(mode==null || mode.equals("")) mode="0";
		   if(mode.equals("0")){
			 //传输模式使用passive被动模式
			   ftp.enterLocalPassiveMode();
		   }else{
			 //传输模式使用active主动模式
			   ftp.enterLocalActiveMode();
		   }
	        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	        ftp.changeWorkingDirectory(path);
//			   if(ftp.makeDirectory(path)){
//				
//				   System.out.println(" ftp.changeWorkingDirectory(path); ");
//				   
//			   }
		   FTPFile[] fs = ftp.listFiles();   
		         
	         for(int i = 0; i < fs.length; i++){ 
	         FTPFile ff = fs[i];
	         System.out.println("ff  getName"+ff.getName());	         }
	        inputStream = ftp.retrieveFileStream(fileName); 
	   System.out.println("inputStream ="+inputStream);
		   
		   ftp.logout();
		} catch (IOException e) {
		   e.printStackTrace();
		} finally {
		   if (ftp.isConnected()) {
		      try {
		         ftp.disconnect();
		      } catch (IOException ioe) {
		    	  ioe.printStackTrace();
		      }
		   }
		}
		return inputStream;
	}
	
	
	public static void main(String []args){
		downLoadFile("","");
	}
}
