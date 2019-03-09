package com.example.vitabu;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class IntentJson {
    private UserAbstract curUser;
    private ArrayList<Object> objects = new ArrayList<Object>();


    public IntentJson(LocalUser curUser) {
        this.curUser = (UserAbstract) curUser;
    }

    public LocalUser getUser(){
        LocalUser usr = (LocalUser) curUser;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        usr.setFirebaseUser(firebaseUser);
        return usr;
    }

    public void setUser(LocalUser user){
        curUser = (UserAbstract) user;
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
