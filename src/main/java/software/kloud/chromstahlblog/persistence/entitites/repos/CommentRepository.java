package software.kloud.chromstahlblog.persistence.entitites.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kloud.chromstahlblog.persistence.entitites.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
