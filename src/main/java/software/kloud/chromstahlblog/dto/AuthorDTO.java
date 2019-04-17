package software.kloud.chromstahlblog.dto;

import software.kloud.kms.entities.UserJpaRecord;

public class AuthorDTO {
    private String userName;
    private Integer id;

    public AuthorDTO(UserJpaRecord userJpaRecord) {
        this.userName = userJpaRecord.getUserName();
        this.id = userJpaRecord.getId();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
