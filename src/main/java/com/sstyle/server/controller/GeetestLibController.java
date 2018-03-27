package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.Right;
import com.sstyle.server.domain.User;
import com.sstyle.server.service.RightService;
import com.sstyle.server.service.UserService;
import com.sstyle.server.utils.AESUtil;
import com.sstyle.server.utils.GeetestLib;
import com.sstyle.server.utils.MD5Util;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author ss
 * @Date 2018/3/27 10:36
 */

@RestController
@RequestMapping("/gt")
public class GeetestLibController {

    @Autowired
    private UserService userService;

    @Autowired
    private RightService rightService;

    private final String ERROR_CALLBACK = "1";

    private Logger logger = LoggerFactory.getLogger(GeetestLibController.class);

    @RequestMapping(value = "/init",method = RequestMethod.GET)
    public JSONResult initGit(HttpServletRequest request) {
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);

        resStr = gtSdk.getResponseStr();

        return new JSONResult(resStr);
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public JSONResult checkValidate(@RequestParam Map formMap, HttpServletRequest request) {
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key, GeetestLib.newfailback);

        String challenge = MapUtils.getString(formMap, "geetest_challenge");
        String validate =  MapUtils.getString(formMap, "geetest_validate");
        String seccode = MapUtils.getString(formMap, "geetest_seccode");

        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

        //从session中获取userid
        String userid = (String)request.getSession().getAttribute("userid");

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", userid);

        int gtResult = 0;
        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证

            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证

            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }
        String userName = MapUtils.getString(formMap, "userName");

        if (gtResult == 1) {
            UsernamePasswordToken token = new UsernamePasswordToken(userName,
                    AESUtil.aesEncode(MapUtils.getString(formMap, "password")));

            //获取当前的Subject
            Subject subject = SecurityUtils.getSubject();
            try {
                logger.info("对用户[" + userName + "]进行登录验证..验证开始");
                subject.login(token);
                logger.info("对用户[" + userName + "]进行登录验证..验证通过");
            } catch (UnknownAccountException uae) {
                logger.info("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");
            } catch (IncorrectCredentialsException ice) {
                logger.info("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");
            }

            //验证是否登录成功
            if (subject.isAuthenticated()) {
                logger.info("用户[" + userName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                User user = userService.findByUsername(userName);
                List<Right> rightList = rightService.findRights(user.getId());

                // 生成 AUTH Token
                String staffToken = MD5Util.md5((new StringBuilder()).append(userName).append(user.getId()).toString());

                JSONResult result = new JSONResult(com.sstyle.server.utils.MapUtils.of("token", staffToken, "staff", user, "menus", rightList));

                subject.getSession().setTimeout(1000 * 60 * 60 * 24);
                return new JSONResult("ok");
            } else {
                token.clear();
                return new JSONResult(ERROR_CALLBACK, "用户名或密码错误，请重新输入并完成验证");
            }
        }
        else {
            return new JSONResult("failed");
        }
    }
}
