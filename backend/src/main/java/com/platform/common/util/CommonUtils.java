package com.platform.common.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

import java.util.UUID;

public class CommonUtils {

    public static String generateId() {
        return IdUtil.fastSimpleUUID();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateCode(int length) {
        return RandomUtil.randomNumbers(length);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String toJson(Object obj) {
        if (obj == null) return null;
        return JSONUtil.toJsonStr(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (isEmpty(json)) return null;
        return JSONUtil.toBean(json, clazz);
    }

    public static String maskPassword(String password) {
        if (isEmpty(password)) return "";
        if (password.length() <= 4) return "****";
        return password.substring(0, 2) + "****" + password.substring(password.length() - 2);
    }

    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 7) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String name = parts[0];
        if (name.length() <= 2) {
            return name.charAt(0) + "***@" + parts[1];
        }
        return name.substring(0, 2) + "***@" + parts[1];
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return phone.matches("^1[3-9]\\d{9}$");
    }

    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) return false;
        return password.length() >= 6 && password.length() <= 20;
    }
}
