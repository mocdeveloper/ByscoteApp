package com.moc.byscote.model;

public class EpisodesList {

    String img_src;
    String series_name;
    String series_id;
    String description;
    String episode_name;
    String episode_id;

    public EpisodesList(String img_src, String series_name, String series_id, String description,
                        String episode_name, String episode_id) {

        this.img_src = img_src;
        this.series_name = series_name;
        this.series_id = series_id;
        this.description = description;
        this.episode_name = episode_name;
        this.episode_id = episode_id;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEpisode_name() {
        return episode_name;
    }

    public void setEpisode_name(String episode_name) {
        this.episode_name = episode_name;
    }

    public String getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(String episode_id) {
        this.episode_id = episode_id;
    }
}
