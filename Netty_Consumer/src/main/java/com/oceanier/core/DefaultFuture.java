package com.oceanier.core;

import com.oceanier.handler.param.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {

    public static final ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture =
            new ConcurrentHashMap<Long, DefaultFuture>();

    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private Response response;
    //设置超时时间
    private long timeout = 2 * 60 * 1000;
    private long startTime = System.currentTimeMillis();

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStartTime() {
        return startTime;
    }

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
    }

    public Response get() {
        lock.lock();
        try {
            while (!done()) {
                condition.await(timeout, TimeUnit.MILLISECONDS);
                if ((System.currentTimeMillis() - startTime) > timeout) {
                    System.out.println("请求超时!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    public static void receive(Response response) {
        DefaultFuture df = allDefaultFuture.get(response.getId());
        if (df != null) {
            Lock lock = df.lock;
            lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                allDefaultFuture.remove(df);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean done() {
        if (this.response != null) {
            return true;
        }
        return false;
    }

    // 改成全局的线程池
    static class FutureThread extends Thread {
        @Override
        public void run() {
            Set<Long> ids = allDefaultFuture.keySet();
            for (Long id : ids) {
                DefaultFuture df = allDefaultFuture.get(id);
                if (df == null) {
                    allDefaultFuture.remove(id);
                } else {
                    // 假如链路超时
                    if (df.getTimeout() < (System.currentTimeMillis() - df.getStartTime())) {
                        Response resp = new Response();
                        resp.setId(id);
                        resp.setStatus("11111");
                        resp.setMsg("请求超时！");
                        receive(resp);
                    }
                }
            }
        }
    }

    static {
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }
}
