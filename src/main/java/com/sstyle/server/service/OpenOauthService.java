
package com.sstyle.server.service;


import com.sstyle.server.domain.OpenOauth;
import com.sstyle.server.domain.User;

/**
 * @author ss on 2018/4/12.
 */
public interface OpenOauthService {
    //通过 oauth_token 查询 user
    User getUserByOauthToken(String oauth_token);

    OpenOauth getOauthByToken(String oauth_token);
    
    OpenOauth getOauthByOauthUserId(String oauthUserId);

    OpenOauth getOauthByUid(String userId);

    boolean checkIsOriginalPassword(String userId);

    void saveOauthToken(OpenOauth oauth);

    void openQQchat() throws Exception;

}
