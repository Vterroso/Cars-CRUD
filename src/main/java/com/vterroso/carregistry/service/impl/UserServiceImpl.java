package com.vterroso.carregistry.service.impl;


import com.vterroso.carregistry.repository.UserRepository;
import com.vterroso.carregistry.repository.entity.UserEntity;
import com.vterroso.carregistry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;


    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserEntity save(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public void addUserImage(Long id, MultipartFile imageFile) throws IOException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(RuntimeException::new);
        log.info("Saving user image...");
        userEntity.setImage(Base64.getEncoder().encodeToString(imageFile.getBytes()));
        userRepository.save(userEntity);
    }

    @Override
    public byte[] getUserImage(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return Base64.getDecoder().decode(userEntity.getImage());
    }

}
