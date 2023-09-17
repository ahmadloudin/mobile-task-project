package com.example.kickmyb;

import android.widget.TextView;

public final class Singleton {
String username;
private static final Singleton instance = new Singleton();

private Singleton(){}

    public static Singleton getInstance(){
    return instance;
    }

    public void setText(String username){
    this.username = username;
    }
    public String getText(){
    return username;
    }
}
