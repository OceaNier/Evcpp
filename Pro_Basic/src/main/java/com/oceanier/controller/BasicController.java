package com.oceanier.controller;

import com.alibaba.fastjson.JSONObject;
import com.oceanier.annotations.RemoteInvoke;
import com.oceanier.service.BasicService;
import com.oceanier.user.model.User;
import com.oceanier.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BasicController.class)
@ComponentScan("com.oceanier")
public class BasicController {

    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(100);
        user.setName("张希");
        Object response = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(response));
    }
}