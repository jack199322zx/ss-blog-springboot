package com.sstyle.server.web.interceptor;

import com.sstyle.server.exception.TokenInvalidException;
import com.sstyle.server.utils.AFAdminContext;
import com.sstyle.server.utils.StaffTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * Created by ss on 18/3/14.
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    private final long TOKEN_KEY_EXPIRE_TIME = 60 * 60 * 24 * 1000;
    public static final String PATH_STAFF_AUTH = "/staff/auth";
    public static final String PATH_CHECK_TOKEN = "/oauth/check-token";
    public static final String STATICS_RESOURCES_PATH = "/tv/statistics-resources";
    public static final String VISIT_STATICS_PATH = "/tv/visitstatistics";
    public static final String AUTHORIZATION_HEADER = "Authorization";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String servletPath = request.getServletPath();

        // 过滤 No AUTH 的请求
        if (servletPath.equalsIgnoreCase(PATH_STAFF_AUTH)
                || servletPath.equalsIgnoreCase(PATH_CHECK_TOKEN) || servletPath.equalsIgnoreCase("/tv/dau/insertDAU")
                || servletPath.equalsIgnoreCase("/tv/dau/insertFansData") || servletPath.equalsIgnoreCase("/tv/dau/insertUvsData")
                || servletPath.equalsIgnoreCase("/tv/dau/trailer-play-flow")
                || servletPath.equalsIgnoreCase("/tv/dau/insertPreference")
                || servletPath.equalsIgnoreCase("/tv/dau/addDayPvUv")
                || servletPath.equalsIgnoreCase("/tv/dau/addUserProfile")) {
            return true;
        }

        if (servletPath.toLowerCase().indexOf(STATICS_RESOURCES_PATH) != -1
                || servletPath.toLowerCase().indexOf("/program/survey") != -1
                || servletPath.toLowerCase().indexOf("/survey/export") != -1
                || servletPath.toLowerCase().indexOf("/program/error-export") != -1
                || servletPath.toLowerCase().indexOf("/program/export-templete") != -1
                || servletPath.toLowerCase().indexOf("/tv/orderform/export") != -1
                || servletPath.toLowerCase().indexOf("/program/export") != -1
                || servletPath.toLowerCase().indexOf("/program/download") != -1
                || servletPath.toLowerCase().indexOf("/excel/download") != -1
                || servletPath.toLowerCase().indexOf("/excel/export") != -1
                || servletPath.toLowerCase().indexOf(VISIT_STATICS_PATH) != -1) {
            return true;
        }

//
//        // 拦截 Token 不存在的请求
//        String authToken = getAuthToken(request);
//        if (StringUtils.isBlank(authToken)) {
//            throw new TokenInvalidException();
//        }
//
//        // 校验 Auth Token
//        try {
//            Date authTokenTime = StaffTokenUtils.getTokenTime(authToken);
//            authTokenTime.setTime(authTokenTime.getTime() + TOKEN_KEY_EXPIRE_TIME);
//            if (authTokenTime.before(new Date())) {
//                throw new TokenInvalidException();
//            }
//
//            String staffCode = StaffTokenUtils.decryptStaffToken(authToken);
//            if (StringUtils.isBlank(staffCode)) {
//                throw new RuntimeException("auth token is invalid");
//            }
//
//            AFAdminContext.setStaffCode(staffCode);
//            logger.error(StaffTokenUtils.decryptStaffIdToken(authToken));
//            AFAdminContext.setStaffId(StaffTokenUtils.decryptStaffIdToken(authToken));
//        } catch (Exception e) {
//            logger.warn("auth token is invalid", e);
//            throw new TokenInvalidException();
//        }

        return true;
    }

    private String getAuthToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

}
