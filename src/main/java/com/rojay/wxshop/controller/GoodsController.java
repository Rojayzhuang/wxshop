package com.rojay.wxshop.controller;

import com.rojay.wxshop.entity.PageResponse;
import com.rojay.wxshop.entity.Response;
import com.rojay.wxshop.generate.Goods;
import com.rojay.wxshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 商品模块
 *
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年05月01日  22:55:09
 */
@RestController
@RequestMapping("/api")
public class GoodsController {
    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // @formatter:off

    /**
     * @api {get} /goods 获取所有商品
     * @apiName GetGoods
     * @apiGroup 商品
     * @apiHeader {String} Accept application/json
     * @apiParam {Number} pageNum 页数，从1开始
     * @apiParam {Number} pageSize 每页显示的数量
     * @apiParam {Number} [shopId] 店铺ID，若传递，则只显示该店铺中的商品
     * @apiSuccess {Number} pageNum 页数，从1开始
     * @apiSuccess {Number} pageSize 每页显示的数量
     * @apiSuccess {Number} totalPage 共有多少页
     * @apiSuccess {Goods} data 商品列表
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "pageNum": 1,
     * "pageSize": 10,
     * "totalPage": 5,
     * "data": [
     * {
     * "id": 12345,
     * "name": "肥皂",
     * "description": "纯天然无污染肥皂",
     * "details": "这是一块好肥皂",
     * "imgUrl": "https://img.url",
     * "price": 500,
     * "stock": 10,
     * "shopId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * },
     * {
     * ...
     * }
     * ]
     * }
     * @apiError 401 Unauthorized 若用户未登录
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    /**
     * @param pageNum  页数
     * @param pageSize 页
     * @param shopId   商品id
     * @return 分页
     */
    // @formatter:on
    @GetMapping("/goods")
    public @ResponseBody
    PageResponse<Goods> getGoods(@RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam(value = "shopId", required = false) Integer shopId) {
        return goodsService.getGoods(pageNum, pageSize, shopId);
    }


    // @formatter:off

    /**
     * @api {post} /goods 创建商品
     * @apiName CreateGoods
     * @apiGroup 商品
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParamExample {json} Request-Example:
     * {
     * "name": "肥皂",
     * "description": "纯天然无污染肥皂",
     * "details": "这是一块好肥皂",
     * "imgUrl": "https://img.url",
     * "price": 500,
     * "stock": 10,
     * "shopId": 12345
     * }
     * @apiSuccess {Goods} data 创建的商品
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 201 Created
     * {
     * "data": {
     * "id": 12345,
     * "name": "肥皂",
     * "description": "纯天然无污染肥皂",
     * "details": "这是一块好肥皂",
     * "imgUrl": "https://img.url",
     * "price": 500,
     * "stock": 10,
     * "shopId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * }
     * }
     * @apiError 400 Bad Request 若用户的请求中包含错误
     * @apiError 401 Unauthorized 若用户未登录
     * @apiError 403 Forbidden 若用户尝试创建非自己管理店铺的商品
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    // @formatter:on

    /**
     * @param goods    商品
     * @param response 响应体
     * @return 接口文档定义的返回值
     */
    @PostMapping("/goods")
    public Response<Goods> createdGoods(@RequestBody Goods goods, HttpServletResponse response) {
        //数据清洗
        clean(goods);
        //设置创建成功的返回值
        response.setStatus(HttpServletResponse.SC_CREATED);
        try {
            //向前端返回创建的商品信息
            return Response.of(goodsService.createGoods(goods));
        } catch (GoodsService.NotAuthorizedForShopException e) {
            //创建不成功抛出指定异常403并返回指定message
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Response.of(e.getMessage(), null);
        }
    }

    /**
     * 数据清洗，将前端输入的数据规范
     *
     * @param goods
     */
    private void clean(Goods goods) {
        goods.setId(null);
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
    }


    // @formatter:off

    /**
     * @api {patch} /goods/:id 更新商品
     * @apiName UpdateGoods
     * @apiGroup 商品
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParam {Number} id 商品ID
     * @apiParamExample {json} Request-Example:
     * {
     * "name": "肥皂",
     * "description": "纯天然无污染肥皂",
     * "details": "这是一块好肥皂",
     * "imgUrl": "https://img.url",
     * "price": 500,
     * "stock": 10
     * }
     * @apiSuccess {Goods} data 更新后的商品
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data": {
     * "id": 12345,
     * "name": "肥皂",
     * "description": "纯天然无污染肥皂",
     * "details": "这是一块好肥皂",
     * "imgUrl": "https://img.url",
     * "price": 500,
     * "stock": 10,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * }
     * }
     * @apiError 400 Bad Request 若用户的请求中包含错误
     * @apiError 401 Unauthorized 若用户未登录
     * @apiError 403 Forbidden 若用户尝试修改非自己管理店铺的商品
     * @apiError 404 Not Found 若商品未找到
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    // @formatter:on

    /**
     *
     * @param goods
     * @param response
     * @return 更新后的结果
     */
    public Response<Goods> updateGoods(Goods goods, HttpServletResponse response) {
        try {
            return Response.of(goodsService.updateGoods(goods));
        } catch (GoodsService.NotAuthorizedForShopException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Response.of(e.getMessage(), null);
        } catch (GoodsService.ResourceNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Response.of(e.getMessage(), null);
        }
    }


    // @formatter:off

    /**
     * @api {delete} /goods/:id 删除商品
     * @apiName DeleteGoods
     * @apiGroup 商品
     * @apiHeader {String} Accept application/json
     * @apiParam {Number} id 商品ID
     * @apiSuccess {Goods} data 被删除的商品
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 204 No Content
     * {
     * "data": {
     * "id": 12345,
     * "name": "肥皂",
     * "description": "纯天然无污染肥皂",
     * "details": "这是一块好肥皂",
     * "imgUrl": "https://img.url",
     * "price": 500,
     * "stock": 10,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * }
     * }
     * @apiError 400 Bad Request 若用户的请求中包含错误
     * @apiError 401 Unauthorized 若用户未登录
     * @apiError 403 Forbidden 若用户尝试删除非自己管理店铺的商品
     * @apiError 404 Not Found 若商品未找到
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    // @formatter:on

    /**
     * @param goodsId  商品id
     * @param response 返回值
     * @return 不同情况的返回信息
     */
    @DeleteMapping("/goods/{id}")
    public Response<Goods> deleteGoods(@PathVariable("id") Long goodsId, HttpServletResponse response) {
        try {
            //正常删除时
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return Response.of(goodsService.deleteGoodsById(goodsId));
        } catch (GoodsService.NotAuthorizedForShopException e) {
            //没有权限时
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return Response.of(e.getMessage(), null);
        } catch (GoodsService.ResourceNotFoundException e) {
            //未找到商品时
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Response.of(e.getMessage(), null);
        }
    }
}
