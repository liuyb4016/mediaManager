package com.eshore.yxt.media.core.util.barcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.swetake.util.Qrcode;

/***
 *** @version 1.0
 *** @inheritDoc
 *** 实现功能：QR格式二维码生成类
 *** @author jsingfly 2010-03-30
 ***/
public class QRBarCodeWriter {

	
	//QR二维码实例对象
	private static QRBarCodeWriter qrBarCodeWriter = null;
	private Logger logger = Logger.getLogger(QRBarCodeWriter.class);
	
	private QRBarCodeWriter(){
		
	}
	
	//获取QR二维码实例对象
	public static synchronized QRBarCodeWriter getInstance(){
		if(qrBarCodeWriter == null){
			qrBarCodeWriter = new QRBarCodeWriter();
		}
		return qrBarCodeWriter;
	}
	
	//生成二维码图片
	public void createQRBarCode(QRBarCodeWriterParam param) {

		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeErrorCorrect('M');
		qrcode.setQrcodeEncodeMode('B');
		// qrcode.setQrcodeVersion(7);
		// 二维码信息
		byte[] msgBytes = null;

		try {
			msgBytes = param.getMsg().getBytes(param.getCharacter());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		boolean[][] matrix = qrcode.calQrcode(msgBytes);

		BufferedImage bi = new BufferedImage(matrix.length * 3 + 5,
				matrix.length * 3 + 5, BufferedImage.TYPE_INT_RGB);// TYPE_INT_RGB//TYPE_BYTE_BINARY

		// 获取绘图句柄
		Graphics2D g = bi.createGraphics();

		// 设置背景色
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, matrix.length * 3 + 5, matrix.length * 3 + 5);
		g.setColor(Color.BLACK);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[j][i]) {
					g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3);
				}
			}
		}
		
		g.dispose();
		bi.flush();

		File f = new File(param.getFilePath());
		try {
			ImageIO.write(bi, param.getPicType(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
			
	/**
	 * 
	 * 
	 * 作用：生成金逸二维码图片
	 * 
	 * <p>
	 * <i>版本：</i>2013-8-12<br>
	 * <i>作者：</i>肖习海<br>
	 * </p> 
	 * 
	 * @param   String imgInputStream
	 * @return	
	 * @throws IOException 
	 */
	public void createQRBarCodeForJinYi(QRBarCodeWriterParam param) throws IOException{
		File file = new File(param.getFilePath());
        FileOutputStream outputFileStream = null; 
		//直接生成金逸的二维码图片
		try {  
            outputFileStream = new FileOutputStream(file);  
        	BASE64Decoder decoder = new BASE64Decoder();
        	
            byte[] types = decoder.decodeBuffer(param.getMsg()); 
            logger.info("types.length:"+types.length);
            outputFileStream.write(types);  
            logger.info("二进制流>>>");
            outputFileStream.write("\n".getBytes());  
        }catch (Exception ex){
        	logger.error("Base64解密金逸SP的二维码图片地址出现异常>>>异常信息:"+ex.getMessage(),ex);
        }finally{
        	outputFileStream.close();
        }
	}

}
