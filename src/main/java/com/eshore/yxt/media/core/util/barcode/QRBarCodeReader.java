package com.eshore.yxt.media.core.util.barcode;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
/***
 *** @version 1.0
 *** @inheritDoc
 *** 实现功能：QR格式二维码读取类
 *** @author jsingfly 2010-03-30
 ***/
public class QRBarCodeReader {


	// QR二维码实例对象
	private static QRBarCodeReader qrBarCodeReader = null;

	public QRBarCodeReader() {

	}

	public synchronized static QRBarCodeReader getInstance() {
		if (qrBarCodeReader == null) {
			qrBarCodeReader = new QRBarCodeReader();
		}
		return qrBarCodeReader;
	}

	// 获取二维码信息
	public String readQRBarCode(QRBarCodeReaderParam param) {

		QRCodeDecoder decoder = new QRCodeDecoder();
		String decodedData = "";
		File imageFile = new File(param.getFilePath());
		BufferedImage image = null;

		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			decodedData = new String(decoder
					.decode(new J2SEImage(image)), param.getCharacter());

		} catch (DecodingFailedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return decodedData;
	} 

	
	  public static void main(String args[]){
	    	QRBarCodeReader.getInstance();
	    }

}


class J2SEImage implements QRCodeImage {   
    BufferedImage image;   
       
    public J2SEImage(BufferedImage image) {   
        this.image = image;   
    }   
       
    public int getWidth() {   
        return image.getWidth();   
    }   
       
    public int getHeight() {   
        return image.getHeight();   
    }   
       
    public int getPixel(int x, int y) {   
        return image.getRGB(x, y);   
    }   
       
}  


