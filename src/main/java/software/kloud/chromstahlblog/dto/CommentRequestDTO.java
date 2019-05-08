package software.kloud.chromstahlblog.dto;

public class CommentRequestDTO {
    private String content;

    public CommentRequestDTO(String content) {
        this.content = content;
    }

    public CommentRequestDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
