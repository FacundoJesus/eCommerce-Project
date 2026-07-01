package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.AuthenticationResult;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "Authentications APIs", description = "APIs for managing authentications") //Swagger: Agrupar metodos
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticate user", description = "API to Authenticate user")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        AuthenticationResult result  = authService.login(loginRequest);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                result.getJwtCookie().toString()).body(result.getResponse());
    }

    @Operation(summary = "Register user", description = "API to Register user")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.registerUser(signupRequest);
    }

    @Operation(summary = "Get username of the authenticated user", description = "API to Get username of the authenticated user")
    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if(authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    @Operation(summary = "Get details of the authenticated user", description = "API to Get details of the authenticated user")
    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {

        return ResponseEntity.ok().body(authService.getCurrentUserDetails(authentication));

    }

    @Operation(summary = "Sign Out", description = "API to Sign Out")
    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser() {

        ResponseCookie cleanCookie = authService.logoutUser();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cleanCookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<?>getAllSellers(@RequestParam(
            name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber) {

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, Integer.parseInt(AppConstants.PAGE_SIZE), sortByAndOrder);

        return ResponseEntity.ok(authService.getAllSellers(pageDetails));

    }


}
