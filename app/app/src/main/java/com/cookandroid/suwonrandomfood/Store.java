package com.cookandroid.suwonrandomfood;

import java.util.HashMap;
import java.util.Map;

public class Store {
    int num;
    String name;
    String desc;
    String star;
    String img;
    boolean ec;
    boolean wc;

    public Store(){}

    public Store(int num, String name, String desc, String star, String img, boolean ec, boolean wc) {
        this.num = num;
        this.name = name;
        this.desc = desc;
        this.star = star;
        this.img = img;
        this.ec = ec;
        this.wc = wc;
    }

    public int getNum() {return num;}

    public void setNum(int num) {this.num = num;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getDesc() {return desc;}

    public void setDesc(String desc) {this.desc = desc;}

    public String getStar() {return star;}

    public void setStar(String star) {this.star = star;}

    public String getImg() {return img;}

    public void setImg(String img) {this.img = img;}

    public boolean isEc() {return ec;}

    public void setEc(boolean ec) {this.ec = ec;}

    public boolean isWc() {return wc;}

    public void setWc(boolean wc) {this.wc = wc;}

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("num", num);
        result.put("name", name);
        result.put("desc", desc);
        result.put("star", star);
        result.put("img", img);
        result.put("ec", ec);
        result.put("wc", wc);
        return result;
    }
}
