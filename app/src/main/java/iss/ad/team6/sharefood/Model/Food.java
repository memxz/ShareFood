package iss.ad.team6.sharefood.Model;

import java.io.Serializable;

public class Food implements Serializable {

    String id;
    String name;
    String description;
    String genres;
    String img;

    public Food (String id, String name, String description, String genres,String img) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genres = genres;
        this.img=img;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

    public String getImg() {
        return img;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
