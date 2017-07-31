package com.eshore.yxt.media.core.util;


import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("restriction")
public class MD5 {
   
	public static String getMD5(byte[] source) {
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
                                      // 用字节表示就是 16 个字节
            s = new BASE64Encoder().encode(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public static String getMD5(String source, String charset) {
    	String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes(charset));
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
                                      // 用字节表示就是 16 个字节
            s = byteArrayToHexString(tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    
    public static String getMD5(InputStream in) {
        String s = null;

		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[8192];
	        int byteCount;
	        while ((byteCount = in.read(bytes)) > 0) {
	          digester.update(bytes, 0, byteCount);
	        }
	        byte[] digest = digester.digest();
	        return byteArrayToHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return s;
    }
    
    private static String byteArrayToHexString(byte[] bytes) {
    	StringBuilder s = new StringBuilder();
    	for (byte b : bytes) {
    		int h = ((b >> 4) & 0x0f);
    		if (h > 9) 
    			s.append((char)(h-10+'a'));
    		else
    			s.append((char)(h+'0'));
    		
    		h = (b & 0x0f);
    		if (h > 9) 
    			s.append((char)(h-10+'a'));
    		else
    			s.append((char)(h+'0'));
    	}
    	
    	return s.toString();
    }
}