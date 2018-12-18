package com.moc.byscote.model;

public class MenuModel {

    public String menuName, category_id;
    public int menuImage;
    public int secondImage;
    public boolean hasChildren, isGroup;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String category_id,int menuImage,int secondImage) {

        this.menuName = menuName;
        this.category_id = category_id;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.menuImage = menuImage;
        this.secondImage = secondImage;
    }
}