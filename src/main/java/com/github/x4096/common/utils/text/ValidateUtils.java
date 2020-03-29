package com.github.x4096.common.utils.text;

import com.alibaba.fastjson.JSON;
import com.github.x4096.common.utils.date.enums.DateFormatEnum;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.regex.Pattern;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 16:58
 * @Description: 校验数据工具类
 */
public class ValidateUtils {

    private ValidateUtils() {
    }

    /**
     * 邮箱
     */
    private static final String EMAIL_REGEX = "^[a-z0-9]([a-z0-9]*[-_\\.\\+]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2,4})?$";
    private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 汉字
     */
    private static final String CHINA_TEXT_REGEX = "[\\u4e00-\\u9fa5]+";
    private static final Pattern CHINA_TEXT_REGEX_PATTERN = Pattern.compile(CHINA_TEXT_REGEX);

    /**
     * 中国姓名
     */
    private static final String CHINA_NAME_REGEX = "[\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}";
    private static final Pattern CHINA_NAME_REGEX_PATTERN = Pattern.compile(CHINA_NAME_REGEX);


    /**
     * 手机号 简单校验 1字头＋10位数字即可
     */
    private static final String MOBILE_SIMPLE_REGEX = "^[1]\\d{10}$";
    private static final Pattern MOBILE_SIMPLE_REGEX_PATTERN = Pattern.compile(MOBILE_SIMPLE_REGEX);


    /**
     * 手机号 电信
     */
    private static final String PHONE_CHINA_TELECOM_REGEX = "^1[3578][01379]\\d{8}$";
    private static final Pattern PHONE_CHINA_TELECOM_REGEX_PATTERN = Pattern.compile(PHONE_CHINA_TELECOM_REGEX);

    /**
     * 手机号 联通
     */
    private static final String PHONE_CHINA_UNION_REGEX = "^1[34578][01256]\\d{8}$";
    private static final Pattern PHONE_CHINA_UNION_REGEX_PATTERN = Pattern.compile(PHONE_CHINA_UNION_REGEX);

    /**
     * 手机号 电信
     */
    private static final String PHONE_CHINA_MOBILE_REGEX = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";
    private static final Pattern PHONE_CHINA_MOBILE_REGEX_PATTERN = Pattern.compile(PHONE_CHINA_MOBILE_REGEX);

    /**
     * IP地址 IPV4
     */
    private static final String IPV4_REGEX = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern IPV4_REGEX_PATTERN = Pattern.compile(IPV4_REGEX);

    /**
     * IP地址 IPV6
     */
    private static final String IPV6_REGEX = "([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)\\:([0-9a-f]+)";
    private static final Pattern IPV6_REGEX_PATTERN = Pattern.compile(IPV6_REGEX);

    /**
     * 端口
     */
    private static final int PORT_MIN = 1;
    private static final int PORT_EFFECTIVE = 1024;
    private static final int PORT_MAX = 65535;


    /**
     * 网络协议
     */
    private static final String[] SCHEMES = {"http", "https"};

    /**
     * 时间格式 yyyy-MM-dd
     */
    private static String REGEX_YEAR_MONTH_DAY = "((\\d{2}(([02468][048])|([13579][26]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    private static final Pattern REGEX_YEAR_MONTH_DAY_PATTERN = Pattern.compile(REGEX_YEAR_MONTH_DAY);


    /**
     * 时间格式 yyyy-MM-dd hh:mm:ss
     */
    private static String REGEX_YEAR_MONTH_DAY_H_M_S_12 = REGEX_YEAR_MONTH_DAY + "(\\s(((0[0-9])|([1][0-2])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9]))?)))?";
    private static final Pattern REGEX_YEAR_MONTH_DAY_H_M_S_12_PATTERN = Pattern.compile(REGEX_YEAR_MONTH_DAY_H_M_S_12);


    /**
     * 时间格式 yyyy-MM-dd HH:mm:ss
     */
    private static String REGEX_YEAR_MONTH_DAY_H_M_S_24 = REGEX_YEAR_MONTH_DAY + "(\\s(((0[0-9])|([1-2][0-3])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9]))?)))?";
    private static final Pattern REGEX_YEAR_MONTH_DAY_H_M_S_24_PATTERN = Pattern.compile(REGEX_YEAR_MONTH_DAY_H_M_S_24);


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
        return EMAIL_REGEX_PATTERN.matcher(email).matches();
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
        return CHINA_TEXT_REGEX_PATTERN.matcher(chineseText).matches();
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
        return CHINA_NAME_REGEX_PATTERN.matcher(chineseName).matches();
    }


    /**
     * 验证字符串是否为标准URL 支持域名
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
     * @apiNote 手机号更新比较频繁, 此API无法确保一直可用
     */
    @Deprecated
    public static boolean isMobileExact(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_CHINA_MOBILE_REGEX_PATTERN.matcher(phone).matches() ||
                PHONE_CHINA_TELECOM_REGEX_PATTERN.matcher(phone).matches() ||
                PHONE_CHINA_UNION_REGEX_PATTERN.matcher(phone).matches();
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
        return MOBILE_SIMPLE_REGEX_PATTERN.matcher(phone).matches();
    }


    /**
     * 验证 IPV4
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPV4(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }
        return IPV4_REGEX_PATTERN.matcher(ipAddress).matches();
    }


    /**
     * 验证 IPV6
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPV6(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }
        return IPV6_REGEX_PATTERN.matcher(ipAddress).matches();
    }


    /**
     * 验证 IPV4|6
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIP(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }
        ipAddress = ipAddress.toLowerCase();
        return IPV4_REGEX_PATTERN.matcher(ipAddress).matches() || IPV6_REGEX_PATTERN.matcher(ipAddress).matches();
    }


    /**
     * 是否合理端口
     *
     * @param port
     * @return
     */
    public static boolean isPort(int port) {
        return port >= PORT_MIN && port <= PORT_MAX;
    }


    /**
     * 是否有效端口
     *
     * @param effectivePort
     * @return
     */
    public static boolean isPortEffective(int effectivePort) {
        return effectivePort >= PORT_EFFECTIVE && effectivePort <= PORT_MAX;
    }

    /**
     * 验证时间是否合法
     * yyyy-MM-dd          2019-1-11 2019-01-11
     * yyyy-MM-dd HH:mm:ss 2019-10-22 01:11:11 true 2019-10-22 1:11:11  false
     * yyyy-MM-dd hh:mm:ss 2019-10-22 01:11:11 true 2019-10-22 13:11:11 false
     *
     * @param datetime
     * @param dateFormatEnum 目前只支持两种格式校验:  yyyy-MM-dd yyyy-MM-dd hh:mm:ss yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean isLegalDate(String datetime, DateFormatEnum dateFormatEnum) {
        if (StringUtils.isBlank(datetime)) {
            return false;
        }
        Preconditions.checkNotNull(dateFormatEnum, "DateFormatEnum 不能为null");
        switch (dateFormatEnum) {
            case YEAR_MONTH_DAY:
                return REGEX_YEAR_MONTH_DAY_PATTERN.matcher(datetime).matches();
            case YEAR_MONTH_DAY_12:
                return REGEX_YEAR_MONTH_DAY_H_M_S_12_PATTERN.matcher(datetime).matches();
            case YEAR_MONTH_DAY_24:
                return REGEX_YEAR_MONTH_DAY_H_M_S_24_PATTERN.matcher(datetime).matches();
            default:
                throw new IllegalArgumentException("DateFormatEnum 格式不支持");
        }
    }


}
