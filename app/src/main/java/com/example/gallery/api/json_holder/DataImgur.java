package com.example.gallery.api.json_holder;

public class DataImgur {
    /*
    "data": {
        "id": "73PSlOg",
        "title": null,
        "description": null,
        "datetime": 1699244217,
        "type": "image/png",
        "animated": false,
        "width": 805,
        "height": 929,
        "size": 44110,
        "views": 0,
        "bandwidth": 0,
        "vote": null,
        "favorite": false,
        "nsfw": null,
        "section": null,
        "account_url": null,
        "account_id": 175840357,
        "is_ad": false,
        "in_most_viral": false,
        "has_sound": false,
        "tags": [],
        "ad_type": 0,
        "ad_url": "",
        "edited": "0",
        "in_gallery": false,
        "deletehash": "1Hr8FqAbhwmN4rC",
        "name": "",
        "link": "https://i.imgur.com/73PSlOg.png"
    },
    "success": true,
    "status": 200
}
     */
    private String id;
    private String title;
    private String description;
    private String datetime;
    private String type;
    private boolean animated;
    private int width;
    private int height;
    private int size;
    private int views;
    private int bandwidth;
    private String vote;
    private boolean favorite;
    private String nsfw;
    private String section;
    private String account_url;
    private int account_id;
    private boolean is_ad;
    private boolean in_most_viral;
    private boolean has_sound;
    private String[] tags;
    private int ad_type;
    private String ad_url;
    private String edited;
    private boolean in_gallery;
    private String deletehash;
    private String name;
    private String link;

    public DataImgur(String id, String title, String description, String datetime, String type, boolean animated, int width, int height, int size, int views, int bandwidth, String vote, boolean favorite, String nsfw, String section, String account_url, int account_id, boolean is_ad, boolean in_most_viral, boolean has_sound, String[] tags, int ad_type, String ad_url, String edited, boolean in_gallery, String deletehash, String name, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.type = type;
        this.animated = animated;
        this.width = width;
        this.height = height;
        this.size = size;
        this.views = views;
        this.bandwidth = bandwidth;
        this.vote = vote;
        this.favorite = favorite;
        this.nsfw = nsfw;
        this.section = section;
        this.account_url = account_url;
        this.account_id = account_id;
        this.is_ad = is_ad;
        this.in_most_viral = in_most_viral;
        this.has_sound = has_sound;
        this.tags = tags;
        this.ad_type = ad_type;
        this.ad_url = ad_url;
        this.edited = edited;
        this.in_gallery = in_gallery;
        this.deletehash = deletehash;
        this.name = name;
        this.link = link;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getNsfw() {
        return nsfw;
    }

    public void setNsfw(String nsfw) {
        this.nsfw = nsfw;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public boolean isIs_ad() {
        return is_ad;
    }

    public void setIs_ad(boolean is_ad) {
        this.is_ad = is_ad;
    }

    public boolean isIn_most_viral() {
        return in_most_viral;
    }

    public void setIn_most_viral(boolean in_most_viral) {
        this.in_most_viral = in_most_viral;
    }

    public boolean isHas_sound() {
        return has_sound;
    }

    public void setHas_sound(boolean has_sound) {
        this.has_sound = has_sound;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public boolean isIn_gallery() {
        return in_gallery;
    }

    public void setIn_gallery(boolean in_gallery) {
        this.in_gallery = in_gallery;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
