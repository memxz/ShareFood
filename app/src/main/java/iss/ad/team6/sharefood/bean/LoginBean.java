package iss.ad.team6.sharefood.bean;

import com.google.gson.annotations.SerializedName;

public class LoginBean {
    @SerializedName("userId")
    private Long userId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("salary")
    private Double salary;

    @SerializedName("level")
    private String level;

    @SerializedName("points")
    private int points;

    @SerializedName("role")
    private Role role;

    //add one more attribute
//    @SerializedName("avatar")//头像
//    private String avatar;
//    public String getAvatar() {
//        return avatar;
//    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Double getSalary() {
        return salary;
    }

    public String getLevel() {
        return level;
    }

    public int getPoints() {
        return points;
    }

    public Role getRole() {
        return role;
    }
}