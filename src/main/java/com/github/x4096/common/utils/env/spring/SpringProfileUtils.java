package com.github.x4096.common.utils.env.spring;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/12/8 2:11 下午
 * @Description:
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
        return StringUtils.isNotBlank(profile) && profile.contains(profile);
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
