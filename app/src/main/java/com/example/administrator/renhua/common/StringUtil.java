/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.util.TextUtils;

public final class StringUtil {

    /**
     * MD5加密
     *
     * @param sourceStr 传入密码字符串,获取秘钥
     * @return 返回秘钥
     */
    public static String Md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            System.out.println("result: " + result);//32位的加密
//            System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return result;
    }

    public static String decode64(String input) {
        byte[] data = input.getBytes();
        String output = "";
        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        //数据参照，其中字母顺序与加密的参照表中的顺序必须一样，解码出来的数据才能一致
        String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabdcefghijklmnopqrstuvwxyz0123456789+/=";
        //检测编码合法性必须是 A-Z, a-z, 0-9, +, /, or =
        for (int j = 0; j < data.length; j++) {
            if (keyStr.indexOf(data[j]) < 0) {
                System.out
                        .println("There were invalid base64 characters in the input text.\n"
                                + "Valid base64 characters are A-Z, a-z, 0-9, '+', '/', and '='\n"
                                + "Expect errors in decoding.");
                return "";
            }
        }
        do {
            enc1 = keyStr.indexOf(input.charAt(i++));
            enc2 = keyStr.indexOf(input.charAt(i++));
            enc3 = keyStr.indexOf(input.charAt(i++));
            enc4 = keyStr.indexOf(input.charAt(i++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;

            output = output + (char) chr1;

            if (enc3 != 64) {
                output = output + (char) chr2;
            }
            if (enc4 != 64) {
                output = output + (char) chr3;
            }

            chr1 = chr2 = chr3 = 0;
            enc1 = enc2 = enc3 = enc4 = 0;

        } while (i < input.length());

        return Escape.unescape(output);
    }


//    public static String md5(String s) {
//        if (s == null) return null;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            byte[] bytes = md5.digest(s.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b : bytes) {
//                int val = b & 0xFF;
//                if (val < 16) {
//                    sb.append(0);
//                }
//                sb.append(Integer.toHexString(val));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    // 匹配md5
    public static boolean matchesMd5(String s) {
        return s != null && s.matches("^[a-f0-9A-F]{32}$");
    }

    // 登录密码 6-20位字符
    public static boolean matchesPassword(String s) {
        return s != null && s.matches("^.{6,20}$");
//        return s != null && s.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{5,11}$");
    }

    // 匹配手机号码
    public static boolean matchesPhone(String s) {
        return s != null && s.matches("^1[34578]\\d{9}$");
    }

    public static boolean isMobileNO(String mobiles) {
               String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[678]\\d{8}";
                if (TextUtils.isEmpty(mobiles)) return false;
                 else return mobiles.matches(telRegex);
            }
    // 短信验证码
    public static boolean matchesCode(String s) {
        return s != null && s.matches("^\\w{4}$");
    }

    // 匹配中文
    public static boolean matchesChinese(String s) {
        return s != null && s.matches("^.*[\\u4e00-\\u9fa5]+.*$");
    }

    // 匹配身份证
    public static boolean matchesIdCard(String s) {
        return s != null && s.matches("(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$)|(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)");
//        return s != null && s.matches("(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$/)");
    }

    // 匹配邮箱
    public static boolean matchesEmail(String s) {
        return s != null && s.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    // 匹配邮编
    public static boolean matchesZipcode(String s) {
        return s != null && s.matches("^\\d{6}$");
    }

    // 匹配数字
    public static boolean matchesNumber(String s) {
        return s != null && s.matches("^\\d+$");

    }

    // 匹配固定电话
    public static boolean matchesFixPhone(String s) {
        return s != null && s.matches("^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
    }

}
