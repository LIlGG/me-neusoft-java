package com.lixingyong.meneusoft.api.Bind;

public enum Type {
    JWC("jwc"), LIBRARY("library"),Bind("bind");
    private String name;
    Type(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
