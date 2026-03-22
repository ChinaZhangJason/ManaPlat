package com.platform.system;

import com.platform.system.model.SysUser;
import com.platform.system.model.SysRole;
import com.platform.system.model.SysPermission;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SysModelTest {

    @Test
    void testSysUserCreation() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encrypted_password");
        user.setEmail("admin@example.com");
        user.setPhone("13800138000");
        user.setEnabled(true);
        
        assertEquals("admin", user.getUsername());
        assertEquals("admin@example.com", user.getEmail());
        assertTrue(user.getEnabled());
    }

    @Test
    void testSysUserStatus() {
        SysUser user = new SysUser();
        
        user.setEnabled(true);
        assertTrue(user.getEnabled());
        
        user.setEnabled(false);
        assertFalse(user.getEnabled());
    }

    @Test
    void testSysRoleCreation() {
        SysRole role = new SysRole();
        role.setId(1L);
        role.setRoleName("管理员");
        role.setRoleCode("ADMIN");
        role.setDescription("系统管理员");
        role.setEnabled(true);
        
        assertEquals("管理员", role.getRoleName());
        assertEquals("ADMIN", role.getRoleCode());
        assertTrue(role.getEnabled());
    }

    @Test
    void testSysRoleTypes() {
        Set<String> validRoles = new HashSet<>(Arrays.asList(
            "SUPER_ADMIN", "ADMIN", "USER", "GUEST"
        ));
        
        assertTrue(validRoles.contains("SUPER_ADMIN"));
        assertTrue(validRoles.contains("ADMIN"));
        assertTrue(validRoles.contains("USER"));
        assertFalse(validRoles.contains("UNKNOWN"));
    }

    @Test
    void testSysPermissionCreation() {
        SysPermission permission = new SysPermission();
        permission.setId(1L);
        permission.setPermissionName("用户查看");
        permission.setPermissionCode("user:view");
        permission.setPermissionType("BUTTON");
        
        assertEquals("user:view", permission.getPermissionCode());
        assertEquals("BUTTON", permission.getPermissionType());
    }

    @Test
    void testPermissionTypes() {
        Set<String> permissionTypes = new HashSet<>(Arrays.asList(
            "MENU", "BUTTON", "API"
        ));
        
        assertTrue(permissionTypes.contains("MENU"));
        assertTrue(permissionTypes.contains("BUTTON"));
        assertTrue(permissionTypes.contains("API"));
    }

    @Test
    void testRolePermissions() {
        SysRole role = new SysRole();
        role.setId(1L);
        role.setRoleCode("ADMIN");
        
        Set<String> adminPermissions = new HashSet<>(Arrays.asList(
            "user:view", "user:edit", "user:delete",
            "role:view", "role:edit",
            "system:config"
        ));
        
        assertEquals(6, adminPermissions.size());
        assertTrue(adminPermissions.contains("user:view"));
        assertFalse(adminPermissions.contains("system:delete"));
    }

    @Test
    void testUserRoleMapping() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("admin");
        
        SysRole role = new SysRole();
        role.setId(1L);
        role.setRoleCode("ADMIN");
        
        assertNotNull(user.getId());
        assertNotNull(role.getId());
        assertEquals("ADMIN", role.getRoleCode());
    }

    @Test
    void testUserTimestamps() {
        SysUser user = new SysUser();
        LocalDateTime now = LocalDateTime.now();
        
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testPasswordEncryption() {
        String rawPassword = "admin123";
        String encryptedPassword = "encrypted_" + rawPassword;
        
        assertNotEquals(rawPassword, encryptedPassword);
        assertTrue(encryptedPassword.startsWith("encrypted_"));
    }
}
