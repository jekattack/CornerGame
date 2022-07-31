package com.github.jekattack.cornergame.userdata;

import com.github.jekattack.cornergame.model.CGErrorDTO;
import com.github.jekattack.cornergame.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CGUserService userService;

    @PostMapping()
    public ResponseEntity<Object> login(@RequestBody LoginData loginData) {
        try {
            loginData.setUsername(loginData.getUsername().toLowerCase());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.getUsername(), loginData.getPassword()));
            CGUser user = userService.findByUsername(loginData.getUsername()).orElseThrow();

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());
            String jwt = jwtService.createToken(claims, user.getId());

            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Login failed"));
        }
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> refreshToken(Principal principal) {

        try{
            //principal.getName() contains userId
            CGUser user = userService.getUser(principal.getName()).orElseThrow();
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());

            String token = jwtService.createToken(claims, user.getId());
            return ResponseEntity.ok().body(new LoginResponse(token));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CGErrorDTO("Token invalid.", e.getMessage()));
        }
    }
}
