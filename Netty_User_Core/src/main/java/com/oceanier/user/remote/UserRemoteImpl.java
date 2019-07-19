package com.oceanier.user.remote;

import com.oceanier.netty.annotations.Remote;

import com.oceanier.netty.util.ResponseUtil;

import com.oceanier.user.model.User;
import com.oceanier.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Remote
public class UserRemoteImpl implements UserRemote{

    @Resource
    UserService userService;

    public Object saveUser(User user) {
        userService.save(user);
        return ResponseUtil.createSuccessResult(user, "00000");
    }

    public Object saveUsers(List<User> users) {
        userService.saveList(users);
        return ResponseUtil.createSuccessResult(users, "00000");
    }

}
