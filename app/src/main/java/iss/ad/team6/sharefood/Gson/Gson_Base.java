package iss.ad.team6.sharefood.Gson;

import com.google.gson.annotations.SerializedName;

public class Gson_Base
{
    @SerializedName("code")
    private int code;
    public int getCode(){return code;}

    @SerializedName("msg")
    private String msg;
    public String getMsg(){return msg;}

    @SerializedName("data")
    private String data;
    public String getData(){return data;}
}
