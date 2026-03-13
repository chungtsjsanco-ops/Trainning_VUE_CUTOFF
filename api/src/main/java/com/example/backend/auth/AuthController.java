package com.example.backend.auth;

import com.example.backend.common.ApiResult;
import com.example.backend.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Authentication", description = "Đăng nhập và lấy JWT token")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Đăng nhập", description = "Trả về JWT token dùng cho các request tiếp theo (Authorization: Bearer <token>)")
    @PostMapping("/login")
    public ResponseEntity<ApiResult> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtUtil.generateToken(auth.getName());
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            ApiResult result = new ApiResult(new LoginResponse(token, auth.getName(), roles));
            return ResponseEntity.ok(result);

        } catch (BadCredentialsException e) {
            ApiResult result = new ApiResult();
            result.setErrorCode("99999999");
            result.setMessage("Tên đăng nhập hoặc mật khẩu không đúng");
            return ResponseEntity.status(401).body(result);
        }
    }
}
