package com.x4096.common.utils.reflect;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static io.lettuce.core.internal.LettuceClassUtils.CGLIB_CLASS_SEPARATOR;


/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/20
 * @instructions: 参考来源: https://github.com/lovey900119/okde/blob/e11fb350871e939250a4af3d70a8d536027965fc/cedu_pss_web/src/java/net/cedu/common/reflection/ClassUtil.java
 */
public class ClassUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 系统启动,WEB-INF/classes路径=
     */
    private static String PATH = "";


    /**
     * 取得某一类所在包的所有类名 不含迭代
     *
     * @param classLocation
     * @param packageName
     * @return
     */
    public static String[] getPackageAllClassName(String classLocation, String packageName) {
        // 将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        for (int i = 0, packageLength = packagePathSplit.length; i < packageLength; i++) {
            realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
        }
        File packeageDir = new File(realClassLocation);
        if (packeageDir.isDirectory()) {
            String[] allClassName = packeageDir.list();
            return allClassName;
        }
        return null;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(Package pack) {
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack.getName();
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class")&& !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            LOGGER.error("添加用户自定义视图类错误 找不到此类的.class文件");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error("在扫描用户定义视图时从jar包获取文件出错", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("IO异常", e);
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            LOGGER.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(
                        packageName + "." + file.getName(),
                        file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("添加用户自定义视图类错误 找不到此类的.class文件", e);
                }
            }
        }
    }



    /**
     * 根据包名获取包下所有的类名
     * @param basePackpageName
     * @return
     */
    public static List<String> getClassNames(String basePackpageName) {
        return getClassNames(new ArrayList<>(), basePackpageName);
    }

    /**
     * 递归算法，获取，某个基包下的所有类
     *
     * @param basePackpageName
     * @return
     */
    private static List<String> getClassNames(List<String> classList,String basePackpageName) {
        String[] classStrs = getPackageAllClassName(ClassUtils.PATH, basePackpageName);
        for (String pg : classStrs) {
            if (!pg.substring(0, 1).equals(pg.substring(0, 1).toUpperCase())) {
                getClassNames(classList, basePackpageName + "." + pg);
            } else {
                if (pg.indexOf(".class") > 0) {
                    classList.add(basePackpageName + "." + pg.substring(0, pg.indexOf(".class")));
                }
            }
        }
        return classList;
    }

    /**
     * 获取CGLib处理过后的实体的原Class.
     */
    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 递归返回所有的SupperClasses，包含Object.class
     */
    public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
        return ClassUtils.getAllSuperclasses(cls);
    }


    /**
     * 递归返回本类及所有基类继承的接口，及接口继承的接口，比Spring中的相同实现完整
     */
    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        return ClassUtils.getAllInterfaces(cls);
    }

}
