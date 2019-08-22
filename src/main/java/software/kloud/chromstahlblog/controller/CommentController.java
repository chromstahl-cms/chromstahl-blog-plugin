package software.kloud.chromstahlblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import software.kloud.ChromPluginSDK.AbsController;
import software.kloud.chromstahlblog.dto.CommentDTO;
import software.kloud.chromstahlblog.dto.CommentRequestDTO;
import software.kloud.chromstahlblog.persistence.entitites.Comment;
import software.kloud.chromstahlblog.persistence.entitites.repos.BlogEntryRepository;
import software.kloud.chromstahlblog.persistence.entitites.repos.CommentRepository;
import software.kloud.kms.repositories.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class CommentController extends AbsController {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BlogEntryRepository blogEntryRepository;

    public CommentController(
            UserRepository userRepository,
            CommentRepository commentRepository,
            BlogEntryRepository blogEntryRepository
    ) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.blogEntryRepository = blogEntryRepository;
    }

    @PostMapping("/blog/comment/{id}")
    public ResponseEntity<CommentDTO> save(
            @AuthenticationPrincipal Principal principal,
            @PathVariable("id") Integer postId,
            @RequestBody CommentRequestDTO dto
    ) {
        var user = userRepository.findByUserName(getUsernameFromPrincipal(principal)).orElseThrow(() ->
                new SecurityException(String.format("not able to map user: %s to database user",
                        getUsernameFromPrincipal(principal))));

        var postOptional = blogEntryRepository.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var post = postOptional.get();
        var comment = new Comment(dto.getContent(), new Date(), user, post);

        var postComments = post.getComments();
        if (null == postComments) {
            var comments = new ArrayList<Comment>();
            comments.add(comment);
            post.setComments(comments);
        } else {
            postComments.add(comment);
            post.setComments(postComments);
        }
        commentRepository.save(comment);
        blogEntryRepository.save(post);

        return ResponseEntity.ok(new CommentDTO(comment));
    }
}
