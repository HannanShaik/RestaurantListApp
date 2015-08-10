package com.hs.cardviewlist.model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hannan on 06/08/15.
 */
@Table(name = "restaurant")
public class Restaurant extends Model implements Serializable{

    @Column(name = "OutletId")
    public String OutletID;
    @Column(name = "OutletName")
    public String OutletName;
    @Column(name = "Address")
    public String Address;
    @Column(name = "Latitude")
    public String Latitude;
    @Column(name = "Longitude")
    public String Longitude;
    @Column(name = "Pincode")
    public String Pincode;
    @Column(name = "BrandName")
    public String BrandName;
    @Column(name = "NumCoupons")
    public String NumCoupons;
    @Column(name = "NeighbourhoodName")
    public String NeighbourhoodName;
    @Column(name = "PhoneNumber")
    public String PhoneNumber;
    @Column(name = "CityName")
    public String CityName;
    @Column(name = "Distance")
    public String Distance;
    @Column(name = "Categories")
    public String categoriesList;
    public List<Category> Categories;
    @Column(name = "LogoURL")
    public String LogoURL;
    @Column(name = "CoverURL")
    public String CoverURL;

    public Restaurant(){
        super();
    }

    public static List<Restaurant> getAll(){
        List<Restaurant> restaurants = new Select().from(Restaurant.class).execute();
        List<Restaurant> restaurantList = new ArrayList<>();
        for(Restaurant restaurant:restaurants){
            restaurant.Categories = restaurant.getCategories();
            restaurantList.add(restaurant);
        }
        return restaurantList;
    }

    public List<Category> getCategories(){
        Type type = new TypeToken<List<Category>>() {}.getType();
        return new Gson().fromJson(this.categoriesList,type);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "OutletID='" + OutletID + '\'' +
                ", OutletName='" + OutletName + '\'' +
                ", Address='" + Address + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Pincode='" + Pincode + '\'' +
                ", BrandName='" + BrandName + '\'' +
                ", NumCoupons='" + NumCoupons + '\'' +
                ", NeighbourhoodName='" + NeighbourhoodName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", CityName='" + CityName + '\'' +
                ", Distance='" + Distance + '\'' +
                ", categoriesList='" + categoriesList + '\'' +
                ", Categories=" + getCategories().toString() +
                ", LogoURL='" + LogoURL + '\'' +
                ", CoverURL='" + CoverURL + '\'' +
                '}';
    }


    public static void truncate() {
        List<Restaurant> restaurantList = Restaurant.getAll();
        ActiveAndroid.beginTransaction();
        try {
            for (Restaurant restaurant : restaurantList) {
                restaurant.delete();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

}
