package com.leoyang.genghis.server.util;

/**
 * Created by yang.liu on 2018/10/26
 */
public class StringUtil {

    /**
     * 16进制转字符串
     * @param hexStr
     * @return
     */
    public static String hexStrToString(String hexStr) throws Exception {
        String result = "";
        hexStr = hexStr.toUpperCase();
        String hexDigital = "0123456789ABCDEF";
        char[] hexChars = hexStr.toCharArray();
        byte[] hexNewBytes = new byte[hexStr.length() / 2];
        int n;
        for (int i =0 ;i<hexNewBytes.length;i++) {
            n = hexDigital.indexOf(hexChars[2*i])*16 + hexDigital.indexOf(hexChars[2*i +1]);
            hexNewBytes[i] = (byte) (n & 0xff);
        }
        result = new String(hexNewBytes,"UTF-8");
        return result;
    }
}
