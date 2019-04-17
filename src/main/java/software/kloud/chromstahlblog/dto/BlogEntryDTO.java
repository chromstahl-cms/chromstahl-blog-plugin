package software.kloud.chromstahlblog.dto;

import software.kloud.chromstahlblog.persistence.entitites.BlogEntry;

import java.util.Date;

public class BlogEntryDTO {
    private String title;
    private Date published;
    private String content;
    private AuthorDTO author;

    public BlogEntryDTO(BlogEntry blogEntry) {
        this.title = blogEntry.getTitle();
        this.published = blogEntry.getPublished();
        this.content = blogEntry.getContent();
        this.author = new AuthorDTO(blogEntry.getUser());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }
}
