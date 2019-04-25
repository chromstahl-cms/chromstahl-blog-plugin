package software.kloud.chromstahlblog;

import org.springframework.stereotype.Component;
import software.kloud.KMSPluginSDK.NavBarEntity;
import software.kloud.KMSPluginSDK.NavBarLinkRegister;
import software.kloud.KMSPluginSDK.RoleService;

import java.util.List;

@Component
public class AdminNavBarRegister implements NavBarLinkRegister {
    private final RoleService roleService;

    public AdminNavBarRegister(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public List<NavBarEntity> register() {
        return List.of(
                new NavBarEntity("New blog post", "/admin/blog/new", roleService.getAdminRole())
        );
    }
}
