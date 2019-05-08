package software.kloud.chromstahlblog.dto;

import software.kloud.chromstahlblog.persistence.entitites.Comment;

import java.util.Date;

public class CommentDTO {
    private int id;
    private Date published;
    private String content;
    private AuthorDTO author;

    public CommentDTO(Comment entry) {
        this.id = entry.getId();
        this.published = entry.getPublished();
        this.content = entry.getContent();
        this.author = new AuthorDTO(entry.getUser());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }
}
