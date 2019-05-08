package software.kloud.chromstahlblog.dto;

import software.kloud.chromstahlblog.persistence.entitites.BlogEntry;
import software.kloud.chromstahlblog.persistence.entitites.Comment;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BlogEntryDTO {
    private int id;
    private String title;
    private Date published;
    private String content;
    private AuthorDTO author;
    private List<CommentDTO> comments;

    public BlogEntryDTO(BlogEntry blogEntry) {
        this.id = blogEntry.getId();
        this.title = blogEntry.getTitle();
        this.published = blogEntry.getPublished();
        this.content = blogEntry.getContent();
        this.author = new AuthorDTO(blogEntry.getUser());
        this.comments = blogEntry.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getPublished))
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
