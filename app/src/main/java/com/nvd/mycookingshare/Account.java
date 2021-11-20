package com.nvd.mycookingshare;

import java.util.ArrayList;

public class Account {
    protected String username;
    protected String pass;
    protected ArrayList<String> listFavoryte;

    public Account() {
    }

    public Account(String username, String pass, ArrayList<String> listFavoryte) {
        this.username = username;
        this.pass = pass;
        this.listFavoryte = listFavoryte;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setListFavoryte(ArrayList<String> listFavoryte) {
        this.listFavoryte = listFavoryte;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public ArrayList<String> getListFavoryte() {
        return listFavoryte;
    }
}
