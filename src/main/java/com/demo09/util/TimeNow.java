package com.demo09.util;

import java.sql.Date;
import java.sql.Timestamp;

public class TimeNow {

    public static void main(String []args){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp1);
        System.out.println(timestamp.getTime());
        System.out.println(timestamp1.getTime());
        System.out.println("id为"+12+"的用户得分为:");
        System.out.println(56.78891234);
    }
}
