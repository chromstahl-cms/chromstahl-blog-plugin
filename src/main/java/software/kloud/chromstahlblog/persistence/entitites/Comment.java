package software.kloud.chromstahlblog.persistence.entitites;

import software.kloud.kms.entities.UserJpaRecord;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            unique = true
    )
    private Integer id;
    @Lob
    private String content;
    private Date published;

    @ManyToOne
    private UserJpaRecord user;
    @ManyToOne
    private BlogEntry post;

    public Comment() {
    }

    public Comment(
            String content,
            Date published,
            UserJpaRecord user,
            BlogEntry post
    ) {
        this.content = content;
        this.published = published;
        this.user = user;
        this.post = post;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserJpaRecord getUser() {
        return user;
    }

    public void setUser(UserJpaRecord user) {
        this.user = user;
    }

    public BlogEntry getPost() {
        return post;
    }

    public void setPost(BlogEntry post) {
        this.post = post;
    }
}
