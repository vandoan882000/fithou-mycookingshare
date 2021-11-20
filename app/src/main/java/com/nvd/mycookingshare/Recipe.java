package com.nvd.mycookingshare;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    protected String id;
    protected String ten;
    protected String img;
    protected ArrayList<String> chuanbi;
    protected String loai;
    protected List<MyStep> mystep;
    protected String userid;
    public Recipe() {
    }

    public Recipe(String id, String ten, String img, ArrayList<String> chuanbi, String loai, List<MyStep> mystep, String userid) {
        this.id = id;
        this.ten = ten;
        this.img = img;
        this.chuanbi = chuanbi;
        this.loai = loai;
        this.mystep = mystep;
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public String getImg() {
        return img;
    }

    public void setChuanbi(ArrayList<String> chuanbi) {
        this.chuanbi = chuanbi;
    }

    public String getLoai() {
        return loai;
    }

    public List<MyStep> getMystep() {
        return mystep;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<String> getChuanbi() {
        return chuanbi;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }


    public void setMystep(List<MyStep> mystep) {
        this.mystep = mystep;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }
}
