package software.kloud.chromstahlblog.navbar;

import org.springframework.stereotype.Component;
import software.kloud.ChromPluginSDK.NavBarEntity;
import software.kloud.ChromPluginSDK.NavBarLinkRegister;
import software.kloud.ChromPluginSDK.RoleService;

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
                new NavBarEntity("New blog post", "/admin/blog/new", roleService.getAdminRole()),
                new NavBarEntity("New page", "/admin/page/new", roleService.getAdminRole())
        );
    }
}
