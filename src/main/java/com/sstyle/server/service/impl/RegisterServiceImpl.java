package com.sstyle.server.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.User;
import com.sstyle.server.exception.UserExistException;
import com.sstyle.server.mapper.RegisterMapper;
import com.sstyle.server.service.RegisterService;
import com.sstyle.server.utils.*;
import org.n3r.idworker.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author ss
 * @Date 2018/4/9 15:09
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;

    public static final String PRIMARY_USER_ROLE_ID = "40002";

    @Override
    public JSONResult initCaptcha(String phoneNum) {
        List<Map> dataList = new ArrayList<>();
        try {
            //发短信
            SendSmsResponse response = SmsUtil.sendSms(phoneNum);
            Thread.sleep(3000L);
            if (response.getCode() != null && response.getCode().equals("OK")) {
                //验证码发送成功
                QuerySendDetailsResponse querySendDetailsResponse = SmsUtil.querySendDetails(response.getBizId(), phoneNum);

                for (QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs()) {
                    String content = smsSendDetailDTO.getContent();
                    int index = content.indexOf("您的验证码为");
                    String captcha = content.substring(index + 6, index + 12);
                    String phone = smsSendDetailDTO.getPhoneNum();
                    Map<String, Object> data = MapUtils.of("captcha", captcha, "phone", phone);
                    dataList.add(data);
                }
            }

        } catch (ClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return new JSONResult(dataList);
    }

    @Override
    @Transactional
    public JSONResult saveUser(Map params) throws UserExistException {
        String emailCode = (String) params.get("emailCode");
        long userId = Id.next();
        params.put("userId", userId);
        if (emailCode != null) {
            //注册用户为邮箱注册
            params.put("emailPassword", AESUtil.aesEncode((String) params.get("emailPassword")));
            try {
                registerMapper.saveUserByEmail(params);
                //生成随机校验码
                String checkCode = generateCheckcode(emailCode);
                EmailUtil.sendEmail(checkCode, String.valueOf(userId), emailCode);
                //生成的校验码和userId放在redis中，以便之后激活时进行匹配，value=0表示未激活
                RedisClient.hset("ACTIVE_USER_" + userId + ":CHECK_CODE=" + checkCode, userId + "|" + checkCode, "0");
                RedisClient.expire("ACTIVE_USER_" + userId + ":CHECK_CODE=" + checkCode, 15 * 60 * 1000);
            } catch (Exception e) {
                throw new UserExistException();
            }
        } else {
            //注册用户为手机注册
            params.put("password", AESUtil.aesEncode((String) params.get("password")));
            try {
                registerMapper.saveUserByPhone(params);
            } catch (Exception e) {
                throw new UserExistException();
            }
        }
        registerMapper.saveUserRole(String.valueOf(userId), PRIMARY_USER_ROLE_ID);
        return new JSONResult("ok");
    }

    /**
     * 生成校验码，用户账号+6位随机码唯一标识符，为安全把他们加密发送
     *
     * @param emailCode
     * @return
     */
    private String generateCheckcode(String emailCode) {
        return MD5Util.md5(emailCode + ":" + createRandomVcode());
    }

    /**
     * @Return String 随机生成6位验证码
     * @Date 2018/4/10 11:34
     */
    private String createRandomVcode() {
        //验证码
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        return vcode;
    }

    @Override
    public JSONResult activate(String checkCode, String userId) throws UserExistException{
        String statusCode = RedisClient.hget("ACTIVE_USER_" + userId + ":CHECK_CODE=" + checkCode, userId + "|" + checkCode);
        if (statusCode == null){
            //激活失败
            return new JSONResult("failed");
        }
        if ("0".equals(statusCode)) {
            //激活成功
            registerMapper.updateActiveUser(userId);
            RedisClient.hset("ACTIVE_USER_" + userId + ":CHECK_CODE=" + checkCode, userId + "|" + checkCode, "1");
            return new JSONResult("success");
        }
        throw new UserExistException();
    }
}
