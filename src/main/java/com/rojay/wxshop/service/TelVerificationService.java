package com.rojay.wxshop.service;

import com.rojay.wxshop.controller.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 *
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年04月26日  17:12:59
 */
@Service
public class TelVerificationService {
    private static Pattern TEL_PATTERN = Pattern.compile("1\\d{10}");
    /**
     * 验证输入的参数是否合法：
     * tel必须存在且为合法的中国大陆手机号
     * @param param 输入的参数
     * @return true 合法，否则返回false
     */
    public boolean verifyTelParameter(AuthController.TelAndCode param) {
        if (param == null) {
            return false;
        } else if (param.getTel() == null) {
            return false;
        } else {
            //验证手机号是否合法的正则表达式
            return TEL_PATTERN.matcher(param.getTel()).find();
        }
    }

}
