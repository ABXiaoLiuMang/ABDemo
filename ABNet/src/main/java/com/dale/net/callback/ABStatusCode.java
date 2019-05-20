package com.dale.net.callback;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class ABStatusCode {
    /**
     * 接口返回请求状态码
     */
    public static final class ServiceStatus {

        /**
         * 请求正常返回
         */
        public static final int REQUEST_SUCCESS_200 = 200;

        /**
         * 新资源已被创建
         */
        public static final int REQUEST_RESOURCE_CREATED = 201;

        /**
         * 资源删除成功
         */
        public static final int REQUEST_DELETE_SUCCESS = 204;

        /**
         *没有变化，客户端可以使用缓存数据
         */
        public static final int REQUEST_NOT_NEW_DATA = 304;

        /**
         *Bad Request – 调用不合法，确切的错误应该在error payload中描述，例如：“JSON 不合法 ”
         */
        public static final int REQUEST_BAD_REQUEST = 400;

        /**
         *未认证，调用需要用户通过认证
         */
        public static final int REQUEST_NOT_AUTH = 401;

        /**
         *不允许的，服务端正常解析和请求，但是调用被回绝或者不被允许
         */
        public static final int REQUEST_NOT_ALLOW = 403;

        /**
         *未找到，指定的资源不存在
         */
        public static final int REQUEST_NOT_FOUND = 404;

        /**
         *不可指定的请求体 – 只有服务器不能处理实体时使用，比如图像不能被格式化，或者重要字段丢失。
         */
        public static final int REQUEST_CANNOT_REQUEST_BODY = 422;

        /**
         *标准服务端错误，API开发人员应该尽量避开这种错误。
         */
        public static final int REQUEST_SERVICE_ERROR = 500;

    }

    /**
     * http网络请求返回状态码
     */
    public static final class HttpStatus {

        /**
         * 数据解析异常
         */
        public static final int HTTP_HANDLER_ERROR = 11000;

        /**
         * 网络异常
         */
        public static final int HTTP_NET_ERROR = 11001;

        /**
         * 请求超时
         */
        public static final int HTTP_TIME_OUT = 11002;

        /**
         * 获取数据失败
         */
        public static final int REQUEST_DATA_ERROR = 11003;

        /**
         * 非法baseUrl
         */
        public static final int UNKNOW_HOST = 11004;

        /**
         * 请求参数异常
         */
        public static final int REQUEST_PARAMS_ERROR = 11005;

        /**
         * 图形验证码填写错误
         */
        public static final int WRITE_IMAGE_ERROR = 11006;

        /**
         * 程序未知异常
         */
        public static final int PROJECT_ERROR = 11007;

        /**
         * 正常请求成功
         */
        public static final int HTTP_NET_200 = 200;
        /**
         * 请求未经授权
         */
        public static final int HTTP_NET_401 = 401;
        /**
         * 禁止访问，服务器收到请求，但是拒绝提供服务
         */
        public static final int HTTP_NET_403 = 403;

        /**
         * 服务器不能正确执行一个正确的请求
         */
        public static final int HTTP_NET_500 = 500;
        public static final int HTTP_NET_501 = 501;
        public static final int HTTP_NET_502 = 502;
        public static final int HTTP_NET_504 = 504;
        public static final int HTTP_NET_505 = 505;

        /**
         * 由于超载或停机维护，服务器目前无法使用，一段时间后可能恢复正常
         */
        public static final int HTTP_NET_503 = 503;


    }

}
