package com.oceanier.user.remote;

import com.oceanier.user.model.User;

import java.util.List;

// 面向接口编程
public interface UserRemote {

    Object saveUser(User user);

    Object saveUsers(List<User> users);
}
