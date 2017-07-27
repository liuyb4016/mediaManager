package com.eshore.yxt.media.core.util;

import java.util.Random;

public class Util {
	
	/**
	 * 
	 * @Description: 生成6位随机数
	 * @return
	 * @throws 
	 * @author lbn
	 * @date 2016年3月22日
	 */
	public static int random6(){
		Random r = new Random();
		int code = 000000;
		while (true) {
			int temp = r.nextInt(999999);
			if (temp > 100000) {
				code = temp;
				break;
			}
		}
		return code;
	}
}
