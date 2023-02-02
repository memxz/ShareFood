package iss.ad.team6.sharefood.Gson;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Gson_User
{
    @SerializedName("userId")
    private Long userId;

    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String pwd;

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

    @SerializedName("birth")
    private LocalDate birth;

    @SerializedName("role")
    private Role role;

    //add one more attribute
    @SerializedName("avatar")//头像
    private String avatar;
    public String getAvatar() {
        return avatar;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPwd() {
        return pwd;
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

    public LocalDate getBirth() {
        return birth;
    }

    public Role getRole() {
        return role;
    }

/*@SerializedName("id")//主键id
    private String mId;
    public String getId() {
        return mId;
    }

    @SerializedName("appKey")//用户创建应用的id
    private String appKey;
    public String getAppKey() {
        return appKey;
    }

    @SerializedName("username") //用户名
    private String username;
    public String getUsername() {
        return username;
    }

    @SerializedName("password")//密码
    private String password;
    public String getPassword() {
        return password;
    }

    @SerializedName("sex")//性别
    private int sex;
    public int getSex() {
        return sex;
    }

    @SerializedName("introduce")//个人介绍
    private String introduce;
    public String getIntroduce() {
        return introduce;
    }

    @SerializedName("avatar")//头像
    private String avatar;
    public String getAvatar() {
        return avatar;
    }

    @SerializedName("createTime")//创建时间
    private String createTime;
    public String getCreateTime() {
        return createTime;
    }

    @SerializedName("lastUpdateTime")//修改时间
    private String lastUpdateTime;
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }*/
}

