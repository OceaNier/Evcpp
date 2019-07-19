package com.oceanier.proxy;

import com.oceanier.annotations.RemoteInvoke;
import com.oceanier.core.ClientRequest;
import com.oceanier.core.TcpClient;
import com.oceanier.handler.param.Response;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokeProxy implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {

        // 动态代理
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f : fields) {
            // 判断是否是RemoteInvoke注解
            if (f.isAnnotationPresent(RemoteInvoke.class)) {
                f.setAccessible(true);

                final Map<Method, Class> methodClassMap = new HashMap<Method, Class>();
                putMethodClass(methodClassMap, f);
                Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[]{f.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        // 采用netty客户端调用服务器
                        ClientRequest request = new ClientRequest();
                        request.setCommand(methodClassMap.get(method).getName() + "." + method.getName());
                        request.setContent(args[0]);

                        Response resp = TcpClient.send(request);
                        return resp;
                    }
                });

                try {
                    f.set(o, enhancer.create());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return null;
    }

    /**
     * 将属性的所有方法和属性接口类型放入一个map
     * @param methodClassMap
     * @param f
     */
    private void putMethodClass(Map<Method, Class> methodClassMap, Field f) {
        Method[] methods = f.getType().getDeclaredMethods();
        for (Method m : methods) {
            methodClassMap.put(m, f.getType());
        }
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return null;
    }
}
