package si.fri.rso.samples.comment.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "comments")
@NamedQueries(value =
        {
                @NamedQuery(name = "Comment.getAll", query = "SELECT o FROM comments o"),
                @NamedQuery(name = "Comment.findById", query = "SELECT o FROM comments o WHERE o.id = " + ":id")
        })
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer imageId;

    private String text;

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

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
