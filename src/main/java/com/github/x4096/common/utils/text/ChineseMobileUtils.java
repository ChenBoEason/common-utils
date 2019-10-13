package com.github.x4096.common.utils.text;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-12 23:44
 * @Description:
 */
public class ChineseMobileUtils {

    private ChineseMobileUtils() {
    }

    private static final int[] MOBILE_PREFIX = new int[]{133, 153, 177, 180,
            181, 189, 134, 135, 136, 137, 138, 139, 150, 151, 152, 157, 158, 159,
            178, 182, 183, 184, 187, 188, 130, 131, 132, 155, 156, 176, 185, 186,
            145, 147, 170};

    public static String generate() {
        return genMobilePre() + StringUtils.leftPad("" + RandomUtils.nextInt(0, 99999999 + 1), 8, "0");
    }


    private static String genMobilePre() {
        return "" + MOBILE_PREFIX[RandomUtils.nextInt(0, MOBILE_PREFIX.length)];
    }

}
