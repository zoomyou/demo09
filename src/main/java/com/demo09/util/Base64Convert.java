package com.demo09.util;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64Convert {

    /**
     * 将png文件转换为base64编码
     * @param path
     * @return
     */
    public static String toBase64(String path){
        String imgFile = path;
        InputStream inputStream = null;
        byte[] data = null;

        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public static void main(String []args){
        String a = toBase64("C:/Users/10842/Desktop/demo09/src/main/resources/static/img/a.png");
        System.out.println(a);
    }
}
