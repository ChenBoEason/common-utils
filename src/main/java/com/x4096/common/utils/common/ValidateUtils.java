package com.x4096.common.utils.common;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.regex.Pattern;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2019/1/11
 * @instructions: 校验数据工具类,例如手机号,邮箱等
 * 参考: https://github.com/javahongxi/whatsmars/blob/master/whatsmars-common/src/main/java/org/hongxi/whatsmars/common/util/ValidateUtils.java
 */
public class ValidateUtils {

    /**
     * 邮箱
     */
    private static final String EMAIL = "^[a-z0-9]([a-z0-9]*[-_\\.\\+]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2,4})?$";


    /**
     * 中文
     */
    private static final String CHINA_TEXT="[\\u4e00-\\u9fa5]+";

    /**
     * 中国姓名 [\u4E00-\u9FA5\uf900-\ufa2d·s]{2,20}
     */
    private static final String CHINA_NAME ="[\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}";


    private static final String PHONE_CHINA_TELECOM = "^1[3578][01379]\\d{8}$";

    private static final String PHONE_CHINA_UNION = "^1[34578][01256]\\d{8}$";

    private static final String PHONE_CHINA_MOBILE = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";

    /**
     * 网络协议
     */
    private static final String[] schemas = {"http", "https"};

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
        return Pattern.matches(EMAIL, email);
    }

    /**
     * 验证中国手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return Pattern.matches(PHONE_CHINA_TELECOM, phone)
                || Pattern.matches(PHONE_CHINA_UNION, phone)
                || Pattern.matches(PHONE_CHINA_MOBILE, phone);
    }


    /**
     * 验证字符串是否是中文
     *
     * @param chineseText
     * @return
     */
    public static boolean isChineseText(String chineseText){
        if(StringUtils.isBlank(chineseText)){
            return false;
        }
        return Pattern.matches(CHINA_TEXT, chineseText);
    }


    /**
     * 验证是否为中国人姓名 包括少数民族,exp: 噶及·洛克业  外国人翻译为中文: 洛克·哈德森
     *
     * @param chineseName
     * @return
     */
    public static boolean isChineseName(String chineseName){
        if(StringUtils.isBlank(chineseName)){
            return false;
        }
        return Pattern.matches(CHINA_NAME, chineseName);
    }



    /**
     * 验证密码是否纯字母或纯数字
     *
     * @param password
     * @return
     */
    public static boolean isWeakPassword(String password) {
        if(StringUtils.isBlank(password)
                || password.matches("[a-zA-Z]+")
                || password.matches("[0-9]+")){
            return false;
        }
        if (password.matches("[a-zA-Z]+") || password.matches("[0-9]+")) {
            return false;
        }
        return true;
    }


    /**
     * 验证 URL 是否合法
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if(StringUtils.isBlank(url)){
            return false;
        }
        UrlValidator urlValidator = new UrlValidator(schemas);
        return urlValidator.isValid(url);
    }


    /**
     * 验证 URL 不合法
     *
     * @param url
     * @return
     */
    public static boolean isNotUrl(String url) {
        return !isUrl(url);
    }



    /**
     * 验证字符串是否为标准 JSON 字符串
     * TODO 是否有更好的方案?
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJSON(String jsonStr){
        if(StringUtils.isBlank(jsonStr)
            || ! StringUtils.startsWith(jsonStr, "{")
            || ! StringUtils.endsWith(jsonStr, "}")){
            return false;
        }
        try{
            JSON.parseObject(jsonStr);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 验证字符串是否为标准 JSON 字符串
     *
     * @param jsonStr
     * @return
     */
    public static boolean isNotJSON(String jsonStr){
        return !isJSON(jsonStr);
    }

}
