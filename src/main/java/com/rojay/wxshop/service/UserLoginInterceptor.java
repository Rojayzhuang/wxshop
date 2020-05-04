package com.rojay.wxshop.service;

import com.rojay.wxshop.generate.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录的拦截器
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年04月27日  22:37:09
 */

public class UserLoginInterceptor implements HandlerInterceptor {
    private UserService userService;

    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    /**
     * 在请求发送前做的事情
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到电话号码
        Object tel = SecurityUtils.getSubject().getPrincipal();
        if (tel != null) {
            //说明已经登录了
            //此时将用户信息保存在ThreadLocal（线程的局部变量）中
            //下面这行代码需要对getUserByTel方法进行Optional改写
            //userService.getUserByTel(tel.toString()).ifPresent(UserContext::setCurrentUser);
            User user = userService.getUserByTel(tel.toString());
            //已经登录，将其设置到
            UserContext.setCurrentUser(user);
        }
        return true;
    }

    /**
     * 在请求发送后做的事情
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //当请求结束，将上下文清空，十分重要！！！因为线程是会被复用的
        //当请求结束，将上下文清空，十分重要！！！因为线程是会被复用的
        //当请求结束，将上下文清空，十分重要！！！因为线程是会被复用的
        //如果在线程1中保存了用户A的信息，且没有清理的话
        //下次线程1再用来被用来处理憋的请求的时候，就会出现“串号”的情况
        UserContext.setCurrentUser(null);
    }
}

