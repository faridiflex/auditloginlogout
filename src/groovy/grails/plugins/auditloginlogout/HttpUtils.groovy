package grails.plugins.auditloginlogout

import org.apache.log4j.Logger

import javax.servlet.http.HttpServletRequest

class HttpUtils {

    static final Logger log = Logger.getLogger(HttpUtils)

    static String getClientIpAddr(HttpServletRequest request) {
        String ip = null
        try {
            ip = request?.getHeader("X-Forwarded-For")
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request?.getHeader("Proxy-Client-IP")
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
               ip = request?.getHeader("WL-Proxy-Client-IP")
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request?.getHeader("HTTP_CLIENT_IP")
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request?.getHeader("HTTP_X_FORWARDED_FOR")
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request?.getRemoteAddr()
            }
        } catch (Exception ex) {
            log.error("Failed to get the IP Address of the requester. ", ex)
        }
        return ip
    }
}