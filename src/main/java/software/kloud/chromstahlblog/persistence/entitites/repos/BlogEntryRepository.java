package software.kloud.chromstahlblog.persistence.entitites.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.kloud.chromstahlblog.persistence.entitites.BlogEntry;
import software.kloud.kms.entities.UserJpaRecord;

import java.util.List;

@Repository
public interface BlogEntryRepository extends JpaRepository<BlogEntry, Integer> {
    List<BlogEntry> findBlogEntriesByUser(UserJpaRecord userJpaRecord);
}
