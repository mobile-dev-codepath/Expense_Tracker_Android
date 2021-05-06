package com.codepath.expensetracker;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Date;

@ParseClassName("Receipt")
public class Receipt extends ParseObject{
    public static final String KEY_STORE_NAME = "storeName";
    public static final String KEY_STORE_TYPE = "storeType";
    public static final String KEY_TRANSACTION_COST = "transactionCost";
    public static final String KEY_TRANSACTION_DATE = "transactionDate";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_PROFILE_PICTURE = "profilePicture";
    public static final String KEY_JOIN_DATE = "joinDate";

    // empty constructor needed by parcel
    public Receipt() {}

    public String getStoreName(){
        return getString(KEY_STORE_NAME);
    }

    public void setStoreName(String storeName){
        put(KEY_STORE_NAME, storeName);
    }

    public String getStoreType(){
        return getString(KEY_STORE_TYPE);
    }

    public void setStoreType(String storeType){
        put(KEY_STORE_TYPE, storeType);
    }

    public String getTransactionCost(){
        return getString(KEY_TRANSACTION_COST);
    }

    public void setTransactionCost(String transactionCost){ put(KEY_TRANSACTION_COST, transactionCost); }

    public String getTransactionDate(){
        return getString(KEY_TRANSACTION_DATE);
    }

    public void setTransactionDate(String transactionDate){ put(KEY_TRANSACTION_DATE, transactionDate); }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getProfilePicture(){
        return getParseFile(KEY_PROFILE_PICTURE);
    }

    public void setProfilePicture(ParseFile parseFile){
        put(KEY_PROFILE_PICTURE, parseFile);
    }

    public String getJoinDate(){
        return getString(KEY_JOIN_DATE);
    }

    public void setJoinDate(String transactionDate){ put(KEY_JOIN_DATE, transactionDate); }

}
