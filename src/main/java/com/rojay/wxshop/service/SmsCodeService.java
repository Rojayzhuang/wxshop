package com.rojay.wxshop.service;

/**
 * 登录验证码伪实现
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年03月29日  16:42:11
 */
public interface SmsCodeService {
    /**
     * 向一个指定手机号发送验证码，返回正确答案
     * @param tel 目标手机号
     * @return 正确答案
     */
    String sendSmsCode(String tel);

}
