package com.rojay.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rojay.wxshop.WxshopApplication;
import com.rojay.wxshop.entity.Response;
import com.rojay.wxshop.generate.Goods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 商品模块的集成化测试
 * 有数据库模块
 *
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年05月04日  00:19:51
 */
@ExtendWith(SpringExtension.class)
//设置随机端口
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//使用的数据库，应该使用测试数据库与生产的分离
//@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
@ActiveProfiles("test")
public class GoodsIntegrationTest extends AbstractIntegrationTest {

    public void testCreateGoods() throws JsonProcessingException {
        UserLoginResponse loginResponse = loginAndGetCookie();

        Goods goods = new Goods();
        goods.setName("肥皂");
        goods.setDescription("纯天然无污染肥皂");
        goods.setDetails("这是一块好肥皂");
        goods.setImgUrl("http://img.url");
        goods.setPrice(1000L);
        goods.setStock(10);
        goods.setShopId(123L);

        HttpResponse response = doHttpRequest(
                "/api/goods",
                "POST",
                goods,
                loginResponse.cookie);

        Response<Goods> responseData = objectMapper.readValue(response.body, new TypeReference<Response<Goods>>() {
        });

        Assertions.assertEquals(SC_CREATED, response.code);
        Assertions.assertEquals("肥皂", responseData.getData().getName());

    }

    @Test
    public void return404IfGoodsToDeleteNotExist() throws JsonProcessingException {
        String cookie = loginAndGetCookie().cookie;
        HttpResponse response = doHttpRequest(
                "/api/goods/12345678",
                "DELETE",
                null,
                cookie);
        assertEquals(SC_NOT_FOUND, response.code);
    }
}
