package com.dale.net.callback;

/**
 * Desc:服务器返回数据的异常
 **/
public class ABServiceError extends ABError {
    /**
     * 异常的原始字符串
     */
    private String originMessage;

    public ABServiceError(String message) {
        super(message);
    }

    public String getOriginMessage() {
        return originMessage;
    }

    public ABServiceError setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
        return this;
    }
}
