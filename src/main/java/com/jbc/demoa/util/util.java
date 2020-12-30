package com.jbc.demoa.util;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

//工具类
public class util {
    //检查cmp
    public static boolean checkCmp(String a1,String a2){

        for(int i=0;i<5;++i){
            if(a1.charAt(i)<a2.charAt(i))return false;
            if(a1.charAt(i)>a2.charAt(i))return true;

        }
        return true;
    }

    //判断日期是否冲突
    public  static  boolean dateCheck(String a1,String a2,String b1,String b2){
        if((a1.split(" ")[0]).equals(b1.split(" ")[0])){
            a1=a1.split(" ")[1];
            a2=a2.split(" ")[1];
            b1=b1.split(" ")[1];
            b2=b2.split(" ")[1];
            if(a1.length()!=5)a1="0"+a1;
            if(a2.length()!=5)a2="0"+a2;
            if(((!(checkCmp(a1,b1))&&!(checkCmp(a2,b1))))||a2.equals(b1))return true;
            if(((checkCmp(a1,b2))&&(checkCmp(a2,b2)))||a1.equals(b2))return true;
            return false;
        }
        return true;
    }
}

