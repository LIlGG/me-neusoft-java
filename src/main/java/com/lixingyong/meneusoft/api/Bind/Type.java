package com.lixingyong.meneusoft.api.Bind;

public enum Type {
    JWC("教务处"), LIBRARY("图书馆");
    private String name;
    Type(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
