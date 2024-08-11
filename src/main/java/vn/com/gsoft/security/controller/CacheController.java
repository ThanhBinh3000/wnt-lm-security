package vn.com.gsoft.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.security.model.system.BaseResponse;
import vn.com.gsoft.security.service.UserCacheService;
import vn.com.gsoft.security.util.system.ResponseUtils;

@RestController
@RequestMapping("/caches")
public class CacheController {
    @Autowired
    private UserCacheService userCacheService;

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<BaseResponse> clearCacheByUsername(@RequestParam("username") String username) {
        userCacheService.clearCacheByUsername(username);
        return ResponseEntity.ok(ResponseUtils.ok(true));
    }

}
