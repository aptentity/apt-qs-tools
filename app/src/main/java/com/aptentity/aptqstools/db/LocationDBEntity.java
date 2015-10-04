package com.aptentity.aptqstools.db;

import org.litepal.crud.DataSupport;

public class LocationDBEntity extends DataSupport {
    private long timestamp=0;
    private double Latitude;
    private double Longitude;
    private String time;
    private float Radius;
    private String Address;
    private String Province;
    private String Street;
    private String StreetNumber;
    private String City;
    private String CityCode;
    private String District;
    public LocationDBEntity() {

    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getLatitude() {
        return Latitude;
    }
    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
    public double getLongitude() {
        return Longitude;
    }
    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public float getRadius() {
        return Radius;
    }
    public void setRadius(float radius) {
        Radius = radius;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getProvince() {
        return Province;
    }
    public void setProvince(String province) {
        Province = province;
    }
    public String getStreet() {
        return Street;
    }
    public void setStreet(String street) {
        Street = street;
    }
    public String getStreetNumber() {
        return StreetNumber;
    }
    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
    }
    public String getCity() {
        return City;
    }
    public void setCity(String city) {
        City = city;
    }
    public String getCityCode() {
        return CityCode;
    }
    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }
    public String getDistrict() {
        return District;
    }
    public void setDistrict(String district) {
        District = district;
    }
    
    
}
