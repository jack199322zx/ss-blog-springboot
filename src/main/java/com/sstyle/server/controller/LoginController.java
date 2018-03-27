package com.sstyle.server.controller;


import com.alibaba.fastjson.JSON;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.StaffBean;
import com.sstyle.server.service.UserService;
import com.sstyle.server.utils.GeetestLib;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ss on 2018/3/24.
 */
@RestController
@RequestMapping("/broker")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public JSONResult doLogin(@RequestParam String userName, @RequestParam String password) {
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JSONResult doLogout() {
        SecurityUtils.getSubject().logout();
        return new JSONResult("您已安全退出");
    }

}
