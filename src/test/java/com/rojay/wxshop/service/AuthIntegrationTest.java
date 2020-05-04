package com.rojay.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kevinsawicki.http.HttpRequest;
import com.rojay.wxshop.WxshopApplication;
import com.rojay.wxshop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.rojay.wxshop.service.TelVerificationServiceTest.VALID_PARAMETER;
import static java.net.HttpURLConnection.*;

/**
 * 登录模块的集成化测试
 *
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年04月26日  23:50:20
 */
@ExtendWith(SpringExtension.class)
//设置随机端口
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//使用的数据库，应该使用测试数据库与生产的分离
//@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
//@TestPropertySource(locations = "classpath:application.yml" )
public class AuthIntegrationTest extends AbstractIntegrationTest {

    /**
     * 登录模块集成测式
     *
     * @throws JsonProcessingException
     */
    @Test
    public void loginLogoutTest() throws JsonProcessingException {
        /*//第一步：最开始默认情况下，访问/api/status 处于未登录状态
        //首次查询登录状态，应返回未登录状态
        String statusResponse = doHttpRequest("/api/status", true, null, null).body;
        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());

        //第二步：发送验证码
        int responseCode = doHttpRequest("/api/code", false, VALID_PARAMETER, null).code;
        //返回成功验证码
        Assertions.assertEquals(HTTP_OK, responseCode);

        //第三步：带着验证码进行登录，得到Cookie
        //带着验证码进行登录
        Map<String, List<String>> responseHeaders = doHttpRequest("/api/login", false, VALID_PARAMETER_CODE, null).headers;
        List<String> setCookie = responseHeaders.get("Set-Cookie");
        //取出JSESSIONID
        String sessionId = getSessionIdFromSetCookie(setCookie.stream().filter(cookie -> cookie.contains("JSESSIONID"))
                .findFirst()
                .get());*/
        //上述代码重构为
        String sessionId = loginAndGetCookie().cookie;
        //第四步：带着Cookie访问 /api/status，此时应该处于登录状态
        String statusResponse = doHttpRequest("/api/status", "GET", null, sessionId).body;
        LoginResponse response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertTrue(response.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), response.getUser().getTel());

        //第五步：执行注销登录操作，调用注销接口 /api/logout
        //注意，注销登录也需要带cookie；  .header("Cookie", sessionId)
        doHttpRequest("/api/logout", "POST", null, sessionId);

        //再次带着Cookie访问/api/status，此时恢复成未登录状态
        //恢复成未登录状态
        statusResponse = doHttpRequest("/api/status", "GET", null, sessionId).body;

        response = objectMapper.readValue(statusResponse, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());
    }

    @Test
    public void returnHttpOKWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(VALID_PARAMETER))
                .code();
        Assertions.assertEquals(HTTP_OK, responseCode);
    }

    @Test
    public void returnHttpBadRequestWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.EMPTY_TEL))
                .code();
        Assertions.assertEquals(HTTP_BAD_REQUEST, responseCode);
    }

    @Test
    public void returnUnauthorizedIfNotLogin() {
        int responseCode = HttpRequest.post(getUrl("/api/any"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .code();
        Assertions.assertEquals(HTTP_UNAUTHORIZED, responseCode);
    }

}

