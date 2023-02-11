package iss.ad.team6.sharefood.Model;

import java.io.Serializable;

public class Food implements Serializable {


    long foodId;
    String title;
    String description;
    String isHalal;
    String img;
    int listDays;
    float longitude;
    float latitude;
    String foodLocation;
    Object publisher;

    public Food (long foodId, String title, String description, String isHalal, String img, int listDays, float longitude, float latitude, String foodLocation, Object publisher) {
        this.foodId = foodId;
        this.title = title;
        this.description = description;
        this.isHalal = isHalal;
        this.img=img;
        this.listDays = listDays;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foodLocation = foodLocation;
    }

    public long getFoodId() {
        return foodId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIsHalal() {
        return isHalal;
    }

    public String getImg() {
        return img;
    }

    public int getListDays() { return listDays; }

    public String getFoodLocation() { return foodLocation; }

    public float getLongitude() { return longitude; }

    public float getLatitude() { return latitude; }

    public Object getPublisher() { return publisher; }


    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsHalal(String isHalal) {
        this.isHalal = isHalal;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setListDays(int listDays) { this.listDays = listDays; }

    public void setFoodLocation(String foodLocation) { this.foodLocation = foodLocation; }

    public void setLongitude(float longitude) { this.longitude = longitude; }

    public void setLatitude(float latitude) { this.latitude = latitude; }

    public void setPublisher(Object publisher) { this.publisher = publisher; }
}
