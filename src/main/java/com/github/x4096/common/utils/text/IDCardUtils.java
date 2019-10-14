package com.github.x4096.common.utils.text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-14 00:26
 * @Description: 身份证工具类
 */
public class IDCardUtils {

    private IDCardUtils() {
    }

    private static final String[] PROVINCES_CODE = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82"};

    private static final String[] checks = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "X"};

    private static final Random RANDOM = new Random();

    public static String generate() {
        String no = RANDOM.nextInt(899) + 100 + "";
        return randomOne(PROVINCES_CODE) +
                randomCityCode(18) +
                randomCityCode(28) +
                randomBirth(20, 50) +
                no + randomOne(checks);
    }


    private static String randomOne(String[] s) {
        return s[new Random().nextInt(s.length - 1)];
    }


    private static String randomCityCode(int max) {
        int i = new Random().nextInt(max) + 1;
        return i > 9 ? i + "" : "0" + i;
    }

    private static String randomBirth(int minAge, int maxAge) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        int randomDay = 365 * minAge + RANDOM.nextInt(365 * (maxAge - minAge));
        date.set(Calendar.DATE, date.get(Calendar.DATE) - randomDay);
        return dft.format(date.getTime());
    }

}
