package com.x4096.common.utils.common;

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
     * 中国居民身份证
     */
    private static final String CARD_ID = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)$";

    /**
     * 中文
     */
    private static final String CHINA_TEXT="[\\u4e00-\\u9fa5]+";


    private static final String PHONE_CHINA_TELECOM = "^1[3578][01379]\\d{8}$";

    private static final String PHONE_CHINA_UNION = "^1[34578][01256]\\d{8}$";

    private static final String PHONE_CHINA_MOBILE = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";

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
     * 检测身份证格式是否正确
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        return Pattern.matches(CARD_ID, idCard);
    }

    /**
     * 验证字符串是否是中文
     *
     * @param chinaText
     * @return
     */
    public static boolean isChineseText(String chinaText){
        if(StringUtils.isBlank(chinaText)){
            return false;
        }
        return Pattern.matches(CHINA_TEXT,chinaText);
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

}
