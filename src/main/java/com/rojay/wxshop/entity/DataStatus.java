package com.rojay.wxshop.entity;

/**
 * 数据库内数据的状态
 * 主要用于货物状态，因为对商品的操作为逻辑操作
 * 如要删除一个商品，将其状态改为删除即可
 * 避免数据丢失或误操作造成的损失
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年05月03日  23:48:44
 */
public enum  DataStatus {
    OK(),
    DELETED();

    public String getName() {
        return name().toLowerCase();
    }
}
