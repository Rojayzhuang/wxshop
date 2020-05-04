package com.rojay.wxshop.service;

import com.rojay.wxshop.dao.UserDao;
import com.rojay.wxshop.generate.User;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Rojay
 * @version 1.0.0
 * @createTime 2020年03月29日  16:47:26
 */
@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUserIfNotExist(String tel) {
        User user = new User();
        user.setTel(tel);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        try {
            userDao.insertUser(user);
            //如果出现该异常说明重复加入用户信息
        } catch (PersistenceException e) {
            return userDao.getUserByTel(tel);
        }
        return user;
    }


    public User getUserByTel(String tel) {
        //根据电话返回用户，如果用户不存在，返回null
        return userDao.getUserByTel(tel);
    }
}
