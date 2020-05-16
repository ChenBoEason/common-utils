package com.github.x4096.common.utils.env.spring;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/12/8 2:11 下午
 * @Description: 若要使用该工具类, 需要 Spring 扫描 com.github.x4096.common.utils
 * exp: @SpringBootApplication(scanBasePackages = {"com.github.x4096.common.utils"})
 */
@Configuration
public class SpringProfileUtils implements InitializingBean, ApplicationContextAware {

    private static ApplicationContext context;

    private static Set<String> profileSet;


    /**
     * 获取当前环境参数  exp: dev,prod,test
     */
    public static Set<String> getActiveProfiles() {
        return profileSet;
    }


    /**
     * 是否包含当前环境
     *
     * @param profile 运行环境, exp: test,pro
     */
    public static boolean contain(String profile) {
        return profileSet.contains(profile);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String[] profiles = context.getEnvironment().getActiveProfiles();
        if (ArrayUtils.isEmpty(profiles)) {
            profileSet = new HashSet<>();
        } else {
            profileSet = new HashSet<>(Arrays.asList(profiles));
        }
    }

}
