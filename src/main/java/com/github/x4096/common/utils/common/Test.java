package com.github.x4096.common.utils.common;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: peng.zhup
 * @Project: common-utils
 * @DateTime: 2019/6/2 10:47
 * @Description:
 */
public class Test {

    public static void main(String[] args) throws IOException {

        Map<String, String> province2 = new HashMap<String, String>(34);

        Map<String, String> province4 = new HashMap<String, String>(1000);


        Map<String, String> map = Maps.newHashMapWithExpectedSize(3306);

        // 后4位为0 则说明是一个省或者直辖市
        // 后两位是0 则说明是市
        // 然后是县或者区
        File file = new File("C:\\Users\\0x4096\\Desktop\\sfz.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);



        String str = null;
        while(true) {
            str = reader.readLine();
            if(str != null){
                String[] strings = str.split("\t");
                String code = strings[0];
                String name = strings[1];

                /* 省或直辖市 */
                String privince4 = code.substring(2, 6);

                String prefix = code.substring(0, 2);
                String middle = code.substring(3, 5);
                String suffix = code.substring(4, 6);
                if(StringUtils.equals(privince4, "0000")){
                    // String result = "map.put(%s, %s)";
                    province2.put(prefix, name);
                    // System.out.printf("map.put(%s, %s)\n", code.substring(0, 2), name);
                }else if(StringUtils.equals("00", suffix)){
                    /* 省市 */
                    province4.put(prefix + middle, province2.get(prefix) + name);
                }

                // System.out.println( str );
            }else{
                break;
            }
        }

    }


}
