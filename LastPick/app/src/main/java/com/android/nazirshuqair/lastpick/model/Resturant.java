package com.android.nazirshuqair.lastpick.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

/**
 * Created by nazirshuqair on 12/04/14.
 */
public class Resturant implements Parcelable{

    private String name;
    private String formattedPhone;
    private String formattedAddress;
    private String url;
    private String currency;
    private double rating;
    private String text;
    private String firstName;
    private double lat;
    private double lng;
    private double distance;
    private String imgUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    public Resturant(){

    }

    public Resturant(String name, String formattedPhone, String formattedAddress, String currency,
                           String text, String firstName, String url, double rating, double lat, double lng, double distance,
                           String imgUrl) {
        this.name = name;
        this.formattedPhone = formattedPhone;
        this.formattedAddress = formattedAddress;
        this.currency = currency;
        this.text = text;
        this.firstName = firstName;
        this.url = url;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.imgUrl = imgUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.formattedPhone);
        dest.writeString(this.formattedAddress);
        dest.writeString(this.currency);
        dest.writeString(this.text);
        dest.writeString(this.firstName);
        dest.writeString(this.url);
        dest.writeDouble(this.rating);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.distance);
        dest.writeString(this.imgUrl);

    }

    public Resturant(Parcel source) {

        this.name = source.readString();
        this.formattedPhone = source.readString();
        this.formattedAddress = source.readString();
        this.currency = source.readString();
        this.text = source.readString();
        this.firstName = source.readString();
        this.url = source.readString();
        this.rating = source.readDouble();
        this.lat = source.readDouble();
        this.lng = source.readDouble();
        this.distance = source.readDouble();
        this.imgUrl = source.readString();

    }

    public static final Parcelable.Creator<Resturant> CREATOR = new Parcelable.Creator<Resturant>() {
        @Override
        public Resturant createFromParcel(Parcel source) {
            return new Resturant(source); // RECREATE VENUE GIVEN SOURCE
        }

        @Override
        public Resturant[] newArray(int size) {
            return new Resturant[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


}
