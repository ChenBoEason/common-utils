package com.github.x4096.common.utils.text;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.regex.Pattern;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 16:58
 * @Description:
 */
public class ValidateUtils {

    private ValidateUtils() {
    }

    /**
     * 邮箱
     */
    private static final String EMAIL = "^[a-z0-9]([a-z0-9]*[-_\\.\\+]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2,4})?$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL);

    /**
     * 汉字
     */
    private static final String CHINA_TEXT = "[\\u4e00-\\u9fa5]+";
    private static final Pattern CHINA_TEXT_PATTERN = Pattern.compile(CHINA_TEXT);

    /**
     * 中国姓名
     */
    private static final String CHINA_NAME = "[\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}";
    private static final Pattern CHINA_NAME_PATTERN = Pattern.compile(CHINA_NAME);


    /**
     * 手机号 简单校验 1字头＋10位数字即可
     */
    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    private static final Pattern PATTERN_REGEX_MOBILE_SIMPLE = Pattern.compile(REGEX_MOBILE_SIMPLE);


    /**
     * 手机号 电信
     */
    private static final String PHONE_CHINA_TELECOM = "^1[3578][01379]\\d{8}$";
    private static final Pattern PHONE_CHINA_TELECOM_PATTERN = Pattern.compile(PHONE_CHINA_TELECOM);

    /**
     * 手机号 联通
     */
    private static final String PHONE_CHINA_UNION = "^1[34578][01256]\\d{8}$";
    private static final Pattern PHONE_CHINA_UNION_PATTERN = Pattern.compile(PHONE_CHINA_UNION);

    /**
     * 手机号 电信
     */
    private static final String PHONE_CHINA_MOBILE = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";
    private static final Pattern PHONE_CHINA_MOBILE_PATTERN = Pattern.compile(PHONE_CHINA_MOBILE);

    /**
     * 正则：IP地址 目前只支持 IPV4
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);

    /**
     * 网络协议
     */
    private static final String[] SCHEMES = {"http", "https"};

    /**
     * 验证是否为邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }


    /**
     * 验证字符串是否为中文
     *
     * @param chineseText
     * @return
     */
    public static boolean isChineseText(String chineseText) {
        if (StringUtils.isBlank(chineseText)) {
            return false;
        }
        return CHINA_TEXT_PATTERN.matcher(chineseText).matches();
    }


    /**
     * 验证字符串是否为中国人姓名
     * 包括少数民族      噶及·洛克业
     * 外国人翻译为中文  洛克·哈德森
     *
     * @param chineseName
     * @return
     */
    public static boolean isChineseName(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            return false;
        }
        return CHINA_NAME_PATTERN.matcher(chineseName).matches();
    }


    /**
     * 验证字符串是否为标准URL
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }
        UrlValidator urlValidator = new UrlValidator(SCHEMES);
        return urlValidator.isValid(url);
    }


    /**
     * 验证字符串是否为标准URL
     *
     * @param url
     * @return
     */
    public static boolean isNotUrl(String url) {
        return !isUrl(url);
    }

    /**
     * 验证字符串是否为标准JSON字符串
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJSON(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }
        return JSON.isValid(jsonStr);
    }


    /**
     * 验证字符串是否为标准JSON字符串
     *
     * @param jsonStr
     * @return
     */
    public static boolean isNotJSON(String jsonStr) {
        return !isJSON(jsonStr);
    }


    /**
     * 验证中国手机号 （精确）
     *
     * @param phone
     * @return
     * @apiNote 手机号更新比较频繁, 此API无法确保一直可用,若失效请联系开发负责人
     */
    @Deprecated
    public static boolean isMobileExact(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_CHINA_TELECOM_PATTERN.matcher(phone).matches() ||
                PHONE_CHINA_UNION_PATTERN.matcher(phone).matches() ||
                PHONE_CHINA_MOBILE_PATTERN.matcher(phone).matches();
    }


    /**
     * 验证手机号（简单）
     *
     * @param phone
     * @return
     */
    public static boolean isMobileSimple(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PATTERN_REGEX_MOBILE_SIMPLE.matcher(phone).matches();
    }


    /**
     * 验证IP地址 目前只支持 IPV4
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIP(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }
        return PATTERN_REGEX_IP.matcher(ipAddress).matches();
    }

}