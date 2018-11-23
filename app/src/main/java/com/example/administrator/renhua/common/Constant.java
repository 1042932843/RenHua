package com.example.administrator.renhua.common;

/**
 * 常量
 */
public interface Constant {
//    String DOMAIN = "http://192.168.1.183:8088/jeecg-bpm/";//测试
//    String DOMAIN = "http://www.rhggfw.com:9700/rhggfw/";//原9700为正式环境
//    String DOMAIN = "http://www.rhggfw.com:8081/rhggfw/";//测试http://192.168.1.6:8079/rhggfw
//        String DOMAIN = "http://192.168.1.155:8080/jeecg/";
   String DOMAIN = "http://www.rhggfw.com:8081/rhggfw/";//现8080为正式环境http://www.rhggfw.com:8081/rhggfw
    String DOMAINn = "http://www.rhggfw.com:8081/rhggfw";//现8080为正式环境http://192.168.1.6:8079/rhggfw/
//    H5页面
    String DOMAIN2 = DOMAINn+"/webpage/views/app/";
//    String DOMAIN2 = "http://www.rhggfw.com:8081/rhggfw/webpage/views/app/";
//    String DOMAIN2 = "http://192.168.1.155:8080/jeecg/webpage/views/app/";

     String url_randCodeImage="/webpage/views/commons/checkCode.jsp?";

    static final int FILE_SELECT_CODE = 0;
    String WT_DOMAIN = "http://www.rhggfw.com:8081/rhggfw";
    //     String url_randCodeImage="http://www.rhggfw.com:8081/rhggfw/webpage/views/commons/checkCode.jsp?";
//public static String url_randCodeImage="http://192.168.1.155:8080/jeecg/webpage/views/commons/checkCode.jsp?";
//    public static String url_randCodeImage="http://192.168.1.183:8088/jeecg-bpm/webpage/views/commons/checkCode.jsp?";

}


