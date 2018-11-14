package com.softwaretraining.cddgame.message;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageFilter {

    private final static String IP = "47.106.214.129";
    private final static String EXCEPTION = "exception";
    private final static String REFUSE = "refuse";

    public static String filter(final String message) {
        String result = message;
        if (isContainException(message)) {
            return "服务器异常，请联系管理员";
        }
        if (isContainRefuse(message)) {
            return "服务器拒绝了连接";
        }
        if (isContainIP(result)) {
            result = result.substring(0, result.indexOf(IP) - 4)
                    + result.substring(result.indexOf(IP) + IP.length() + 1, result.length());
        }

        return result;
    }

    public static String getEncode(String message) {
        return getMD5("gamesi2v" + getMD5(message) + "lw4ngame");
    }

    private static boolean isContainIP(String message) {
        return message != null && message.contains(IP);
    }

    private static boolean isContainException(String message) {
        return message != null && message.contains(EXCEPTION);
    }

    private static boolean isContainRefuse(String message) {
        return message != null && message.contains(REFUSE);
    }

    private static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
