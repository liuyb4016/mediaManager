package com.eshore.yxt.media.core.util.media;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
public class FileDigest {
    /**
     * 获取单个文件的MD5值！
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()){
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in=null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取文件夹中文件的MD5值
     * @param file
     * @param listChild ;true递归子目录中的文件
     * @return
     */
    public static Map<String, String> getDirMD5(File file,boolean listChild) {
        if(!file.isDirectory()){
            return null;
        }
        //<filepath,md5>
        Map<String, String> map=new HashMap<String, String>();
        String md5;
        File files[]=file.listFiles();
        for(int i=0;i<files.length;i++){
            File f=files[i];
            if(f.isDirectory()&&listChild){
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5=getFileMD5(f);
                if(md5!=null){
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }


    public static void main(String[] args) {
        File file1 = new File("a.txt");
        File file2 = new File("b.txt");
        System.out.println(getFileMD5(file1).equals(getFileMD5(file2)));
    }
}