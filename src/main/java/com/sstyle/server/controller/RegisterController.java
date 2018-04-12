package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.exception.UserExistException;
import com.sstyle.server.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @Author ss
 * @Date 2018/4/9 15:08
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/init-captcha", method = RequestMethod.POST)
    public JSONResult initCaptcha(@RequestParam String phoneNum) {
        return registerService.initCaptcha(phoneNum);
    }

    @RequestMapping(value = "/saveUserInfo", method = RequestMethod.POST)
    public JSONResult saveUserInfo(@RequestBody Map params) throws UserExistException{
        return registerService.saveUser(params);
    }

    @RequestMapping(value = "/activate")
    public JSONResult activate(String checkCode, String userId) throws UserExistException{
        return registerService.activate(checkCode, userId);
    }
}
