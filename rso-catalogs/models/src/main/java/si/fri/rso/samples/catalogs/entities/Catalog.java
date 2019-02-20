package si.fri.rso.samples.catalogs.entities;

import si.fri.rso.samples.catalogs.dtos.Image;

import javax.persistence.*;
import java.util.List;

@Entity(name = "catalogs")
@NamedQueries(value =
        {
                @NamedQuery(name = "Catalog.getAll", query = "SELECT o FROM catalogs o"),
                @NamedQuery(name = "Catalog.findById", query = "SELECT o FROM catalogs o WHERE o.id = " + ":id")
        })
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String cname;

    @Transient
    private List<Image> images;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
