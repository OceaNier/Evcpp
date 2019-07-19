package com.oceanier.handler.param;

public class Response {
    private Long id;
    private Object result;
    private String status; // 00000表示成功，其他失败
    private String msg; // 失败原因

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", result=" + result +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
