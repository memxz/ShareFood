package iss.ad.team6.sharefood.bean;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class FoodBean {
    @SerializedName("foodId")
    private Long foodId;

    @SerializedName("title")
    private String title;


    @SerializedName("description")
    private String description;

    @SerializedName("listDays")
    private double listDays;

    @SerializedName("isPendingPickup")
    private boolean isPendingPickup;

    @SerializedName("isCollected")
    private boolean isCollected;

    @SerializedName("isHalal")
    private FoodType foodType;

    @SerializedName("isListed")
    private boolean isListed;

//    @SerializedName("foodLocation")
//    private Location foodLocation;

    @SerializedName("img")
    private String imgUrl;

    @SerializedName("publisher")
    private LoginBean publisher;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("requestId")
    private Long requestId;

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getListDays() {
        return listDays;
    }

    public void setListDays(double listDays) {
        this.listDays = listDays;
    }

    public boolean isPendingPickup() {
        return isPendingPickup;
    }

    public void setPendingPickup(boolean pendingPickup) {
        isPendingPickup = pendingPickup;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public boolean isListed() {
        return isListed;
    }

    public void setListed(boolean listed) {
        isListed = listed;
    }

//    public Location getFoodLocation() {
//        return foodLocation;
//    }
//
//    public void setFoodLocation(Location foodLocation) {
//        this.foodLocation = foodLocation;
//    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public LoginBean getPublisher() {
        return publisher;
    }

    public void setPublisher(LoginBean publisher) {
        this.publisher = publisher;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "FoodBean{" +
                "foodId=" + foodId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", listDays=" + listDays +
                ", isPendingPickup=" + isPendingPickup +
                ", isCollected=" + isCollected +
                ", foodType=" + foodType +
                ", isListed=" + isListed +
                '}';
    }
}
