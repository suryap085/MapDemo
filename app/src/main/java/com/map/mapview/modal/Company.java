package com.map.mapview.modal;

public class Company {
    public String company_id;
    public String company_name;
    public String company_description;
    public String latitude;
    public String longitude;
    public String company_image_url;
    public String avg_rating;
    public int position;


    public Company(String company_id, String company_name, String company_description, String latitude, String longitude, String company_image_url, String avg_rating, int position) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_description = company_description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.company_image_url = company_image_url;
        this.avg_rating = avg_rating;
        this.position = position;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_description() {
        return company_description;
    }

    public void setCompany_description(String company_description) {
        this.company_description = company_description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCompany_image_url() {
        return company_image_url;
    }

    public void setCompany_image_url(String company_image_url) {
        this.company_image_url = company_image_url;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
