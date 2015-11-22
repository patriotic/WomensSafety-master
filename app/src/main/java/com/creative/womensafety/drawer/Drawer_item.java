package com.creative.womensafety.drawer;

/**
 * Created by Syed Ikhtiar Ahmed on 11/22/2015.
 */
public class Drawer_item {
    String text;
    int image;

    public Drawer_item(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
