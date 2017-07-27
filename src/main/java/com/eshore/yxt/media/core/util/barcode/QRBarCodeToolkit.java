package com.eshore.yxt.media.core.util.barcode;

import java.io.File;
import java.io.FileInputStream;


/***
 *** @version 1.0
 *** @inheritDoc
 *** 实现功能：QR格式二维码工具类
 *** @author jsingfly 2010-03-30
 ***/
public class QRBarCodeToolkit {

	
	//获取二维码读取对象
	public static QRBarCodeReader getQRBarCodeReader(){
		return QRBarCodeReader.getInstance();
	}

	//获取二维码生成对象
	public static QRBarCodeWriter getQRBarCodeWriter(){
		return QRBarCodeWriter.getInstance();
	}
	
	public static void main(String args[])throws Exception{
		//编码
		QRBarCodeWriter barCodeWriter = QRBarCodeToolkit.getQRBarCodeWriter();
		QRBarCodeWriterParam writerParam = new QRBarCodeWriterParam();
		//writerParam.setFilePath("c:/jpg_2.png");
		writerParam.setFilePath("D:/upload/影院取票号测试.png");
		writerParam.setMsg("71474223");
		long begin = System.currentTimeMillis();
		barCodeWriter.createQRBarCode(writerParam);
		long end = System.currentTimeMillis();
		System.out.println("编码测试时间:" + (end - begin));
		
		//解码
		QRBarCodeReader barCodeRead = QRBarCodeToolkit.getQRBarCodeReader();
		QRBarCodeReaderParam readParam = new QRBarCodeReaderParam();
		readParam.setFilePath("c:/影院取票号测试.png");
		begin = System.currentTimeMillis();
		System.out.println(barCodeRead.readQRBarCode(readParam));
		end = System.currentTimeMillis();
		System.out.println("解码测试时间:" + (end - begin));
		
		File barcode = new File("c:/wma_1.png");
		FileInputStream in = new FileInputStream(barcode);
		byte barcodeFileBytes[] = new byte[in.available()];
		in.read(barcodeFileBytes);
//		System.out.println(Base64.encode(barcodeFileBytes));
//		System.out.println(Base64.encode(barcodeFileBytes).length());
	}

}
