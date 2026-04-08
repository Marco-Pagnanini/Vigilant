package marcopagnanini.backend.Api;

import marcopagnanini.backend.Infrastructure.Security.JwtUtil;
import marcopagnanini.backend.Utils.Dto.LoginRequest;
import marcopagnanini.backend.Utils.Dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final String adminUsername;
    private final String adminPassword;

    public AuthController(JwtUtil jwtUtil,
                          @Value("${admin.username}") String adminUsername,
                          @Value("${admin.password}") String adminPassword) {
        this.jwtUtil = jwtUtil;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (!adminUsername.equals(request.getUsername()) || !adminPassword.equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(request.getUsername())));
    }
}
