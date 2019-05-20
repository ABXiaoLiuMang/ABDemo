package com.net;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class LoginBean {

    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vd3d3LnNvYXJnOTk5LmNvbS9hcGkvdjEvYXV0aHRva2VuIiwiaWF0IjoxNTU4MzM1ODU0LCJleHAiOjE1NTgzNjQ2NTQsIm5iZiI6MTU1ODMzNTg1NCwianRpIjoiWjA4WXlCMnRUQ01hZDBLQyIsInN1YiI6NTU5ODd9.62fSdsEUHAHEpNd8yNU3zE1G7EbfQZEQ5S7I-widNJ0
     * userinfo : {"id":55987,"phone":"","username":"div123","avatar":"https://d2al1bcutpfcks.cloudfront.net/S3_image_2018_11_27_2018112718572397256.png"}
     */

    private String token;
    private UserinfoBean userinfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * id : 55987
         * phone :
         * username : div123
         * avatar : https://d2al1bcutpfcks.cloudfront.net/S3_image_2018_11_27_2018112718572397256.png
         */

        private int id;
        private String phone;
        private String username;
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "token='" + token + '\'' +
                ", userinfo=" + userinfo +
                '}';
    }
}
