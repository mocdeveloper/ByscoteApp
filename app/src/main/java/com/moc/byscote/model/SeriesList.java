package com.moc.byscote.model;

public class SeriesList {

    String img_src;
    String series_name;
    String series_id;

    public SeriesList(String img_src, String series_name, String series_id) {

        this.img_src = img_src;
        this.series_name = series_name;
        this.series_id = series_id;

    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getSeries_id() {
        return series_id;
    }

    public void setSeries_id(String series_id) {
        this.series_id = series_id;
    }
}
