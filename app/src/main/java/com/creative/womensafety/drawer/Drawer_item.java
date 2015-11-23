package com.creative.womensafety.drawer;

import java.util.ArrayList;

/**
 * Created by Syed Ikhtiar Ahmed on 11/22/2015.
 */
public class Drawer_item {
    private String header;
    private ArrayList<String> child;
    int header_icon;
    ArrayList<Integer> child_icon;



    public Drawer_item(String header, int header_icon) {
        this.header = header;
        this.header_icon = header_icon;
        this.child = null;
        this.child_icon = null;
    }

    public Drawer_item(String header, ArrayList<String> child, int header_icon, ArrayList<Integer> child_icon) {
        this.header = header;
        this.header_icon = header_icon;
        this.child = child;
        this.child_icon = child_icon;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<String> getChild() {
        return child;
    }

    public void setChild(ArrayList<String> child) {
        this.child = child;
    }

    public void setHeader_icon(int header_icon){
        this.header_icon = header_icon;
    }
    public void setChild_icon(ArrayList<Integer> child_icon){
        this.child_icon = child_icon;
    }

    public int getHeader_icon(){
        return this.header_icon;
    }
    public ArrayList<Integer> getChild_icon(){
        return  this.child_icon;
    }
}
