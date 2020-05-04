package com.rojay.wxshop.entity;

/**
 * 自定义的Http响应
 *
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年05月03日  22:48:51
 */
public class Response<T> {
    private String message;
    private T data;

    public static <T> Response<T> of(String message, T data) {
        return new Response<T>(message, data);
    }

    public static <T> Response<T> of(T data) {
        return new Response<T>(null, data);
    }

    public Response() {
    }

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
