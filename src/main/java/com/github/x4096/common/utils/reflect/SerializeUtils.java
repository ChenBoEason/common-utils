package com.github.x4096.common.utils.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/20
 * @instructions: 序列化工具类
 */
public class SerializeUtils {

    private static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    /**
     * 序列化对象到文件
     *
     * @param filePath
     * @param object
     */
    public static void serialize(String filePath, Object object) {
        File file = new File(filePath);
        if (file.isDirectory() || !file.exists()) {
            throw new IllegalArgumentException("请输入正确的需要保存的文件路径!");
        }

        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("待序列化对象未实现Serializable接口");
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(object);
        } catch (Exception e) {
            logger.error("序列号文件输出异常", e);
        }
    }

    /**
     * 从文件反序列化到对象
     *
     * @param filePath
     * @return
     */
    public static Object deserialize(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            throw new IllegalArgumentException("需要反序列化的文件路径不能为null或''");
        }

        File file = new File(filePath);
        if (file.isDirectory() || !file.exists()) {
            throw new IllegalArgumentException("请输入正确的需要反序列化的文件路径!");
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            return inputStream.readObject();
        } catch (Exception e) {
            logger.error("读取文件异常", e);
        }

        return null;
    }

}
