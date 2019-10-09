package com.github.x4096.common.utils.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/20
 * @instructions: 网络端口工具类, 用于检测指定host的指定端口是否能连接
 */
public class PortUtils {

    private PortUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PortUtils.class);

    /**
     * 检测指定host的指定端口是否能连接,默认连接超时时间2秒
     *
     * @param host 主机
     * @param port 端口
     * @return
     */
    public static boolean isConnect(String host, int port) {
        return isConnect(host, port, 2_000);
    }

    /**
     * 检测指定host的指定端口是否能连接
     *
     * @param host    主机
     * @param port    端口
     * @param timeout 超时时间, 单位毫秒
     * @return
     */
    public static boolean isConnect(String host, int port, int timeout) {
        Socket connect = new Socket();
        try {
            connect.connect(new InetSocketAddress(host, port), timeout);
            return connect.isConnected();
        } catch (IOException e) {
            LOGGER.error("Socket 连接异常", e);
        } finally {
            try {
                connect.close();
            } catch (IOException e) {
                LOGGER.error("Socket 关闭异常", e);
            }
        }
        return false;
    }

}
