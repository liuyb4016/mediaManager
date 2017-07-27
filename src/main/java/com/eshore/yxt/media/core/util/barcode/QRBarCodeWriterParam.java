package com.eshore.yxt.media.core.util.barcode;


/***
 *** @version 1.0
 *** @inheritDoc
 *** 实现功能：QR格式二维码生成输入参数类
 *** @author jsingfly 2010-03-30
 ***/

public class QRBarCodeWriterParam {

	//宽
	private int width = 116;
	//高
	private int height = 116;
	//图片类型
	private String picType = "bmp"; 
	//二维码生成路径[带文件名]
	private String filePath = "";
	
	//编码格式
	private String character = "UTF-8";
	
	//二维码信息
	private String msg = "";
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
	public int getWidth() {
		return width;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	
	

}
