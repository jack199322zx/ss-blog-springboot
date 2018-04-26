
package com.sstyle.server.service.impl;


import com.sstyle.server.domain.OpenOauth;
import com.sstyle.server.domain.User;
import com.sstyle.server.service.OpenOauthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方登录授权管理
 * @author ss on 2018/4/12.
 */
@Service
public class OpenOauthServiceImpl implements OpenOauthService {
    @Override
    public User getUserByOauthToken(String oauth_token) {
        return null;
    }

    @Override
    public OpenOauth getOauthByToken(String oauth_token) {
        return null;
    }

    @Override
    public OpenOauth getOauthByOauthUserId(String oauthUserId) {
        return null;
    }

    @Override
    public OpenOauth getOauthByUid(String userId) {
        return null;
    }

    @Override
    public boolean checkIsOriginalPassword(String userId) {
        return false;
    }

    @Override
    public void saveOauthToken(OpenOauth oauth) {

    }
}
