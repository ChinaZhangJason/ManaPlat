package com.platform.integrate.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.auth.security.CustomUserDetails;
import com.platform.common.Result;
import com.platform.integrate.model.IntegratePlatform;
import com.platform.integrate.service.PlatformService;
import com.platform.integrate.service.SsoTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/integrate")
public class IntegrateController {

    private final PlatformService platformService;
    private final SsoTokenService ssoTokenService;

    public IntegrateController(PlatformService platformService, SsoTokenService ssoTokenService) {
        this.platformService = platformService;
        this.ssoTokenService = ssoTokenService;
    }

    @GetMapping("/platforms")
    public Result<IPage<IntegratePlatform>> listPlatforms(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer status) {
        IPage<IntegratePlatform> page = platformService.page(current, size, name, code, status);
        return Result.success(page);
    }

    @GetMapping("/platforms/{id}")
    public Result<IntegratePlatform> getPlatform(@PathVariable Long id) {
        IntegratePlatform platform = platformService.getById(id);
        if (platform == null) {
            return Result.error(404, "Platform not found");
        }
        return Result.success(platform);
    }

    @GetMapping("/platforms/code/{code}")
    public Result<IntegratePlatform> getPlatformByCode(@PathVariable String code) {
        IntegratePlatform platform = platformService.getByCode(code);
        if (platform == null) {
            return Result.error(404, "Platform not found");
        }
        return Result.success(platform);
    }

    @PostMapping("/platforms")
    public Result<Boolean> addPlatform(@RequestBody IntegratePlatform platform) {
        boolean result = platformService.addPlatform(platform);
        return result ? Result.success(true) : Result.error("Failed to add platform");
    }

    @PutMapping("/platforms/{id}")
    public Result<Boolean> updatePlatform(@PathVariable Long id, @RequestBody IntegratePlatform platform) {
        platform.setId(id);
        boolean result = platformService.updatePlatform(platform);
        return result ? Result.success(true) : Result.error("Failed to update platform");
    }

    @DeleteMapping("/platforms/{id}")
    public Result<Boolean> deletePlatform(@PathVariable Long id) {
        boolean result = platformService.deletePlatform(id);
        return result ? Result.success(true) : Result.error("Failed to delete platform");
    }

    @PostMapping("/sso-token")
    public Result<Map<String, Object>> generateSsoToken(@RequestBody Map<String, String> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        String platformCode = request.get("platformCode");
        if (!StringUtils.hasText(platformCode)) {
            return Result.error(400, "Platform code is required");
        }

        IntegratePlatform platform = platformService.getByCode(platformCode);
        if (platform == null) {
            return Result.error(404, "Platform not found");
        }

        if (platform.getStatus() != 1) {
            return Result.error(403, "Platform is disabled");
        }

        Long userId = getCurrentUserId(authentication);
        if (!platformService.hasAccess(userId, platformCode)) {
            return Result.error(403, "You do not have access to this platform");
        }

        String token = ssoTokenService.generateToken(userId, username, platformCode, platform.getAppSecret());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("platformCode", platformCode);
        result.put("expiresIn", 300);
        result.put("expiresAt", java.time.LocalDateTime.now().plusMinutes(5));
        
        return Result.success(result);
    }

    @PostMapping("/iframe-url")
    public Result<Map<String, String>> getIframeUrl(@RequestBody Map<String, String> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        String platformCode = request.get("platformCode");
        if (!StringUtils.hasText(platformCode)) {
            return Result.error(400, "Platform code is required");
        }

        IntegratePlatform platform = platformService.getByCode(platformCode);
        if (platform == null) {
            return Result.error(404, "Platform not found");
        }

        if (platform.getStatus() != 1) {
            return Result.error(403, "Platform is disabled");
        }

        Long userId = getCurrentUserId(authentication);
        if (!platformService.hasAccess(userId, platformCode)) {
            return Result.error(403, "You do not have access to this platform");
        }

        String ssoUrl = ssoTokenService.generateSsoUrl(
                platformCode,
                platform.getBaseUrl(),
                platform.getSsoEndpoint(),
                userId,
                username,
                platform.getAppSecret()
        );

        Map<String, String> result = new HashMap<>();
        result.put("url", ssoUrl);
        result.put("platformCode", platformCode);
        result.put("platformName", platform.getName());
        result.put("expiresAt", java.time.LocalDateTime.now().plusMinutes(5).toString());
        
        return Result.success(result);
    }

    private Long getCurrentUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        return 1L;
    }
}
