package com.demo09.util;

public class CaptchaClassify {

    public static String classify(String fileName){
        String classInfo = "";
        String temp = fileName.substring(0, 4);
        switch (temp){
            case "1001":
                classInfo = "4字符纯数字";
                break;
            case "1002":
                classInfo = "4字符纯数字+字母";
                break;
            case "1003":
                classInfo = "4字符纯数字+背景";
                break;
            case "1004":
                classInfo = "4字符纯数字+字母+背景";
                break;
            case "1005":
                classInfo = "三维4字符";
                break;
            case "1006":
                classInfo = "4字符动画";
                break;
            case "1101":
                classInfo = "基于关键词点选图片";
                break;
            case "1102":
                classInfo = "基于关键词点选字符";
                break;
            case "1201":
                classInfo = "拖拽至空缺位置";
                break;
            case "1202":
                classInfo = "拖拽旋转图像至正确方位";
                break;
            case "1203":
                classInfo = "拖拽以生成正确轨迹";
                break;
            case "1301":
                classInfo = "recaptcha 3*3";
                break;
            case "1302":
                classInfo = "recaptcha 4*4";
                break;
        }
        return classInfo;
    }
}
