package com.vterroso.carregistry.controller;


import com.vterroso.carregistry.controller.dto.LoginRequest;
import com.vterroso.carregistry.controller.dto.SingUpRequest;

import com.vterroso.carregistry.service.UserService;
import com.vterroso.carregistry.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> singup(@RequestBody SingUpRequest request) {
       try {
           return ResponseEntity.ok(authenticationServiceImpl.signup(request));
       }
       catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authenticationServiceImpl.login(request));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/userImage/{id}/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('VENDOR','CLIENT')")
    public ResponseEntity<?> addImage(@PathVariable Long id, @RequestParam("imageFile")MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()){
            log.error("The file is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }

        if(Objects.requireNonNull(imageFile.getOriginalFilename()).endsWith(".jpg")){
            log.info("File name: {}", imageFile.getOriginalFilename());

            // Add user image
            authenticationServiceImpl.addImage(id, imageFile);
            log.info("Image successfully saved.");
            return ResponseEntity.ok("Image successfully saved.");
        }

        log.error("The file is not a jpg file");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is not a jpg file");

    }
    @GetMapping(value = "/userImage/{id}")
    @PreAuthorize("hasAnyRole('VENDOR','CLIENT')")
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id){
        try {

            byte[] imageBytes = userService.getUserImage(id);
            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes,headers,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
