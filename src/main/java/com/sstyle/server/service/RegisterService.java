package com.sstyle.server.service;


import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.exception.UserExistException;

import java.util.Map;

/**
 * @Author ss
 * @Date 2018/4/9 15:09
 */
public interface RegisterService {
    JSONResult initCaptcha(String phoneNum);
    JSONResult saveUser(Map params) throws UserExistException;
    JSONResult activate(String checkCode, String userId) throws UserExistException;
    int checkPhoneNum(String phoneNum);
    int saveUserName(String userName, String userId);
}
