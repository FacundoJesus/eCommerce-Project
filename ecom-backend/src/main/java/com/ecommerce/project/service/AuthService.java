package com.ecommerce.project.service;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AuthenticationResult;
import com.ecommerce.project.payload.UserDTO;
import com.ecommerce.project.payload.UserResponse;
import com.ecommerce.project.repositories.iRoleRepository;
import com.ecommerce.project.repositories.iUserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService implements iAuthService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private iRoleRepository roleRepository;
    @Autowired
    private iUserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AuthenticationResult login(LoginRequest loginRequest) {

        //Va a contener el usuario autenticado
        //Autenticación !!
        //Internamente llama a UserDetailsServiceImpl.loadUserByUsername()
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        //Guardar autenticación
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Obtener usuario autenticado
        // tiene el id, username y roles.
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        //Generar cookie con JWT
        ResponseCookie jwtCokie = jwtUtils.generateJwtCookie(userDetails);
        //Obtener roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        //Crear respuesta - DTO al Front
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),roles);

        return new AuthenticationResult(response,jwtCokie);
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signupRequest) {

        if(userRepository.existsByUserName(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();

        Set<Role> roles = new HashSet<>();
        if(strRoles == null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow (() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);

        }
        else {
            strRoles.forEach( role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow (() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow (() -> new RuntimeException("Error: Role is not found"));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow (() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });

        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Succesfully!!"));
    }

    @Override
    public UserInfoResponse getCurrentUserDetails(Authentication authentication) {
        //Recupero el usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(rol -> rol.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),roles);

        return response;
    }

    @Override
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    @Override
    public UserResponse getAllSellers(Pageable pageDetails) {

        Page<User> allUsers = userRepository.findByRoleName(AppRole.ROLE_SELLER, pageDetails);

        List<UserDTO> userDTOS = allUsers.getContent()
                .stream()
                .map(u-> modelMapper.map(u, UserDTO.class))
                .toList();

        UserResponse response = new UserResponse();
        response.setContent(userDTOS);
        response.setPageNumber(allUsers.getNumber());
        response.setPageSize(allUsers.getSize());
        response.setTotalElements(allUsers.getTotalElements());
        response.setTotalPages(allUsers.getTotalPages());
        response.setLastPage(allUsers.isLast());

        return response;
    }
}