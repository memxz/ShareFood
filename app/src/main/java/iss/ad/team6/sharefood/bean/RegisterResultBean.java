package iss.ad.team6.sharefood.bean;


public class RegisterResultBean extends BaseBean {
    public String userid;
    public String collected;

    public boolean isLoginFailed() {
        return "0".equals(userid);
    }


    public boolean isCollect() {
        return "1".equals(collected);
    }
}
