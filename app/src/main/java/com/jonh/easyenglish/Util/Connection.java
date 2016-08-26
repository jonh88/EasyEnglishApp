package com.jonh.easyenglish.Util;

/**
 * Created by Jonh on 29/11/2015.
 */
public class Connection {
    private static String endPoint = "http://52.38.209.62";


    public static String getHost(){
        return endPoint+":8080/easyEnglish/rest/";
    }

    public static void setEndPoint(String url){
        endPoint = url;
    }

    public static String getEndPoint(){
        return endPoint;
    }


}
