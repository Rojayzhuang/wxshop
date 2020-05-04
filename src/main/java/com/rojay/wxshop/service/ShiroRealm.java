package com.rojay.wxshop.service;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年04月21日  23:36:15
 * 设定在某个区域内怎么进行验全
 * 这是一个验全的抽象类
 */
@Service
public class ShiroRealm extends AuthorizingRealm {

    private final VerificationCodeCheckService verificationCodeCheckService;

    @Autowired
    public ShiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        this.verificationCodeCheckService = verificationCodeCheckService;
        /**
         * 比对用户提供的身份信息是否正确
         * @param token 用户提供的身份信息
         * @param info 用户真正的身份信息
         * @return
         */
        /*this.setCredentialsMatcher(new CredentialsMatcher() {

            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                return token.getCredentials().equals(info.getCredentials());
            }
        });*/
        //将上述代码转化为lambda表达式
        this.setCredentialsMatcher((token, info) -> new String((char[]) token.getCredentials()).equals(info.getCredentials()));
    }

    /**
     * 查看是否有权限访问该资源
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 对登录信息进行验证，如：验证码与手机号是否匹配
     * 由shiro对用户提供的信息进行比对，成功则实现登录
     *
     * @param token 外界用户提供的token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String tel = (String) token.getPrincipal();
        String correctCode = verificationCodeCheckService.getCorrectCode(tel);
        /**
         * AuthenticationInfo将告诉shiro该用户真正的身份是什么
         * 然后shiro会将返回的身份与生成的身份相比较
         */
        return new SimpleAuthenticationInfo(tel, correctCode, getName());
    }
}
