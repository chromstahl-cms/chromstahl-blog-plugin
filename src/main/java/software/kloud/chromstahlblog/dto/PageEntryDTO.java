package software.kloud.chromstahlblog.dto;

import software.kloud.chromstahlblog.persistence.entitites.BlogEntry;
import software.kloud.chromstahlblog.persistence.entitites.Comment;
import software.kloud.chromstahlblog.persistence.entitites.PageEntry;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PageEntryDTO {
    private int id;
    private String title;
    private Date published;
    private String content;
    private AuthorDTO author;
    private List<CommentDTO> comments;

    public PageEntryDTO(PageEntry pageEntry) {
        this.id = pageEntry.getId();
        this.title = pageEntry.getTitle();
        this.published = pageEntry.getPublished();
        this.content = pageEntry.getContent();
        this.author = new AuthorDTO(pageEntry.getUser());
        this.comments = pageEntry.getComments()
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
