package com.oceanier.service;

import com.oceanier.annotations.RemoteInvoke;
import com.oceanier.user.model.User;
import com.oceanier.user.remote.UserRemote;
import org.springframework.stereotype.Service;

@Service
public class BasicService {

    @RemoteInvoke
    private UserRemote userRemote;

    public void saveUser() {
        User user = new User();
        user.setId(100);
        user.setName("张希");
        Object response = userRemote.saveUser(user);
        System.out.println(response.toString());
    }
}
