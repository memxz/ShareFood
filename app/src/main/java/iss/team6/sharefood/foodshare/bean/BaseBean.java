package iss.team6.sharefood.foodshare.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
    public String success;
    public boolean isSuccess(){
        return "1".equals(success);
    }

}
