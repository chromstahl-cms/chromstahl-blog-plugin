package software.kloud.chromstahlblog.navbar;

import org.springframework.stereotype.Component;
import software.kloud.ChromPluginSDK.NavBarEntity;
import software.kloud.ChromPluginSDK.NavBarLinkRegister;
import software.kloud.ChromPluginSDK.RoleService;
import software.kloud.chromstahlblog.persistence.entitites.repos.PageEntryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Manages navbar items for pages
 *
 * Holds a local list of mapped navbar entries. Gets notified from controllers when changes to
 * the {@link PageEntryRepository} occur. Thread safe.
 */
@Component
public class PageNavbarRegister implements NavBarLinkRegister {
    private final PageEntryRepository pageEntryRepository;
    private final RoleService roleService;
    private final AtomicBoolean dirty;
    private final ReentrantLock navBarEntitiesLock;
    private List<NavBarEntity> navBarEntities;

    public PageNavbarRegister(
            PageEntryRepository pageEntryRepository,
            RoleService roleService
    ) {
        this.pageEntryRepository = pageEntryRepository;
        this.roleService = roleService;
        this.navBarEntities = new ArrayList<>();
        this.navBarEntitiesLock = new ReentrantLock();
        this.dirty = new AtomicBoolean(true);
        this.sync();
    }

    private void sync() {
        this.navBarEntitiesLock.lock();
        try {
            this.navBarEntities = this.pageEntryRepository
                    .findAll()
                    .stream()
                    .map(page -> new NavBarEntity(
                            page.getTitle(),
                            String.format("/page/%d", page.getId()),
                            roleService.getGuestRole())
                    )
                    .collect(Collectors.toList());
        } finally {
            this.dirty.set(false);
            navBarEntitiesLock.unlock();
        }
    }

    /**
     * Sets the state of the cache to dirty.
     * On next call to {@link PageNavbarRegister#register()} navbar items will be re-generated
     */
    public void notifyDirty() {
        this.dirty.set(true);
    }

    @Override
    public List<NavBarEntity> register() {
        if (this.dirty.get()) {
            this.sync();
        }

        return this.navBarEntities;
    }
}
