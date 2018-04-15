package com.sstyle.server.web.interceptor;

import com.sstyle.server.exception.TokenInvalidException;
import com.sstyle.server.utils.AESUtil;
import com.sstyle.server.utils.ThreadContext;
import com.sstyle.server.utils.RedisClient;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Id;
import org.n3r.idworker.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * Created by ss on 18/3/14.
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    private final int TOKEN_KEY_EXPIRE_TIME = 60 * 60 * 5 * 100;
    public static final String GT_INIT_PATH = "/gt/init";
    public static final String GT_LOGIN_PATH = "/gt/check";
    public static final String BLOG_REGISTER_INIT = "/register/init-captcha";
    public static final String BLOG_REGISTER_SAVE = "/register/saveUserInfo";
    public static final String BLOG_REGISTER_ACTIVE = "/register/activate";
    public static final String BLOG_PORTAL = "/blog/init";
    public static final String BLOG_CHECK_LOGIN = "/blog/check-login";
    public static final String AUTHORIZATION_HEADER = "Authorization";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String servletPath = request.getServletPath();

        // 过滤 No AUTH 的请求
        if (servletPath.equalsIgnoreCase(GT_INIT_PATH) || servletPath.equalsIgnoreCase(GT_LOGIN_PATH)
                || servletPath.equalsIgnoreCase(BLOG_REGISTER_INIT) || servletPath.equalsIgnoreCase(BLOG_REGISTER_SAVE)
                || servletPath.equalsIgnoreCase(BLOG_REGISTER_ACTIVE) || servletPath.equalsIgnoreCase(BLOG_PORTAL)
                || servletPath.equalsIgnoreCase(BLOG_CHECK_LOGIN)) {
            return true;
        }


        // 拦截 Token 不存在的请求
        String authToken = getAuthToken(request);
        if (StringUtils.isBlank(authToken)) {
            throw new TokenInvalidException();
        }

        // 校验 Auth Token
        try {
            String json = RedisClient.get(authToken);
            if (json == null) {
                throw new TokenInvalidException();
            }
            RedisClient.expire(authToken, TOKEN_KEY_EXPIRE_TIME);
            String[] staff = AESUtil.aesDecode(authToken).split("\\|");
            ThreadContext.setStaffCode(staff[0]);
            logger.error("staff ========{}" + Arrays.toString(staff));
            ThreadContext.setStaffId(staff[1]);
        } catch (Exception e) {
            logger.warn("auth token is invalid", e);
            throw new TokenInvalidException();
        }

        return true;
    }

    private String getAuthToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

}
