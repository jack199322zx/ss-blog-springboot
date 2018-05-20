package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.OpenOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ss on 2018/5/20.
 */
@RestController
@RequestMapping("/oath")
public class OpenOathController {

    @Autowired
    private OpenOauthService openOauthService;

    @RequestMapping(value = "/link-me")
    public void linkMe() throws Exception{
        openOauthService.openQQchat();
    }
}
