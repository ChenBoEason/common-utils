package com.x4096.common.utils.network;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: IP 地址工具集, 包括网络请求IP, 本地IP(支持多网卡,只获取主机IP地址)
 */
public class IpUtils {

    private static final String LOCAL_HOST_V4 = "127.0.0.1";

    private static final String LOCAL_HOST_V6 = "0:0:0:0:0:0:0:1";

    private static final int IP_LENGTH = 15;

    private static final String SEPARATOR = ",";

    private static final String UNKNOWN = "unknown";

    /**
     * 获取网络请求IP地址
     *
     * @param request
     * @return
     */
    public static String getNetIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");

        if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCAL_HOST_V4.equals(ipAddress) || LOCAL_HOST_V6.equals(ipAddress)){
                /* 根据网卡取本机配置的IP*/
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }

        /* 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 */
        if(ipAddress != null && ipAddress.length() > IP_LENGTH){
            if(ipAddress.indexOf(SEPARATOR)>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(SEPARATOR));
            }
        }

        return ipAddress;
    }


    /**
     * 获取主机 IP ,去除存在虚拟机情况
     *
     * @return
     */
    public static String getLocalIpAddr() {
        InetAddress candidateAddress = null;
        try {
            /* 遍历所有的网络接口 */
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                /* 在所有的接口下再遍历IP */
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    /* 排除loopback类型地址 */
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            /* 如果是site-local地址，就是它了 */
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            /* site-local类型的地址未被发现，先记录候选地址 */
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }

            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }

            /* 如果没有发现 non-loopback地址.只能用最次选的方案 */
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            try {
                throw unknownHostException;
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
            return null;
        }

    }

    /**
     * 获取本机 Host 名称.
     *
     * @return 本机Host名称
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }


}
