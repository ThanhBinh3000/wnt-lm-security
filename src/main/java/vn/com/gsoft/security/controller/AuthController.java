package vn.com.gsoft.security.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.security.constant.RecordStatusContains;
import vn.com.gsoft.security.constant.RoleConstant;
import vn.com.gsoft.security.entity.UserProfile;
import vn.com.gsoft.security.model.dto.*;
import vn.com.gsoft.security.model.system.BaseResponse;
import vn.com.gsoft.security.model.system.MessageDTO;
import vn.com.gsoft.security.model.system.Profile;
import vn.com.gsoft.security.service.ApiService;
import vn.com.gsoft.security.service.RedisListService;
import vn.com.gsoft.security.service.UserService;
import vn.com.gsoft.security.util.system.JwtTokenUtil;
import vn.com.gsoft.security.util.system.ResponseUtils;

import java.util.Objects;
import java.util.UUID;


@RestController
@Slf4j
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisListService redisListService;

    @Autowired
    private ApiService apiService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<BaseResponse> authenticate(
            @RequestBody @Valid JwtRequest jwtRequest) {

        try {
            UserProfile username = userService.findByUsername(jwtRequest.getUsername());
            if (username == null) {
                throw new Exception("Sai tên đăng nhập hoặc mật khẩu!");
            }
            //check thanh vien co active khong
            var nhaThuoc = userService.findByMaNhaThuoc(username.getMaNhaThuoc());
            if(!Objects.equals(username.getEntityCode(), RoleConstant.ROLE_ADMIN)){
                if (nhaThuoc == null) {
                    throw new Exception("Người dùng này không là thành viên liên minh!");
                }
                if(nhaThuoc.getRecordStatusId().equals(RecordStatusContains.DELETED)){
                    throw new Exception("Người dùng này đã bị xóa khỏi liên minh!");
                }
            }

            if (username.getPassword() == null) {
                // check pass cũ
                String confirmLogin = apiService.confirmLogin(jwtRequest.getUsername(), jwtRequest.getPassword());
                if (confirmLogin == null) {
                    throw new Exception("Sai tên đăng nhập hoặc mật khẩu!");
                }
                Gson gson = new Gson();
                CheckLogin resp = gson.fromJson(confirmLogin, CheckLogin.class);
                if(resp.getData()!=null && resp.getData()){
                    username.setPassword(this.passwordEncoder.encode(jwtRequest.getPassword()));
                    userService.save(username);
                }
            }

            // Xác thực từ username và password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );

            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);


            // Trả về jwt cho người dùng.
            String token = jwtTokenUtil.generateToken(jwtRequest.getUsername());
            String refreshToken = jwtTokenUtil.generateRefreshToken(jwtRequest.getUsername());

            redisListService.addValueToListEnd(jwtRequest.getUsername(), token);

            return ResponseEntity.ok(ResponseUtils.ok(new JwtResponse(token, refreshToken)));
        } catch (Exception ex) {
            log.error("Authentication error", ex);
            throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse> getUserDetails(Authentication authentication) {
        Profile profile = (Profile) authentication.getPrincipal();
        return ResponseEntity.ok(ResponseUtils.ok(profile));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<BaseResponse> refreshToken(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            refreshToken = requestTokenHeader.substring(7);
            try {
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(refreshToken);
                String type = (String) claims.get("type");
                String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
                if ("refreshtoken".equals(type)) {
                    String jwtToken = jwtTokenUtil.generateToken(username);
                    return ResponseEntity.ok(ResponseUtils.ok(new JwtResponse(jwtToken, refreshToken)));
                }
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        }
        throw new BadCredentialsException("Token invalid!");
    }

    @GetMapping("/passwordEncoder")
    public ResponseEntity<BaseResponse> getPasswordEncoder(String password) {
        return ResponseEntity.ok(ResponseUtils.ok(this.passwordEncoder.encode(password)));
    }
}
