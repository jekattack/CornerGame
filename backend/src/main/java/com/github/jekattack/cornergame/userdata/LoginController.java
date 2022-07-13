package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CGUserService userService;

    @PostMapping()
    public ResponseEntity<LoginResponse> login(@RequestBody LoginData loginData) {
        try {
            loginData.setUsername(loginData.getUsername().toLowerCase());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));
            CGUser user = userService.findByUsername(loginData.getUsername()).orElseThrow();
            String jwt = jwtService.createToken(new HashMap<>(), user.getId());
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(Principal principal) {
        CGUser user = userService.findByUsername(principal.getName()).orElseThrow();
        String token = jwtService.createToken(new HashMap<>(), user.getId());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
