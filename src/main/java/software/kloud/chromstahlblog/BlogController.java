package software.kloud.chromstahlblog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.kloud.ChromPluginSDK.AbsController;
import software.kloud.chromstahlblog.dto.BlogEntryDTO;
import software.kloud.chromstahlblog.dto.BlogRequestDTO;
import software.kloud.chromstahlblog.persistence.entitites.BlogEntry;
import software.kloud.chromstahlblog.persistence.entitites.repos.BlogEntryRepository;
import software.kloud.kms.repositories.UserRepository;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BlogController extends AbsController {

    private final BlogEntryRepository blogEntryRepository;
    private final UserRepository userRepository;

    public BlogController(BlogEntryRepository repo, UserRepository userRepository) {
        this.blogEntryRepository = repo;
        this.userRepository = userRepository;
    }

    @GetMapping("/blog")
    public ResponseEntity<List<BlogEntryDTO>> getAllBlogPosts() {
        return ResponseEntity.ok(blogEntryRepository
                .findAll()
                .stream()
                .map(BlogEntryDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/blog/entry/{id}")
    public ResponseEntity<BlogEntryDTO> getBlogEntryById(@PathVariable Integer id) {
        var entryOptional = blogEntryRepository.findById(id);

        if (entryOptional.isPresent()) {
            var dto = entryOptional.map(BlogEntryDTO::new).get();
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/blog/user/{id}")
    public ResponseEntity<List<BlogEntryDTO>> getAllEntriesByAuthor(@PathVariable Long id) {
        var userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(blogEntryRepository
                    .findBlogEntriesByUser(userOptional.get())
                    .stream()
                    .map(BlogEntryDTO::new)
                    .collect(Collectors.toList()));
        }

        return ResponseEntity.notFound().build();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/blog/publish")
    public ResponseEntity<String> publishEntry(@AuthenticationPrincipal Principal principal, @RequestBody BlogRequestDTO requestDTO) {
        var user = userRepository.findByUserName(getUsernameFromPrincipal(principal)).orElseThrow(() ->
                new SecurityException(String.format("not able to map user: %s to database user",
                        getUsernameFromPrincipal(principal))));

        var blogEntry = new BlogEntry();
        blogEntry.setContent(requestDTO.getContent());
        blogEntry.setTitle(requestDTO.getTitle());
        blogEntry.setPublished(new Date());
        blogEntry.setUser(user);

        blogEntryRepository.save(blogEntry);
        return ResponseEntity.ok("success");
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/blog/update/{id}")
    public ResponseEntity<String> updateEntry(@AuthenticationPrincipal Principal principal, @PathVariable Integer id, @RequestBody BlogRequestDTO requestDTO) {
        var user = userRepository.findByUserName(getUsernameFromPrincipal(principal)).orElseThrow(() ->
                new SecurityException(String.format("not able to map user: %s to database user",
                        getUsernameFromPrincipal(principal))));

        var blogEntryOptional = blogEntryRepository.findById(id);
        if (blogEntryOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var blogEntry = blogEntryOptional.get();

        if (!blogEntry.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    String.format("User %s is not allowed to modify this blogpost", user.getUserName()));
        }

        blogEntry.setContent(requestDTO.getContent());
        blogEntry.setTitle(requestDTO.getTitle());
        blogEntry.setPublished(new Date());
        blogEntryRepository.save(blogEntry);

        return ResponseEntity.ok("success");
    }
}
