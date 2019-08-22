package software.kloud.chromstahlblog.persistence.entitites;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import software.kloud.kms.entities.UserJpaRecord;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class PageEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            unique = true
    )
    private Integer id;
    private String title;
    @Lob
    private String content;
    private Date published;

    @ManyToOne
    private UserJpaRecord user;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments;

    public PageEntry(String title, String content, Date published, UserJpaRecord user) {
        this.title = title;
        this.content = content;
        this.published = published;
        this.user = user;
    }

    public PageEntry() {
    }

    public UserJpaRecord getUser() {
        return user;
    }

    public void setUser(UserJpaRecord user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
