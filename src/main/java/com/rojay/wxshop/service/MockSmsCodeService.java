package com.rojay.wxshop.service;

import org.springframework.stereotype.Service;

/**
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年03月29日  16:43:36
 */
@Service
public class MockSmsCodeService implements SmsCodeService {

    @Override
    public String sendSmsCode(String tel) {
        return "000000";
    }
}
