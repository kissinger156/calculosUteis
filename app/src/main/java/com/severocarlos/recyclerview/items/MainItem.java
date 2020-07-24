package com.severocarlos.recyclerview.items;

public class MainItem {
    private int id;
    private String name;
    private int imgResource;
    private int colorValue;

    public MainItem(int id, String name, int imgResource, int colorValue) {
        this.id = id;
        this.name = name;
        this.imgResource = imgResource;
        this.colorValue = colorValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImgResource() { return imgResource; }

    public int getColorValue() {
        return colorValue;
    }

    public void setId(int id) {
        this.id = id;
    }

}
