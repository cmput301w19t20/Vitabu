package com.example.vitabu;


import com.google.gson.Gson;

import java.util.ArrayList;

public class IntentJson {
    private User curUser;
    private ArrayList<Object> objects;
    public IntentJson(User curUser) {
        this.curUser = curUser;
    }
    public User getUser(){
        return curUser;
    }
    public void setUser(User user){
        curUser = user;
    }
    public void addObject(Object o){
        objects.add(o);
    }
    public void removeObject(int i){
        objects.remove(i);
    }
    public Object getObject(int i){
        return objects.get(i);
    }
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
