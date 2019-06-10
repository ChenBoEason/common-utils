package com.x4096.common.utils.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2019/1/11
 * @instructions: 证件号工具类
 * 参考: https://github.com/javahongxi/whatsmars/blob/master/whatsmars-common/src/main/java/org/hongxi/whatsmars/common/util/IdCardUtils.java
 */
public class IDCardUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDCardUtils.class);


    /**
     * 中国公民身份证号码最小长度
     */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度
     */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 省、直辖市代码表
     */
    private static final String CITY_CODE[] = {
            "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
            "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
            "81", "82", "91"
    };

    /**
     * 每位加权因子
     */
    private static final int POWER[] = {
            7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
    };

    /** 第18位校检码 */
    public static final String VERIFY_CODE[] = {
            "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
    };

    /**
     * 最低年限
     */
    private static final int MIN = 1930;

    /**
     * 省份编码
     */
    private static Map<String, String> provinceCode = new HashMap<String, String>(34);

    /**
     * 台湾身份首字母对应数字
     */
    private static Map<String, Integer> twFirstCode = new HashMap<String, Integer>(25);


    /**
     * 香港身份首字母对应数字
     */
    public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>(9);


    static {
        provinceCode.put("11", "北京");
        provinceCode.put("12", "天津");
        provinceCode.put("13", "河北");
        provinceCode.put("14", "山西");
        provinceCode.put("15", "内蒙古");
        provinceCode.put("21", "辽宁");
        provinceCode.put("22", "吉林");
        provinceCode.put("23", "黑龙江");
        provinceCode.put("31", "上海");
        provinceCode.put("32", "江苏");
        provinceCode.put("33", "浙江");
        provinceCode.put("34", "安徽");
        provinceCode.put("35", "福建");
        provinceCode.put("36", "江西");
        provinceCode.put("37", "山东");
        provinceCode.put("41", "河南");
        provinceCode.put("42", "湖北");
        provinceCode.put("43", "湖南");
        provinceCode.put("44", "广东");
        provinceCode.put("45", "广西");
        provinceCode.put("46", "海南");
        provinceCode.put("50", "重庆");
        provinceCode.put("51", "四川");
        provinceCode.put("52", "贵州");
        provinceCode.put("53", "云南");
        provinceCode.put("54", "西藏");
        provinceCode.put("61", "陕西");
        provinceCode.put("62", "甘肃");
        provinceCode.put("63", "青海");
        provinceCode.put("64", "宁夏");
        provinceCode.put("65", "新疆");
        provinceCode.put("71", "台湾");
        provinceCode.put("81", "香港");
        provinceCode.put("82", "澳门");
        provinceCode.put("91", "国外");
        twFirstCode.put("A", 10);
        twFirstCode.put("B", 11);
        twFirstCode.put("C", 12);
        twFirstCode.put("D", 13);
        twFirstCode.put("E", 14);
        twFirstCode.put("F", 15);
        twFirstCode.put("G", 16);
        twFirstCode.put("H", 17);
        twFirstCode.put("J", 18);
        twFirstCode.put("K", 19);
        twFirstCode.put("L", 20);
        twFirstCode.put("M", 21);
        twFirstCode.put("N", 22);
        twFirstCode.put("P", 23);
        twFirstCode.put("Q", 24);
        twFirstCode.put("R", 25);
        twFirstCode.put("S", 26);
        twFirstCode.put("T", 27);
        twFirstCode.put("U", 28);
        twFirstCode.put("V", 29);
        twFirstCode.put("X", 30);
        twFirstCode.put("Y", 31);
        twFirstCode.put("W", 32);
        twFirstCode.put("Z", 33);
        twFirstCode.put("I", 34);
        twFirstCode.put("O", 35);
        hkFirstCode.put("A", 1);
        hkFirstCode.put("B", 2);
        hkFirstCode.put("C", 3);
        hkFirstCode.put("R", 18);
        hkFirstCode.put("U", 21);
        hkFirstCode.put("Z", 26);
        hkFirstCode.put("X", 24);
        hkFirstCode.put("W", 23);
        hkFirstCode.put("O", 15);
        hkFirstCode.put("N", 14);
    }

    /**
     * 将15位身份证号码转换为18位
     *
     * @param idCard    15位身份编码
     * @return          18位身份编码
     */
    public static String conver15CardTo18(String idCard) {
        if(StringUtils.isBlank(idCard)){
            throw new NullPointerException("身份证号不能为空!");
        }

        if(StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH){
            throw new IllegalArgumentException("身份证号只能为15位");
        }

        String idCard18 = "";

        if (isNum(idCard)) {
            /* 获取出生年月日 */
            String birthday = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
            } catch (ParseException e) {
                LOGGER.error("身份证生日信息格式化异常", e);
            }
            Calendar cal = Calendar.getInstance();
            if (birthDate != null){
                cal.setTime(birthDate);
            }
            /* 获取出生年(完全表现形式,如：2010) */
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            /* 转换字符数组 */
            char[] cArr = idCard18.toCharArray();
            int[] iCard = convertCharToInt(cArr);
            int iSum17 = getPowerSum(iCard);
            /* 获取校验位 */
            String sVal = getCheckCode18(iSum17);
            if (sVal.length() > 0) {
                idCard18 += sVal;
            } else {
                return null;
            }
        } else {
            return null;
        }
        return idCard18;
    }



    /**
     * 验证18位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean is18IdCard(String idCard) {
        if(StringUtils.isBlank(idCard) || StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH){
            return false;
        }

        boolean bTrue = false;
        /* 前17位 */
        String code17 = idCard.substring(0, 17);
        /* 第18位 */
        String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
        if (isNum(code17)) {
            char[] cArr = code17.toCharArray();
            int[] iCard = convertCharToInt(cArr);
            int iSum17 = getPowerSum(iCard);
            /* 获取校验位 */
            String val = getCheckCode18(iSum17);
            if (val.length() > 0) {
                if (val.equalsIgnoreCase(code18)) {
                    bTrue = true;
                }
            }
        }
        return bTrue;
    }

    /**
     * 验证15位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean is15IdCard(String idCard) {
        if(StringUtils.isBlank(idCard) || StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH){
            return false;
        }

        if (isNum(idCard)) {
            String proCode = idCard.substring(0, 2);
            if (provinceCode.get(proCode) == null) {
                return false;
            }
            String birthCode = idCard.substring(6, 12);
            Date birthDate = null;
            try {
                birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
            } catch (ParseException e) {
                LOGGER.error("身份证生日信息格式化异常", e);
            }
            Calendar cal = Calendar.getInstance();

            if (birthDate != null){
                cal.setTime(birthDate);
            }

            if (!validateDate(cal.get(Calendar.YEAR), Integer.valueOf(birthCode.substring(2, 4)),
                    Integer.valueOf(birthCode.substring(4, 6)))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }



    /**
     * 将字符数组转换成数字数组
     *
     * @param ca 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            LOGGER.error("字符串转换数字异常", e);
        }
        return iArr;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param iArr
     * @return 身份证编码。
     */
    private static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < POWER.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * POWER[j];
                    }
                }
            }
        }
        return iSum;
    }

    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum
     * @return 校验位
     */
    private static String getCheckCode18(int iSum) {
        String sCode = "";
        switch (iSum % 11) {
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
            default:
                sCode = null;
                break;
        }
        return sCode;
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param idCard    身份编号
     * @return          年龄
     */
    public static int getAge(String idCard) {
        if(StringUtils.isBlank(idCard)
            || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }

        int age = 0;
        if (StringUtils.length(idCard) == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        String year = idCard.substring(6, 10);
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        age = thisYear - Integer.valueOf(year);
        return age;
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idCard    身份编号
     * @return          生日 格式: yyyy-MM-dd exp: 1984-01-01
     */
    public static String getBirth(String idCard) {
        if(StringUtils.isBlank(idCard)
                || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }

       if (StringUtils.length(idCard) == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(6, 10) + "-" + idCard.substring(10,12) + "-" + idCard.substring(12,14);
    }

    /**
     * 根据身份编号获取出生年
     *
     * @param idCard    身份编号
     * @return          出生年份 yyyy
     */
    public static String getYear(String idCard) {
        if(StringUtils.isBlank(idCard)
                || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }

         if (StringUtils.length(idCard) == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(6, 10);
    }

    /**
     * 根据身份编号获取出生月份
     *
     * @param idCard    身份编号
     * @return          出生月份 MM
     */
    public static String getMonth(String idCard) {
        if(StringUtils.isBlank(idCard)
                || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }

        if (StringUtils.length(idCard) == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(10, 12);
    }

    /**
     * 根据身份编号获取出生天
     *
     * @param idCard      身份编号
     * @return            出生天 dd
     */
    public static String getDay(String idCard) {
        if(StringUtils.isBlank(idCard)
                || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }

        if (StringUtils.length(idCard) == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }
        return idCard.substring(12, 14);
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idCard        身份编号
     * @return              性别(M-男, F-女)
     */
    public static String getGender(String idCard) {
        if(StringUtils.isBlank(idCard)
                || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }

        String gender = null;

        if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            idCard = conver15CardTo18(idCard);
        }

        String cardNum = idCard.substring(16, 17);
        if (Integer.parseInt(cardNum) % 2 != 0) {
            gender = "M";
        } else {
            gender = "F";
        }
        return gender;
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idCard 身份编码
     * @return 省级编码。
     */
    public static String getProvince(String idCard) {
        if(StringUtils.isBlank(idCard)
                || (StringUtils.length(idCard) != CHINA_ID_MIN_LENGTH && StringUtils.length(idCard) != CHINA_ID_MAX_LENGTH)){
            throw new IllegalArgumentException("请输入正确的身份证号码!");
        }
        int len = idCard.length();
        String sProvince = null;
        String sProvinNum = "";
        if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
            sProvinNum = idCard.substring(0, 2);
        }
        sProvince = provinceCode.get(sProvinNum);
        return sProvince;
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    private static boolean isNum(String str) {
        return StringUtils.isNotBlank(str) && str.matches("^[0-9]*$");
    }



    /**
     * 验证小于当前日期 是否有效
     *
     * @param iYear 待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate 待验证日期(日)
     * @return 是否有效
     */
    private static boolean validateDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < MIN || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }
        switch (iMonth) {
            case 4:
            case 6:
            case 9:
            case 11:
                datePerMonth = 30;
                break;
            case 2:
                boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
                        && (iYear > MIN && iYear < year);
                datePerMonth = dm ? 29 : 28;
                break;
            default:
                datePerMonth = 31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }


    public static void main(String[] args) {
        System.out.println(is18IdCard("101111111111111111"));
        // 411323198909011460
        // 411323198909011460
        System.out.println(conver15CardTo18("411323890901146"));

        System.out.println(is15IdCard("411323890901146"));
        System.out.println(is18IdCard("411323198909011460"));
        System.out.println(getAge("411323198909011460"));
        System.out.println(getBirth("411323198909011460"));
        System.out.println(getYear("411323198909011460"));
        System.out.println(getMonth("411323198909011460"));
        System.out.println(getDay("411323198909011460"));
        System.out.println(getGender("411323198909011460"));
        System.out.println(getProvince("411323198909011460"));


    }
}
