package org.locate.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.locate.entity.AuthRequest;
import org.locate.entity.UserInfo;
import org.locate.service.JwtService;
import org.locate.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        System.out.println("/signup request received.");
        return service.addUser(userInfo);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            System.out.println("/signin request received.");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(authRequest.getEmail());
                Cookie cookie = new Cookie("token", jwtToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setMaxAge(1200);
                response.addCookie(cookie);
                return ResponseEntity.ok("Login successful");
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (InternalAuthenticationServiceException e){
            throw new RuntimeException("Authentication failed :", e.getCause() );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout successful");
    }

}