//package com.oceanier.client;
//
//import com.oceanier.annotations.RemoteInvoke;
//import com.oceanier.handler.param.Response;
//import com.oceanier.user.entity.User;
//import com.oceanier.user.remote.UserRemote;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = RemoteInvokeTest.class)
//@ComponentScan("com.oceanier")
//public class RemoteInvokeTest {
//
//    @RemoteInvoke
//    private UserRemote userRemote;
//
//    @Test
//    public void testSaveUser() {
//        User user = new User();
//        user.setId(100);
//        user.setName("张希希");
//        Response response = userRemote.saveUser(user);
//        System.out.println(response.toString());
//    }
//
//    @Test
//    public void testSaveUsers() {
//        List<User> users = new ArrayList<User>();
//        User user = new User();
//        user.setId(100);
//        user.setName("张希希");
//        users.add(user);
//        userRemote.saveUsers(users);
////        request.setCommand("com.oceanier.user.controller.UserController.saveUsers");
////        request.setContent(users);
////        Response response = TcpClient.send(request);
////        System.out.println(response.getResult());
//    }
//
//}
