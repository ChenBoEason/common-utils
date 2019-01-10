package com.github.common.utils.jar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/20
 * @instructions: 提供Jar隔离的加载机制，会把传入的路径、及其子路径、以及路径中的jar文件加入到class path
 * 参考代码: https://github.com/Arvin-Mark/DataX-src/blob/5bc8382de7983f8ae3ee356f9313ec9e8d63e585/core/src/main/java/com/alibaba/datax/core/util/container/JarLoader.java
 */
public final class JarLoader extends URLClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(JarLoader.class);

    public JarLoader(String[] paths) {
        this(paths, JarLoader.class.getClassLoader());
    }

    public JarLoader(String[] paths, ClassLoader parent) {
        super(getURLs(paths), parent);
    }

    private static URL[] getURLs(String[] paths) {
        Validate.isTrue(null != paths && 0 != paths.length,"jar包路径不能为空.");

        List<String> dirs = new ArrayList<String>();
        for (String path : paths) {
            dirs.add(path);
            JarLoader.collectDirs(path, dirs);
        }

        List<URL> urls = new ArrayList<URL>();
        for (String path : dirs) {
            urls.addAll(doGetURLs(path));
        }

        return urls.toArray(new URL[0]);
    }

    private static void collectDirs(String path, List<String> collector) {
        if (StringUtils.isBlank(path)) {
            return;
        }

        File current = new File(path);
        if (!current.exists() || !current.isDirectory()) {
            return;
        }

        for (File child : current.listFiles()) {
            if (!child.isDirectory()) {
                continue;
            }
            collector.add(child.getAbsolutePath());
            collectDirs(child.getAbsolutePath(), collector);
        }
    }

    private static List<URL> doGetURLs(final String path) {
        Validate.isTrue(!StringUtils.isBlank(path), "jar包路径不能为空.");

        File jarPath = new File(path);

        Validate.isTrue(jarPath.exists() && jarPath.isDirectory(), "jar包路径必须存在且为目录.");

        /* set filter */
        FileFilter jarFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        };

        /* iterate all jar */
        File[] allJars = new File(path).listFiles(jarFilter);
        List<URL> jarURLs = new ArrayList<URL>(allJars.length);

        for (int i = 0; i < allJars.length; i++) {
            try {
                jarURLs.add(allJars[i].toURI().toURL());
            } catch (Exception e) {
                logger.error("系统加载jar包出错",e);
                throw new RuntimeException("Error in getting jar URL", e);
            }
        }

        return jarURLs;
    }

}
