package com.rojay.wxshop.service;

import com.rojay.wxshop.generate.User;

/**
 * 用户的信息
 *
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年04月27日  22:19:57
 */
public class UserContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser() {
        return currentUser.get();
    }

    /**
     * 用户过来后，将其放入上下文中，使得接下来能够很方便的调用
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static void clearCurrentUser() {
        currentUser.remove();
    }
}
