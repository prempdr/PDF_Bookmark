package app.prempdr.pdfbookmark.adapter;

import java.io.Serializable;

public class itemModel implements Serializable {
    private int image;
    private String id, title, sub_title, url, fav_status;

    public itemModel(int image, String id, String title, String sub_title, String url, String fav_status) {
        this.image = image;
        this.id = id;
        this.title = title;
        this.sub_title = sub_title;
        this.url = url;
        this.fav_status = fav_status;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
    }
}
