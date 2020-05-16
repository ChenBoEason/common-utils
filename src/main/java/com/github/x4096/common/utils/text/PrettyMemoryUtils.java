package com.github.x4096.common.utils.text;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-13 23:59
 * @Description: 字节单位换算
 * 参考: https://github.com/shalousun/ApplicationPower/blob/master/Common-util/src/main/java/com/power/common/util/PrettyMemoryUtil.java
 */
public class PrettyMemoryUtils {

    private PrettyMemoryUtils() {
    }

    private static final int UNIT = 1024;

    /**
     * @param byteSize 字节
     * @return size of memory
     */
    public static String prettyByteSize(long byteSize) {
        double size = 1.0 * byteSize;
        String type;

        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "B";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "KB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "MB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "GB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "TB";
            return format(size, type);
        }

        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            type = "PB";
            return format(size, type);
        }
        return ">PB";
    }


    private static String format(double size, String type) {
        int precision;

        if (size * 1000 % 10 > 0) {
            precision = 3;
        } else if (size * 100 % 10 > 0) {
            precision = 2;
        } else if (size * 10 % 10 > 0) {
            precision = 1;
        } else {
            precision = 0;
        }

        String formatStr = "%." + precision + "f";

        if ("KB".equals(type)) {
            return String.format(formatStr, (size)) + "KB";
        } else if ("MB".equals(type)) {
            return String.format(formatStr, (size)) + "MB";
        } else if ("GB".equals(type)) {
            return String.format(formatStr, (size)) + "GB";
        } else if ("TB".equals(type)) {
            return String.format(formatStr, (size)) + "TB";
        } else if ("PB".equals(type)) {
            return String.format(formatStr, (size)) + "PB";
        }

        return String.format(formatStr, (size)) + "B";
    }

}
