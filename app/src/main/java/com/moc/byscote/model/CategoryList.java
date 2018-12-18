package com.moc.byscote.model;

public class CategoryList {

    String img_src;
    String category_name;
    String category_id;


    public CategoryList(String img_src, String category_name, String category_id) {

        this.img_src = img_src;
        this.category_name = category_name;
        this.category_id = category_id;

    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
