package si.fri.rso.samples.catalogs.dtos;

import java.time.Instant;

public class Image {
    private Integer id;
    private String caption;
    private String url;
    private Instant posted;
    private Integer catalogId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public Instant getPosted() {
        return posted;
    }

    public void setPosted(Instant posted) {
        this.posted = posted;
    }

    public Integer getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Integer catalogId) {
        this.catalogId = catalogId;
    }
}
