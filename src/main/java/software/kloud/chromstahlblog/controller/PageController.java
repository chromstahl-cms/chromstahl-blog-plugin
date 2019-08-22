package software.kloud.chromstahlblog.controller;

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
import software.kloud.chromstahlblog.dto.PageEntryDTO;
import software.kloud.chromstahlblog.dto.PageRequestDTO;
import software.kloud.chromstahlblog.navbar.PageNavbarRegister;
import software.kloud.chromstahlblog.persistence.entitites.PageEntry;
import software.kloud.chromstahlblog.persistence.entitites.repos.PageEntryRepository;
import software.kloud.kms.repositories.UserRepository;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController extends AbsController {
    private final PageEntryRepository pageEntryRepository;
    private final UserRepository userRepository;
    private final PageNavbarRegister pageNavbarRegister;

    public PageController(
            PageEntryRepository pageEntryRepository,
            UserRepository userRepository,
            PageNavbarRegister pageNavbarRegister
    ) {
        this.pageEntryRepository = pageEntryRepository;
        this.userRepository = userRepository;
        this.pageNavbarRegister = pageNavbarRegister;
    }

    @GetMapping("/page")
    public ResponseEntity<List<PageEntryDTO>> getAllPages() {
        return ResponseEntity.ok(pageEntryRepository
                .findAll()
                .stream()
                .map(PageEntryDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/page/entry/{id}")
    public ResponseEntity<PageEntryDTO> getPageEntryById(@PathVariable Integer id) {
        var entryOptional = pageEntryRepository.findById(id);

        if (entryOptional.isPresent()) {
            var dto = entryOptional.map(PageEntryDTO::new).get();
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/page/entry/{id}")
    public ResponseEntity<List<PageEntryDTO>> getAllPageEntriesForAuthor(@PathVariable Long id) {
        var userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(pageEntryRepository
                    .findPageEntriesByUser(userOptional.get())
                    .stream()
                    .map(PageEntryDTO::new)
                    .collect(Collectors.toList()));
        }

        return ResponseEntity.notFound().build();
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/page/publish")
    public ResponseEntity<String> publishEntry(
            @AuthenticationPrincipal Principal principal,
            @RequestBody PageRequestDTO requestDTO
    ) {
        var user = userRepository.findByUserName(getUsernameFromPrincipal(principal)).orElseThrow(() ->
                new SecurityException(String.format("not able to map user: %s to database user",
                        getUsernameFromPrincipal(principal))));

        var pageEntry = new PageEntry();
        pageEntry.setContent(requestDTO.getContent());
        pageEntry.setTitle(requestDTO.getTitle());
        pageEntry.setPublished(new Date());
        pageEntry.setUser(user);

        pageEntryRepository.save(pageEntry);
        pageNavbarRegister.notifyDirty();

        return ResponseEntity.ok("success");
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/page/update/{id}")
    public ResponseEntity<String> updateEntry(
            @AuthenticationPrincipal Principal principal,
            @PathVariable Integer id,
            @RequestBody PageRequestDTO requestDTO
    ) {
        var user = userRepository.findByUserName(getUsernameFromPrincipal(principal)).orElseThrow(() ->
                new SecurityException(String.format("not able to map user: %s to database user",
                        getUsernameFromPrincipal(principal))));

        var pageEntryOptional = pageEntryRepository.findById(id);
        if (pageEntryOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var pageEntry = pageEntryOptional.get();

        if (!pageEntry.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    String.format("User %s is not allowed to modify this page", user.getUserName()));
        }

        pageEntry.setContent(requestDTO.getContent());
        pageEntry.setTitle(requestDTO.getTitle());
        pageEntry.setPublished(new Date());
        pageEntryRepository.save(pageEntry);
        pageNavbarRegister.notifyDirty();

        return ResponseEntity.ok("success");
    }
}
