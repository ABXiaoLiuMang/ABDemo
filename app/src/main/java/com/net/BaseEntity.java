package com.net;

public class BaseEntity<T> {
    private int status;
    private T data;
    private String code = "0";
    private String msg;


    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getStatus_code() {
        return code;
    }

    public boolean isOk() {
        return status == 1;
    }

    public int getStatusCodeInt() {
        try {
            return Integer.parseInt(code);
        } catch (Exception e) {
            return 0;
        }

    }

    public String getMessage() {
        return msg;
    }


}

