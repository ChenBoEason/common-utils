package com.x4096.common.utils.network;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/20
 * @instructions: 网络端口工具类,用于检测指定host的指定端口是否能连接
 */
public class PortUtils {

    /**
     * 检测指定host的指定端口是否能连接,默认连接超时时间2秒
     *
     * @param host
     * @param port
     * @return
     */
    public static boolean isConnect(String host, int port){
        Socket connect = new Socket();
        try {
            connect.connect(new InetSocketAddress(host, port),2000);
            return connect.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检测指定host的指定端口是否能连接
     *
     * @param host
     * @param port
     * @param timeout 单位毫秒
     * @return
     */
    public static boolean isConnect(String host, int port, int timeout){
        Socket connect = new Socket();
        try {
            connect.connect(new InetSocketAddress(host, port), timeout);
            return connect.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
