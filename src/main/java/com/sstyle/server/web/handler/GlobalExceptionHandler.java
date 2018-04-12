package com.sstyle.server.web.handler;

import com.aliyuncs.exceptions.ClientException;
import com.sstyle.server.exception.StaffInvalidException;
import com.sstyle.server.exception.TokenInvalidException;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.exception.UserExistException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Created by ss on 18/3/14.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String RSPCODE_SUCCESS = "0";
    private final String RSPCODE_FAILED = "1";
    private final String RSPCODE_TOKEN_INVALID = "10";
    private final String RSPCODE_STAFF_INVALID = "11";
    private final String RSPCODE_STAFF_NOAUTHORIZATION = "12";
    private final String OAUTH_SUCCESS = "1";
    private final String OAUTH_FAILED = "0";

    @ExceptionHandler(value = TokenInvalidException.class)
    public JSONResult handleTokenInvalidException(TokenInvalidException tie) {
        return new JSONResult(RSPCODE_TOKEN_INVALID, "登录信息已失效，请重新登录");
    }

    @ExceptionHandler(value = StaffInvalidException.class)
    public JSONResult handleStaffInvalidException(StaffInvalidException sie) {
        return new JSONResult(RSPCODE_STAFF_INVALID, "员工信息不存在，请重新登录");
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public JSONResult handleStaffNoAuthorizationException(AuthorizationException sie) {
        return new JSONResult(RSPCODE_STAFF_NOAUTHORIZATION, "没有权限");
    }

    @ExceptionHandler(value = Throwable.class)
    public JSONResult handleThrowable(Throwable throwable) {
        throwable.printStackTrace();
        return new JSONResult(RSPCODE_FAILED, "请求失败");
    }

    @ExceptionHandler(value = ClientException.class)
    public JSONResult handleClientException(ClientException e) {
        return new JSONResult(RSPCODE_FAILED, "验证码发送失败");
    }

    @ExceptionHandler(value = UserExistException.class)
    public JSONResult handleUserExistException(UserExistException e) {
        return new JSONResult(RSPCODE_FAILED, "用户已经存在");
    }

}
