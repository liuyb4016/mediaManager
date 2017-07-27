package com.eshore.yxt.media.core.util.barcode;

public class QRBarCodeReaderParam {

	
	//二维码生成路径[带文件名]
	private String filePath = "";
	//编码格式
	private String character = "UTF-8";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}
	
	


}
